package schoo.sptech.be_amante_livro.autor;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import schoo.sptech.be_amante_livro.controller.AutorController;
import schoo.sptech.be_amante_livro.dto.AutorResponseDto;
import schoo.sptech.be_amante_livro.service.AutorService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AutorController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class AutorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutorService autorService;

    // -------------------------------------------------------------------------
    // POST /autores
    // -------------------------------------------------------------------------

    @Test
    void cadastrar_deveRetornar201QuandoDadosValidos() throws Exception {
        AutorResponseDto resposta = new AutorResponseDto();
        resposta.setNome("Robert Martin");

        when(autorService.cadastrar(any())).thenReturn(resposta);

        mockMvc.perform(post("/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Robert Martin\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Robert Martin"));
    }

    @Test
    void cadastrar_deveRetornar400QuandoNomeEmBranco() throws Exception {
        when(autorService.cadastrar(any())).thenThrow(new IllegalArgumentException("Nome não pode ser em branco"));

        mockMvc.perform(post("/autores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // GET /autores
    // -------------------------------------------------------------------------

    @Test
    void listar_deveRetornar200ComListaDeAutores() throws Exception {
        AutorResponseDto autor = new AutorResponseDto();
        autor.setNome("Robert Martin");

        when(autorService.listar()).thenReturn(List.of(autor));

        mockMvc.perform(get("/autores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Robert Martin"));
    }

    @Test
    void listar_deveRetornar204QuandoListaVazia() throws Exception {
        when(autorService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/autores"))
                .andExpect(status().isNoContent());
    }

    // -------------------------------------------------------------------------
    // GET /autores/{id}
    // -------------------------------------------------------------------------

    @Test
    void buscarPorId_deveRetornar200QuandoAutorEncontrado() throws Exception {
        AutorResponseDto autor = new AutorResponseDto();
        autor.setNome("Robert Martin");

        when(autorService.buscarPorId(1)).thenReturn(autor);

        mockMvc.perform(get("/autores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Robert Martin"));
    }

    @Test
    void buscarPorId_deveRetornar404QuandoAutorNaoEncontrado() throws Exception {
        when(autorService.buscarPorId(99)).thenThrow(new EntityNotFoundException("Autor não encontrado"));

        mockMvc.perform(get("/autores/99"))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // PUT /autores/{id}
    // -------------------------------------------------------------------------

    @Test
    void atualizar_deveRetornar200QuandoDadosValidos() throws Exception {
        AutorResponseDto atualizado = new AutorResponseDto();
        atualizado.setNome("Clean Coder");

        when(autorService.atualizar(eq(1), any())).thenReturn(atualizado);

        mockMvc.perform(put("/autores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Clean Coder\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Clean Coder"));
    }

    @Test
    void atualizar_deveRetornar404QuandoAutorNaoEncontrado() throws Exception {
        when(autorService.atualizar(eq(99), any())).thenThrow(new EntityNotFoundException("Autor não encontrado"));

        mockMvc.perform(put("/autores/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Clean Coder\"}"))
                .andExpect(status().isNotFound());
    }

    // -------------------------------------------------------------------------
    // DELETE /autores/{id}
    // -------------------------------------------------------------------------

    @Test
    void deletar_deveRetornar204QuandoAutorDeletado() throws Exception {
        doNothing().when(autorService).deletar(1);

        mockMvc.perform(delete("/autores/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deletar_deveRetornar404QuandoAutorNaoEncontrado() throws Exception {
        doThrow(new EntityNotFoundException("Autor não encontrado")).when(autorService).deletar(99);

        mockMvc.perform(delete("/autores/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletar_deveRetornar409QuandoAutorPossuiLivros() throws Exception {
        doThrow(new DataIntegrityViolationException("Autor possui livros cadastrados")).when(autorService).deletar(1);

        mockMvc.perform(delete("/autores/1"))
                .andExpect(status().isConflict());
    }
}