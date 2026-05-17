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
import schoo.sptech.be_amante_livro.mapper.ExemplarMapper;
import schoo.sptech.be_amante_livro.model.Condicao;
import schoo.sptech.be_amante_livro.model.Exemplar;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.CondicaoRepository;
import schoo.sptech.be_amante_livro.repository.ExemplarRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;
import schoo.sptech.be_amante_livro.service.ExemplarService;

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
    }
}