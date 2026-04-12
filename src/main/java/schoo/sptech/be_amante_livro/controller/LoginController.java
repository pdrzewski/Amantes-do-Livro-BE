package schoo.sptech.be_amante_livro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import schoo.sptech.be_amante_livro.dto.LoginRequestDto;
import schoo.sptech.be_amante_livro.dto.LoginResponseDto;
import schoo.sptech.be_amante_livro.service.LoginService;

import java.util.List;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponseDto cadastrar(@RequestBody LoginRequestDto dto) {
        return loginService.cadastrar(dto);
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