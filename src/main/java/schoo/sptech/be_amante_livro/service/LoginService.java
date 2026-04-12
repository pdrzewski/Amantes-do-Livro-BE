package schoo.sptech.be_amante_livro.service;

import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.dto.LoginRequestDto;
import schoo.sptech.be_amante_livro.dto.LoginResponseDto;
import schoo.sptech.be_amante_livro.exception.LivroNaoEncontradoException;
import schoo.sptech.be_amante_livro.exception.LoginNaoEncontradoException;
import schoo.sptech.be_amante_livro.mapper.LivroMapper;
import schoo.sptech.be_amante_livro.mapper.LoginMapper;
import schoo.sptech.be_amante_livro.model.Login;
import schoo.sptech.be_amante_livro.repository.LoginRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LoginService {
    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LoginResponseDto cadastrar(LoginRequestDto dto){
        Login login = LoginMapper.toEntity(dto);
        Login loginSalvo = loginRepository.save(login);

        return LoginMapper.toResponse(loginSalvo);
    }

    public List<LoginResponseDto> listar(){
        List<Login> logins = loginRepository.findAll();

        return LoginMapper.toResponsrDtoList(logins);
    }

    public LoginResponseDto buscarPorId(Integer id){
        Optional<Login> loginOpt = loginRepository.findById(id);

        if (loginOpt.isEmpty()) {
            throw new LoginNaoEncontradoException(id);
        }

        return LoginMapper.toResponse(loginOpt.get());
    }
}
