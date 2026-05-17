package schoo.sptech.be_amante_livro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.dto.UsuarioDetalhesDto;
import schoo.sptech.be_amante_livro.model.Usuario;
import schoo.sptech.be_amante_livro.repository.UsuarioRepository;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> loginOpt = usuarioRepository.findByUsuario(username);

        if (loginOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuário: %s não encontrado", username));
        }

        return new UsuarioDetalhesDto(loginOpt.get());
    }
}