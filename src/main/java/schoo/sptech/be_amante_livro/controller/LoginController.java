package schoo.sptech.be_amante_livro.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schoo.sptech.be_amante_livro.dto.LoginRequestDto;
import schoo.sptech.be_amante_livro.dto.LoginResponseDto;
import schoo.sptech.be_amante_livro.dto.LoginSessaoDto;
import schoo.sptech.be_amante_livro.dto.LoginTokenDto;
import schoo.sptech.be_amante_livro.service.LoginService;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    public static final String COOKIE_NOME = "authToken";

    @Value("${jwt.validity}")
    private long jwtValidity;

    @Autowired
    private LoginService loginService;

    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDto cadastrar(@RequestBody LoginRequestDto dto) {
        return loginService.cadastrar(dto);
    }

    @PostMapping("/entrar")
    public ResponseEntity<LoginSessaoDto> entrar(@RequestBody LoginRequestDto dto, HttpServletResponse response) {
        LoginTokenDto autenticado = loginService.autenticar(dto);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, autenticado.getToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofSeconds(jwtValidity))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        LoginSessaoDto sessao = new LoginSessaoDto();
        sessao.setUserId(autenticado.getUserId());
        sessao.setUsuario(autenticado.getUsuario());

        return ResponseEntity.ok(sessao);
    }

    @PostMapping("/sair")
    public ResponseEntity<Void> sair(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, "")
                .httpOnly(true).secure(false).sameSite("Strict").path("/").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<LoginResponseDto> listar() {
        return loginService.listar();
    }

    @GetMapping("/{id}")
    public LoginResponseDto buscarPorId(@PathVariable Integer id) {
        return loginService.buscarPorId(id);
    }
}