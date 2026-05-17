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
import schoo.sptech.be_amante_livro.dto.UsuarioCadastroDto;
import schoo.sptech.be_amante_livro.dto.UsuarioLoginRequestDto;
import schoo.sptech.be_amante_livro.dto.UsuarioLoginResponseDto;
import schoo.sptech.be_amante_livro.dto.UsuarioTokenDto;
import schoo.sptech.be_amante_livro.exception.LoginNaoEncontradoException;
import schoo.sptech.be_amante_livro.mapper.UsuarioMapper;
import schoo.sptech.be_amante_livro.model.Usuario;
import schoo.sptech.be_amante_livro.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UsuarioLoginResponseDto cadastrar(UsuarioCadastroDto dto){
        Usuario login = UsuarioMapper.toEntityCadastro(dto);
        login.setSenha(passwordEncoder.encode(login.getSenha()));
        Usuario loginSalvo = usuarioRepository.save(login);
        return UsuarioMapper.toResponse(loginSalvo);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginRequestDto dto) {
        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                dto.getUsuario(), dto.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario loginAutenticado = usuarioRepository.findByUsuario(dto.getUsuario())
                .orElseThrow(() -> new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Usuário não cadastrado", null));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = gerenciadorTokenJwt.generateToken(authentication);

        UsuarioTokenDto tokenDto = new UsuarioTokenDto();
        tokenDto.setUserId(loginAutenticado.getId());
        tokenDto.setUsuario(loginAutenticado.getUsuario());
        tokenDto.setToken(token);

        return tokenDto;
    }

    public List<UsuarioLoginResponseDto> listar(){
        return UsuarioMapper.toResponsrDtoList(usuarioRepository.findAll());
    }

    public UsuarioLoginResponseDto buscarPorId(Integer id){
        Optional<Usuario> loginOpt = usuarioRepository.findById(id);
        if (loginOpt.isEmpty()) { throw new LoginNaoEncontradoException(id); }
        return UsuarioMapper.toResponse(loginOpt.get());
    }
}