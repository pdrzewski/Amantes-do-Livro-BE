package schoo.sptech.be_amante_livro.usuario;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import schoo.sptech.be_amante_livro.config.GerenciadorTokenJwt;
import schoo.sptech.be_amante_livro.dto.UsuarioCadastroDto;
import schoo.sptech.be_amante_livro.dto.UsuarioLoginRequestDto;
import schoo.sptech.be_amante_livro.dto.UsuarioLoginResponseDto;
import schoo.sptech.be_amante_livro.dto.UsuarioTokenDto;
import schoo.sptech.be_amante_livro.exception.LoginNaoEncontradoException;
import schoo.sptech.be_amante_livro.model.Usuario;
import schoo.sptech.be_amante_livro.repository.UsuarioRepository;
import schoo.sptech.be_amante_livro.service.UsuarioService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTests {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private GerenciadorTokenJwt gerenciadorTokenJwt;


    @Test
    @DisplayName("cadastrar: deve salvar e retornar o usuário cadastrado com sucesso")
    void cadastrar_quandoDadosValidos_deveRetornarResponseDto() {
        UsuarioCadastroDto dto = new UsuarioCadastroDto();
        dto.setUsuario("joao");
        dto.setSenha("123456");

        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsuario("joao");
        usuario.setSenha("123456");

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1);
        usuarioSalvo.setUsuario("joao");
        usuarioSalvo.setSenha("senhaCriptografada");

        when(passwordEncoder.encode("123456")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);

        UsuarioLoginResponseDto resultado = usuarioService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("joao", resultado.getUsuario());
        verify(passwordEncoder, Mockito.times(1)).encode("123456");
        verify(usuarioRepository, Mockito.times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("cadastrar: deve lançar exceção quando o repositório falhar ao salvar")
    void cadastrar_quandoRepositorioFalha_deveLancarExcecao() {
        UsuarioCadastroDto dto = new UsuarioCadastroDto();
        dto.setUsuario("joao");
        dto.setSenha("123456");

        when(passwordEncoder.encode(any())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenThrow(new RuntimeException("Erro ao salvar"));

        assertThrows(RuntimeException.class, () -> usuarioService.cadastrar(dto));
    }

    @Test
    @DisplayName("autenticar: deve retornar token quando credenciais são válidas")
    void autenticar_quandoCredenciaisValidas_deveRetornarToken() {
        UsuarioLoginRequestDto dto = new UsuarioLoginRequestDto();
        dto.setUsuario("joao");
        dto.setSenha("123456");

        Authentication authentication = mock(Authentication.class);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsuario("joao");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(usuarioRepository.findByUsuario("joao")).thenReturn(Optional.of(usuario));
        when(gerenciadorTokenJwt.generateToken(authentication)).thenReturn("token.jwt.gerado");

        UsuarioTokenDto resultado = usuarioService.autenticar(dto);

        assertNotNull(resultado);
        assertEquals("joao", resultado.getUsuario());
        assertEquals("token.jwt.gerado", resultado.getToken());
        assertEquals(1, resultado.getUserId());
    }

    @Test
    @DisplayName("autenticar: deve lançar ResponseStatusException quando usuário não for encontrado")
    void autenticar_quandoUsuarioNaoEncontrado_deveLancarResponseStatusException() {
        UsuarioLoginRequestDto dto = new UsuarioLoginRequestDto();
        dto.setUsuario("naoExiste");
        dto.setSenha("123456");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(usuarioRepository.findByUsuario("naoExiste")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> usuarioService.autenticar(dto));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @DisplayName("autenticar: deve lançar exceção quando credenciais são inválidas")
    void autenticar_quandoCredenciaisInvalidas_deveLancarExcecao() {
        UsuarioLoginRequestDto dto = new UsuarioLoginRequestDto();
        dto.setUsuario("joao");
        dto.setSenha("senhaErrada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> usuarioService.autenticar(dto));
    }

    @Test
    @DisplayName("listar: deve retornar lista de usuários quando existirem registros")
    void listar_quandoExistemUsuarios_deveRetornarLista() {
        Usuario u1 = new Usuario(); u1.setId(1); u1.setUsuario("joao");
        Usuario u2 = new Usuario(); u2.setId(2); u2.setUsuario("maria");

        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2));

        List<UsuarioLoginResponseDto> resultado = usuarioService.listar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar: deve retornar lista vazia quando não houver usuários")
    void listar_quandoNaoExistemUsuarios_deveRetornarListaVazia() {
        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

        List<UsuarioLoginResponseDto> resultado = usuarioService.listar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("buscarPorId: deve retornar o usuário quando o id existir")
    void buscarPorId_quandoIdExiste_deveRetornarResponseDto() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setUsuario("joao");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        UsuarioLoginResponseDto resultado = usuarioService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("joao", resultado.getUsuario());
        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("buscarPorId: deve lançar LoginNaoEncontradoException quando o id não existir")
    void buscarPorId_quandoIdNaoExiste_deveLancarLoginNaoEncontradoException() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(LoginNaoEncontradoException.class, () -> usuarioService.buscarPorId(99));
        verify(usuarioRepository, times(1)).findById(99);
    }
}