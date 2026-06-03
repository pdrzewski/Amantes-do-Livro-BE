package schoo.sptech.be_amante_livro.condicao;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import schoo.sptech.be_amante_livro.controller.CondicaoController;
import schoo.sptech.be_amante_livro.dto.CondicaoResponseDto;
import schoo.sptech.be_amante_livro.service.CondicaoService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = CondicaoController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CondicaoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CondicaoService condicaoService;

    @Test
    void cadastrar_deveRetornar201QuandoDadosValidos() throws Exception {
        CondicaoResponseDto resposta = new CondicaoResponseDto();
        resposta.setNomeCondicao("Novo");

        when(condicaoService.cadastrar(any())).thenReturn(resposta);

        mockMvc.perform(post("/condicoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeCondicao\":\"Novo\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nomeCondicao").value("Novo"));
    }

    @Test
    void cadastrar_deveRetornar400QuandoNomeEmBranco() throws Exception {
        when(condicaoService.cadastrar(any())).thenThrow(new IllegalArgumentException("Nome da condição não pode ser em branco"));

        mockMvc.perform(post("/condicoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeCondicao\":\"\"}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void listar_deveRetornar200ComListaDeCondicoes() throws Exception {
        CondicaoResponseDto condicao = new CondicaoResponseDto();
        condicao.setNomeCondicao("Novo");

        when(condicaoService.listar()).thenReturn(List.of(condicao));

        mockMvc.perform(get("/condicoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCondicao").value("Novo"));
    }

    @Test
    void listar_deveRetornar204QuandoListaVazia() throws Exception {
        when(condicaoService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/condicoes"))
                .andExpect(status().isNoContent());
    }



    @Test
    void buscarPorId_deveRetornar200QuandoCondicaoEncontrada() throws Exception {
        CondicaoResponseDto condicao = new CondicaoResponseDto();
        condicao.setNomeCondicao("Seminovo");

        when(condicaoService.buscarPorId(1)).thenReturn(condicao);

        mockMvc.perform(get("/condicoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCondicao").value("Seminovo"));
    }

    @Test
    void buscarPorId_deveRetornar404QuandoCondicaoNaoEncontrada() throws Exception {
        when(condicaoService.buscarPorId(99)).thenThrow(new EntityNotFoundException("Condição não encontrada"));

        mockMvc.perform(get("/condicoes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizar_deveRetornar200QuandoDadosValidos() throws Exception {
        CondicaoResponseDto atualizado = new CondicaoResponseDto();
        atualizado.setNomeCondicao("Usado");

        when(condicaoService.atualizar(eq(1), any())).thenReturn(atualizado);

        mockMvc.perform(put("/condicoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeCondicao\":\"Usado\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeCondicao").value("Usado"));
    }

    @Test
    void atualizar_deveRetornar404QuandoCondicaoNaoEncontrada() throws Exception {
        when(condicaoService.atualizar(eq(99), any())).thenThrow(new EntityNotFoundException("Condição não encontrada"));

        mockMvc.perform(put("/condicoes/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nomeCondicao\":\"Usado\"}"))
                .andExpect(status().isNotFound());
    }


    @Test
    void deletar_deveRetornar204QuandoCondicaoDeletada() throws Exception {
        doNothing().when(condicaoService).deletar(1);

        mockMvc.perform(delete("/condicoes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_deveRetornar404QuandoCondicaoNaoEncontrada() throws Exception {
        doThrow(new EntityNotFoundException("Condição não encontrada")).when(condicaoService).deletar(99);

        mockMvc.perform(delete("/condicoes/99"))
                .andExpect(status().isNotFound());
    }
}