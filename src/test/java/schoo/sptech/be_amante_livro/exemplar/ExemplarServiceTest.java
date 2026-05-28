package schoo.sptech.be_amante_livro.exemplar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import schoo.sptech.be_amante_livro.dto.ExemplarRequestDto;
import schoo.sptech.be_amante_livro.dto.ExemplarResponseDto;
import schoo.sptech.be_amante_livro.exception.ExemplarNaoEncontradoException;
import schoo.sptech.be_amante_livro.mapper.ExemplarMapper;
import schoo.sptech.be_amante_livro.model.Condicao;
import schoo.sptech.be_amante_livro.model.Exemplar;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.CondicaoRepository;
import schoo.sptech.be_amante_livro.repository.ExemplarRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;
import schoo.sptech.be_amante_livro.service.ExemplarService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ExemplarServiceTest {

    @Mock
    private ExemplarRepository exemplarRepository;

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private CondicaoRepository condicaoRepository;

    @InjectMocks
    private ExemplarService exemplarService;

    @Nested
    @DisplayName("Testes para o método cadastrar para exemplares")
    class cadastrarExemplar {

        @Test
        @DisplayName("deveRetornarResponseDtoAoCadastrar")
        void deveCadastrarExemplarComSucesso() {

            ExemplarRequestDto request = new ExemplarRequestDto();
            request.setIdLivro(1);
            request.setIdCondicao(2);
            request.setPreco(39.90);
            request.setQuantidade(5);

            Livro livro = new Livro();
            livro.setIdLivro(1);
            livro.setTitulo("Livro de Teste");

            Condicao condicao = new Condicao();
            condicao.setIdCondicao(2);
            condicao.setNomeCondicao("Novo");

            Exemplar exemplarEntity = ExemplarMapper.toEntity(request, livro, condicao);
            exemplarEntity.setIdExemplar(123);

            Mockito.when(livroRepository.findById(1)).thenReturn(Optional.of(livro));
            Mockito.when(condicaoRepository.findById(2)).thenReturn(Optional.of(condicao));
            Mockito.when(exemplarRepository.save(Mockito.any(Exemplar.class))).thenAnswer(invocation -> {
                Exemplar arg = invocation.getArgument(0);
                arg.setIdExemplar(123);
                return arg;
            });


            ExemplarResponseDto resultado = exemplarService.cadastrar(request);

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals(123, resultado.getIdExemplar());
            Assertions.assertEquals(request.getQuantidade(), resultado.getQuantidade());
            Assertions.assertEquals(request.getPreco(), resultado.getPreco());
            Assertions.assertNotNull(resultado.getLivro());
            Assertions.assertEquals(livro.getTitulo(), resultado.getLivro().getTitulo());

            Mockito.verify(exemplarRepository, Mockito.times(1)).save(Mockito.any(Exemplar.class));
        }

        @Test
        @DisplayName("deveLancarExcecaoQuandoLivroNaoEncontrado")
        void deveLancarExcecaoQuandoLivroNaoEncontrado() {

            ExemplarRequestDto request = new ExemplarRequestDto();
            request.setIdLivro(99);
            request.setIdCondicao(2);
            request.setPreco(39.90);
            request.setQuantidade(5);

            Mockito.when(livroRepository.findById(99)).thenReturn(Optional.empty());

            Assertions.assertThrows(RuntimeException.class, () -> {
                exemplarService.cadastrar(request);
            });

            Mockito.verify(exemplarRepository, Mockito.never()).save(Mockito.any(Exemplar.class));
        }

        @Test
        @DisplayName("deveLancarExcecaoQuandoCondicaoNaoEncontrada")
        void deveLancarExcecaoQuandoCondicaoNaoEncontrada() {

            ExemplarRequestDto request = new ExemplarRequestDto();
            request.setIdLivro(1);
            request.setIdCondicao(99);
            request.setPreco(39.90);
            request.setQuantidade(5);

            Livro livro = new Livro();
            livro.setIdLivro(1);
            livro.setTitulo("Livro de Teste");

            Mockito.when(livroRepository.findById(1)).thenReturn(Optional.of(livro));
            Mockito.when(condicaoRepository.findById(99)).thenReturn(Optional.empty());

            Assertions.assertThrows(RuntimeException.class, () -> {
                exemplarService.cadastrar(request);
            });

            Mockito.verify(exemplarRepository, Mockito.never()).save(Mockito.any(Exemplar.class));
        }
    }

    @Nested
    @DisplayName("Deve testar método de buscar")
    class buscar {

        @Test
        @DisplayName("Deve testar o método buscar por id com sucesso")
        void deveBuscarExemplarPorIdComSucessoTest () {
            Integer id = 1;

            Exemplar exemplar = new Exemplar();
            exemplar.setIdExemplar(id);

            Mockito.when(exemplarRepository.findById(id)).thenReturn(Optional.of(exemplar));

            ExemplarResponseDto resultado = exemplarService.buscarPorId(id);

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals(id, resultado.getIdExemplar());
            Mockito.verify(exemplarRepository, Mockito.times(1)).findById(id);
        }

        @Test
        @DisplayName("Deve lançar exceção quando exemplar não encontrado pelo ID")
        void deveLancarExcecaoQuandoExemplarNaoEncontradoPorIdTest() {
            Integer id = 99;

            Mockito.when(exemplarRepository.findById(id)).thenReturn(Optional.empty());

            Assertions.assertThrows(ExemplarNaoEncontradoException.class, () -> exemplarService.buscarPorId(id));
            Mockito.verify(exemplarRepository, Mockito.times(1)).findById(id);
        }
    }

    @Nested
    @DisplayName("Deve testar método de listar")
    class listar {

        @Test
        @DisplayName("Deve testar o método listar com sucesso")
        void deveListarExemplaresComSucessoTest() {
            Exemplar exemplar1 = new Exemplar();
            exemplar1.setIdExemplar(1);

            Exemplar exemplar2 = new Exemplar();
            exemplar2.setIdExemplar(2);

            List<Exemplar> exemplares = List.of(exemplar1, exemplar2);

            Mockito.when(exemplarRepository.findAll()).thenReturn(exemplares);

            List<ExemplarResponseDto> resultado = exemplarService.listar();

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals(2, resultado.size());
            Assertions.assertEquals(1, resultado.get(0).getIdExemplar());
            Assertions.assertEquals(2, resultado.get(1).getIdExemplar());
            Mockito.verify(exemplarRepository, Mockito.times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver exemplares")
        void deveRetornarListaVaziaQuandoNaoHouverExemplaresTest() {
            Mockito.when(exemplarRepository.findAll()).thenReturn(Collections.emptyList());

            List<ExemplarResponseDto> resultado = exemplarService.listar();

            Assertions.assertNotNull(resultado);
            Assertions.assertTrue(resultado.isEmpty());
            Mockito.verify(exemplarRepository, Mockito.times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Deve testar método de deletar")
    class deletar {

        @Test
        @DisplayName("Deve testar o método deletar com sucesso")
        void deveDeletarExemplarComSucessoTest() {
            Integer id = 1;

            Exemplar exemplar = new Exemplar();
            exemplar.setIdExemplar(id);

            Mockito.when(exemplarRepository.findById(id)).thenReturn(Optional.of(exemplar));

            exemplarService.deletar(id);

            Mockito.verify(exemplarRepository, Mockito.times(1)).deleteById(id);
        }

        @Test
        @DisplayName("Deve lançar exceção quando exemplar não encontrado ao deletar")
        void deveLancarExcecaoQuandoExemplarNaoEncontradoAoDeletarTest() {
            Integer id = 99;

            Mockito.when(exemplarRepository.findById(id)).thenReturn(Optional.empty());

            Assertions.assertThrows(ExemplarNaoEncontradoException.class, () -> exemplarService.deletar(id));
            Mockito.verify(exemplarRepository, Mockito.never()).deleteById(Mockito.any());
        }
    }
}