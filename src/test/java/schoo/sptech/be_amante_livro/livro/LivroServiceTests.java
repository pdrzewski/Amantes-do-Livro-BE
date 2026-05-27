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
import schoo.sptech.be_amante_livro.exception.LivroNaoEncontradoException;
import schoo.sptech.be_amante_livro.model.Autor;
import schoo.sptech.be_amante_livro.model.Editora;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.AutorRepository;
import schoo.sptech.be_amante_livro.repository.EditoraRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;
import schoo.sptech.be_amante_livro.service.LivroService;

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
}
