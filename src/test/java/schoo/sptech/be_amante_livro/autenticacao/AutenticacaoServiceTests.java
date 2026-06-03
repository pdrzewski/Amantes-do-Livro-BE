package schoo.sptech.be_amante_livro.autenticacao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import schoo.sptech.be_amante_livro.dto.UsuarioDetalhesDto;
import schoo.sptech.be_amante_livro.model.Usuario;
import schoo.sptech.be_amante_livro.repository.UsuarioRepository;
import schoo.sptech.be_amante_livro.service.AutenticacaoService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoServiceTests {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AutenticacaoService service;

    @Test
    void loadUserByUsername_deveRetornarUserDetailsQuandoUsuarioExistir() {
        Usuario usuario = new Usuario();
        usuario.setUsuario("joao");

        when(usuarioRepository.findByUsuario("joao")).thenReturn(Optional.of(usuario));

        UserDetails resultado = service.loadUserByUsername("joao");

        assertThat(resultado).isNotNull().isInstanceOf(UsuarioDetalhesDto.class);
    }

    @Test
    void loadUserByUsername_deveLancarUsernameNotFoundExceptionQuandoUsuarioNaoExistir() {
        when(usuarioRepository.findByUsuario("inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("inexistente"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("inexistente");
    }
}

