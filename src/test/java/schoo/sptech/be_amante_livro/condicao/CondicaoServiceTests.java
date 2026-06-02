package schoo.sptech.be_amante_livro.condicao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import schoo.sptech.be_amante_livro.dto.CondicaoRequestDto;
import schoo.sptech.be_amante_livro.dto.CondicaoResponseDto;
import schoo.sptech.be_amante_livro.exception.CondicaoNaoEncontradaException;
import schoo.sptech.be_amante_livro.model.Condicao;
import schoo.sptech.be_amante_livro.repository.CondicaoRepository;
import schoo.sptech.be_amante_livro.service.CondicaoService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CondicaoServiceTests {

    @InjectMocks
    private CondicaoService condicaoService;

    @Mock
    private CondicaoRepository repository;

    @Test
    @DisplayName("listar: deve retornar lista de condições quando existirem registros")
    void listar_quandoExistemCondicoes_deveRetornarLista() {
        Condicao c1 = new Condicao(); c1.setIdCondicao(1); c1.setNomeCondicao("Diabetes");
        Condicao c2 = new Condicao(); c2.setIdCondicao(2); c2.setNomeCondicao("Hipertensão");

        when(repository.findAll()).thenReturn(List.of(c1, c2));

        List<CondicaoResponseDto> resultado = condicaoService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("listar: deve retornar lista vazia quando não houver condições")
    void listar_quandoNaoExistemCondicoes_deveRetornarListaVazia() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<CondicaoResponseDto> resultado = condicaoService.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("cadastrar: deve salvar e retornar a condição cadastrada com sucesso")
    void cadastrar_quandoDadosValidos_deveRetornarResponseDto() {
        CondicaoRequestDto dto = new CondicaoRequestDto();
        dto.setNomeCondicao("Diabetes");

        Condicao condicaoSalva = new Condicao();
        condicaoSalva.setIdCondicao(1);
        condicaoSalva.setNomeCondicao("Diabetes");

        when(repository.save(any(Condicao.class))).thenReturn(condicaoSalva);

        CondicaoResponseDto resultado = condicaoService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("Diabetes", resultado.getNomeCondicao());
        verify(repository, times(1)).save(any(Condicao.class));
    }

    @Test
    @DisplayName("cadastrar: deve lançar exceção quando o repositório falhar ao salvar")
    void cadastrar_quandoRepositorioFalha_deveLancarExcecao() {
        CondicaoRequestDto dto = new CondicaoRequestDto();
        dto.setNomeCondicao("Diabetes");

        when(repository.save(any(Condicao.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        assertThrows(RuntimeException.class, () -> condicaoService.cadastrar(dto));
        verify(repository, times(1)).save(any(Condicao.class));
    }

    @Test
    @DisplayName("atualizar: deve atualizar e retornar a condição quando o id existir")
    void atualizar_quandoIdExiste_deveRetornarResponseDtoAtualizado() {
        CondicaoRequestDto dto = new CondicaoRequestDto();
        dto.setNomeCondicao("Hipertensão");

        Condicao condicaoExistente = new Condicao();
        condicaoExistente.setIdCondicao(1);
        condicaoExistente.setNomeCondicao("Diabetes");

        Condicao condicaoAtualizada = new Condicao();
        condicaoAtualizada.setIdCondicao(1);
        condicaoAtualizada.setNomeCondicao("Hipertensão");

        when(repository.findById(1)).thenReturn(Optional.of(condicaoExistente));
        when(repository.save(any(Condicao.class))).thenReturn(condicaoAtualizada);

        CondicaoResponseDto resultado = condicaoService.atualizar(1, dto);

        assertNotNull(resultado);
        assertEquals("Hipertensão", resultado.getNomeCondicao());
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Condicao.class));
    }

    @Test
    @DisplayName("atualizar: deve lançar CondicaoNaoEncontradaException quando o id não existir")
    void atualizar_quandoIdNaoExiste_deveLancarCondicaoNaoEncontradaException() {
        CondicaoRequestDto dto = new CondicaoRequestDto();
        dto.setNomeCondicao("Hipertensão");

        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CondicaoNaoEncontradaException.class, () -> condicaoService.atualizar(99, dto));
        verify(repository, times(1)).findById(99);
        verify(repository, never()).save(any(Condicao.class));
    }

    @Test
    @DisplayName("deletar: deve deletar a condição quando o id existir")
    void deletar_quandoIdExiste_deveDeletarSemExcecao() {
        Condicao condicao = new Condicao();
        condicao.setIdCondicao(1);
        condicao.setNomeCondicao("Diabetes");

        when(repository.findById(1)).thenReturn(Optional.of(condicao));
        doNothing().when(repository).deleteById(1);

        assertDoesNotThrow(() -> condicaoService.deletar(1));
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("deletar: deve lançar CondicaoNaoEncontradaException quando o id não existir")
    void deletar_quandoIdNaoExiste_deveLancarCondicaoNaoEncontradaException() {
        when(repository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CondicaoNaoEncontradaException.class, () -> condicaoService.deletar(99));
        verify(repository, times(1)).findById(99);
        verify(repository, never()).deleteById(any());
    }
}