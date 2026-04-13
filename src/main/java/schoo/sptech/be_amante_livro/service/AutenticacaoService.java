package schoo.sptech.be_amante_livro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.dto.LoginDetalhesDto;
import schoo.sptech.be_amante_livro.model.Login;
import schoo.sptech.be_amante_livro.repository.LoginRepository;

import java.util.Optional;

@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Login> loginOpt = loginRepository.findByUsuario(username);

        if (loginOpt.isEmpty()) {
            throw new UsernameNotFoundException(String.format("usuário: %s não encontrado", username));
        }

        return new LoginDetalhesDto(loginOpt.get());
    }
}