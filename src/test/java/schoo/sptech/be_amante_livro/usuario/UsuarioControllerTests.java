package schoo.sptech.be_amante_livro.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import schoo.sptech.be_amante_livro.controller.UsuarioController;
import schoo.sptech.be_amante_livro.dto.UsuarioLoginResponseDto;
import schoo.sptech.be_amante_livro.dto.UsuarioTokenDto;
import schoo.sptech.be_amante_livro.service.UsuarioService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UsuarioController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class UsuarioControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void cadastrar_deveRetornar201QuandoDadosValidos() throws Exception {
        UsuarioLoginResponseDto resposta = new UsuarioLoginResponseDto();
        resposta.setUsuario("joao");

        when(usuarioService.cadastrar(any())).thenReturn(resposta);

        mockMvc.perform(post("/login/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"joao\",\"senha\":\"123456\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.usuario").value("joao"));
    }

    @Test
    void cadastrar_deveRetornar400QuandoDadosInvalidos() throws Exception {
        when(usuarioService.cadastrar(any())).thenThrow(new IllegalArgumentException("Usuário ou senha em branco"));

        mockMvc.perform(post("/login/cadastrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"\",\"senha\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void entrar_deveRetornar200EDefinirCookieQuandoCredenciaisValidas() throws Exception {
        UsuarioTokenDto token = new UsuarioTokenDto();
        token.setUserId(1);
        token.setUsuario("joao");
        token.setToken("jwt-token-mock");

        when(usuarioService.autenticar(any())).thenReturn(token);

        mockMvc.perform(post("/login/entrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"joao\",\"senha\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario").value("joao"))
                .andExpect(header().exists("Set-Cookie"));
    }

    @Test
    void entrar_deveRetornar401QuandoCredenciaisInvalidas() throws Exception {
        when(usuarioService.autenticar(any())).thenThrow(new UsernameNotFoundException("Usuário ou senha incorretos"));

        mockMvc.perform(post("/login/entrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":\"joao\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void sair_deveRetornar204EZerarCookie() throws Exception {
        mockMvc.perform(post("/login/sair"))
                .andExpect(status().isNoContent())
                .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("authToken=")));
    }

    @Test
    void listar_deveRetornar200ComListaDeUsuarios() throws Exception {
        UsuarioLoginResponseDto usuario = new UsuarioLoginResponseDto();
        usuario.setUsuario("joao");

        when(usuarioService.listar()).thenReturn(List.of(usuario));

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuario").value("joao"));
    }

    @Test
    void listar_deveRetornar200ComListaVaziaQuandoNaoHouverUsuarios() throws Exception {
        when(usuarioService.listar()).thenReturn(List.of());

        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void buscarPorId_deveRetornar200QuandoUsuarioEncontrado() throws Exception {
        UsuarioLoginResponseDto usuario = new UsuarioLoginResponseDto();
        usuario.setUsuario("joao");

        when(usuarioService.buscarPorId(1)).thenReturn(usuario);

        mockMvc.perform(get("/login/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario").value("joao"));
    }

    @Test
    void buscarPorId_deveRetornar404QuandoUsuarioNaoEncontrado() throws Exception {
        when(usuarioService.buscarPorId(99)).thenThrow(new jakarta.persistence.EntityNotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/login/99"))
                .andExpect(status().isNotFound());
    }
}
