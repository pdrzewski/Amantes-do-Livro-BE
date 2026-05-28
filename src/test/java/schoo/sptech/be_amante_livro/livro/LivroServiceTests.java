package schoo.sptech.be_amante_livro.livro;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import schoo.sptech.be_amante_livro.dto.LivroRequestDto;
import schoo.sptech.be_amante_livro.dto.LivroResponseDto;
import schoo.sptech.be_amante_livro.exception.AutorNaoEncontradoException;
import schoo.sptech.be_amante_livro.exception.LivroNaoEncontradoException;
import schoo.sptech.be_amante_livro.model.Autor;
import schoo.sptech.be_amante_livro.model.Editora;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.AutorRepository;
import schoo.sptech.be_amante_livro.repository.EditoraRepository;
import schoo.sptech.be_amante_livro.repository.ExemplarRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;
import schoo.sptech.be_amante_livro.service.LivroService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class LivroServiceTests {

    private static final Logger log = LoggerFactory.getLogger(LivroServiceTests.class);
    @Mock
    private LivroRepository livroRepository;

    @Mock
    private AutorRepository autorRepository;

    @Mock
    private EditoraRepository editoraRepository;

    @Mock
    private ExemplarRepository exemplarRepository;

    @InjectMocks
    private LivroService livroService;

    @Nested
    @DisplayName("Deve testar o método de cadastrar")
    class cadastrar {
        @Test
        @DisplayName("Deve cadastrar livro e retornar ResponseDto com sucesso")
        void deveCadastrarLivroComSucessoTest() {
            LivroRequestDto dto = new LivroRequestDto();
            dto.setTitulo("Clean Code");
            dto.setIdAutor(1);
            dto.setIdEditora(1);

            Autor autor = new Autor();
            autor.setIdAutor(1);

            Editora editora = new Editora();
            editora.setIdEditora(1);

            Livro livroSalvo = new Livro();
            livroSalvo.setIdLivro(1);
            livroSalvo.setTitulo("Clean Code");
            livroSalvo.setAutor(autor);
            livroSalvo.setEditora(editora);

            Mockito.when(autorRepository.findById(1)).thenReturn(Optional.of(autor));
            Mockito.when(editoraRepository.findById(1)).thenReturn(Optional.of(editora));
            Mockito.when(livroRepository.save(any())).thenReturn(livroSalvo);

            LivroResponseDto resultado = livroService.cadastrar(dto);

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals(1, resultado.getIdLivro());
            Assertions.assertEquals("Clean Code", resultado.getTitulo());
        }

        @Test
        @DisplayName("Deve testar quando cadastro não retorna responseDTO")
        void deveVerificarFalhaRetornoResponseQuandoCadastradoTest() {
            LivroRequestDto dto = new LivroRequestDto();
            dto.setTitulo("Clean Code");
            dto.setIdAutor(1);
            dto.setIdEditora(1);

            Autor autor = new Autor();
            autor.setIdAutor(1);

            Editora editora = new Editora();
            editora.setIdEditora(1);

            Mockito.when(autorRepository.findById(1)).thenReturn(Optional.of(autor));
            Mockito.when(editoraRepository.findById(1)).thenReturn(Optional.of(editora));
            Mockito.when(livroRepository.save(any())).thenReturn(null);

            Assertions.assertThrows(NullPointerException.class, () -> livroService.cadastrar(dto));
        }
    }

    @Nested
    @DisplayName("Deve testar método de busca")
    class buscar {

        @Test
        @DisplayName("Deve buscar e retornar um livro por ID com sucesso")
        void deveBuscarERetornarLivroPorIdTest() {
            Livro livro = new Livro();
            livro.setIdLivro(1);

            Integer id = 1;

            Mockito.when(livroRepository.findById(id)).thenReturn(Optional.of(livro));

            LivroResponseDto resultado = livroService.buscarPorId(id);

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals(id, resultado.getIdLivro());
        }

        @Test
        @DisplayName("Deve testar quando livro não é encontrado pelo ID")
        void deveVerificarQuandoLivroNaoEncontradoPeloIdTest() {
            Integer id = 12;

            Mockito.when(livroRepository.findById(id)).thenReturn(Optional.empty());

            Assertions.assertThrows(LivroNaoEncontradoException.class, () -> livroService.buscarPorId(id));
        }
    }

    @Nested
    @DisplayName("Deve testar metodo de deletar")
    class deletar {

        @Test
        @DisplayName("Deve testar metodo de deletar livro por ID")
        void deveDeletarLivroComSucessoPorId () {
            Integer id = 1;

            Livro livro = new Livro();
            livro.setIdLivro(id);

            Mockito.when(livroRepository.findById(id)).thenReturn(Optional.of(livro));
            Mockito.when(exemplarRepository.existsByLivroIdLivro(id)).thenReturn(false);

            livroService.deletar(id);

            Mockito.verify(livroRepository, Mockito.times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar livro não encontrado")
        void deveLancarExcecaoAoDeletarLivroNaoEncontradoTest() {
            Integer id = 99;

            Mockito.when(livroRepository.findById(id)).thenReturn(Optional.empty());

            Assertions.assertThrows(LivroNaoEncontradoException.class, () -> livroService.deletar(id));
            Mockito.verify(livroRepository, Mockito.never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar livro com exemplares cadastrados")
        void deveLancarExcecaoAoDeletarLivroComExemplaresTest() {
            Integer id = 1;

            Livro livro = new Livro();
            livro.setIdLivro(id);

            Mockito.when(livroRepository.findById(id)).thenReturn(Optional.of(livro));
            Mockito.when(exemplarRepository.existsByLivroIdLivro(id)).thenReturn(true);

            Assertions.assertThrows(RuntimeException.class, () -> livroService.deletar(id));
            Mockito.verify(livroRepository, Mockito.never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("deve testar metodo de listar")
    class listar {

        @Test
        @DisplayName("Deve listar e retornar lista de livros")
        void deveListarERetornarListaDeLivroComSucesso() {
            Livro livro1 = new Livro();
            livro1.setIdLivro(1);
            livro1.setTitulo("Clean Code");

            Livro livro2 = new Livro();
            livro2.setIdLivro(2);
            livro2.setTitulo("The Pragmatic Programmer");

            List<Livro> livros = List.of(livro1, livro2);

            Mockito.when(livroRepository.findAll()).thenReturn(livros);

            List<LivroResponseDto> resultado = livroService.listar();

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals(2, resultado.size());
            Assertions.assertEquals("Clean Code", resultado.get(0).getTitulo());
            Assertions.assertEquals("The Pragmatic Programmer", resultado.get(1).getTitulo());
            Mockito.verify(livroRepository, Mockito.times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver livros")
        void deveRetornarListaVaziaQuandoNaoHouverLivros() {
            Mockito.when(livroRepository.findAll()).thenReturn(Collections.emptyList());

            List<LivroResponseDto> resultado = livroService.listar();

            Assertions.assertNotNull(resultado);
            Assertions.assertTrue(resultado.isEmpty());
            Mockito.verify(livroRepository, Mockito.times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Deve testar metodo de atualizar")
    class atualizar {

        @Test
        @DisplayName("Deve atualizar livro com sucesso")
        void deveAtualizarLivroComSucessoTest() {
            Integer id = 1;

            LivroRequestDto dto = new LivroRequestDto();
            dto.setTitulo("Clean Code Atualizado");
            dto.setIsbn("123456789");
            dto.setAnoPublicacao(2024);
            dto.setIdAutor(1);
            dto.setIdEditora(1);

            Autor autor = new Autor();
            autor.setIdAutor(1);

            Editora editora = new Editora();
            editora.setIdEditora(1);

            Livro livroExistente = new Livro();
            livroExistente.setIdLivro(id);
            livroExistente.setTitulo("Clean Code");

            Livro livroAtualizado = new Livro();
            livroAtualizado.setIdLivro(id);
            livroAtualizado.setTitulo("Clean Code Atualizado");
            livroAtualizado.setIsbn("123456789");
            livroAtualizado.setAnoPublicacao(2024);
            livroAtualizado.setAutor(autor);
            livroAtualizado.setEditora(editora);

            Mockito.when(livroRepository.findById(id)).thenReturn(Optional.of(livroExistente));
            Mockito.when(autorRepository.findById(1)).thenReturn(Optional.of(autor));
            Mockito.when(editoraRepository.findById(1)).thenReturn(Optional.of(editora));
            Mockito.when(livroRepository.save(any())).thenReturn(livroAtualizado);

            LivroResponseDto resultado = livroService.atualizar(id, dto);

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals("Clean Code Atualizado", resultado.getTitulo());
            Assertions.assertEquals("123456789", resultado.getIsbn());
            Mockito.verify(livroRepository, Mockito.times(1)).save(any());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção quando livro não encontrado ao atualizar")
    void deveLancarExcecaoQuandoLivroNaoEncontradoAoAtualizarTest() {
        Integer id = 99;

        LivroRequestDto dto = new LivroRequestDto();
        dto.setIdAutor(1);
        dto.setIdEditora(1);

        Mockito.when(livroRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(LivroNaoEncontradoException.class, () -> livroService.atualizar(id, dto));
        Mockito.verify(livroRepository, Mockito.never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando autor não encontrado ao atualizar")
    void deveLancarExcecaoQuandoAutorNaoEncontradoAoAtualizarTest() {
        Integer id = 1;

        LivroRequestDto dto = new LivroRequestDto();
        dto.setIdAutor(99);
        dto.setIdEditora(1);

        Livro livro = new Livro();
        livro.setIdLivro(id);

        Mockito.when(livroRepository.findById(id)).thenReturn(Optional.of(livro));
        Mockito.when(autorRepository.findById(99)).thenReturn(Optional.empty());

        Assertions.assertThrows(AutorNaoEncontradoException.class, () -> livroService.atualizar(id, dto));
        Mockito.verify(livroRepository, Mockito.never()).save(any());
    }
}

