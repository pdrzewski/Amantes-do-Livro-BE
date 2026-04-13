package schoo.sptech.be_amante_livro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import schoo.sptech.be_amante_livro.config.GerenciadorTokenJwt;
import schoo.sptech.be_amante_livro.dto.LoginRequestDto;
import schoo.sptech.be_amante_livro.dto.LoginResponseDto;
import schoo.sptech.be_amante_livro.dto.LoginTokenDto;
import schoo.sptech.be_amante_livro.exception.LoginNaoEncontradoException;
import schoo.sptech.be_amante_livro.mapper.LoginMapper;
import schoo.sptech.be_amante_livro.model.Login;
import schoo.sptech.be_amante_livro.repository.LoginRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticationManager;

    public LoginResponseDto cadastrar(LoginRequestDto dto){
        Login login = LoginMapper.toEntity(dto);
        login.setSenha(passwordEncoder.encode(login.getSenha()));
        Login loginSalvo = loginRepository.save(login);
        return LoginMapper.toResponse(loginSalvo);
    }

    public LoginTokenDto autenticar(LoginRequestDto dto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                dto.getUsuario(), dto.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Login loginAutenticado = loginRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Usuário não cadastrado", null));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = gerenciadorTokenJwt.generateToken(authentication);

        LoginTokenDto tokenDto = new LoginTokenDto();
        tokenDto.setUserId(loginAutenticado.getIdLogin());
        tokenDto.setUsuario(loginAutenticado.getUsuario());
        tokenDto.setToken(token);

        return tokenDto;
    }

    public List<LoginResponseDto> listar(){
        return LoginMapper.toResponsrDtoList(loginRepository.findAll());
    }

    public LoginResponseDto buscarPorId(Integer id){
        Optional<Login> loginOpt = loginRepository.findById(id);
        if (loginOpt.isEmpty()) { throw new LoginNaoEncontradoException(id); }
        return LoginMapper.toResponse(loginOpt.get());
    }
}