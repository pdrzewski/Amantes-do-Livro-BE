package schoo.sptech.be_amante_livro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schoo.sptech.be_amante_livro.dto.*;
import schoo.sptech.be_amante_livro.service.UsuarioService;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Operações de cadastro de usuários, login, logout e consulta de usuários")
public class UsuarioController {

    public static final String COOKIE_NOME = "authToken";

    @Value("${jwt.validity}")
    private long jwtValidity;

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário no sistema. Não requer autenticação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioLoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: usuário ou senha em branco)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioLoginResponseDto cadastrar(@RequestBody UsuarioCadastroDto dto) {
        return usuarioService.cadastrar(dto);
    }

    @Operation(summary = "Entrar (login)", description = "Autentica o usuário e retorna um cookie JWT (authToken). Não requer autenticação prévia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso. O cookie 'authToken' é definido automaticamente.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioSessaoDto.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou dados incompletos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Usuário ou senha incorretos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não cadastrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping("/entrar")
    public ResponseEntity<UsuarioSessaoDto> entrar(@RequestBody UsuarioLoginRequestDto dto, HttpServletResponse response) {
        UsuarioTokenDto autenticado = usuarioService.autenticar(dto);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, autenticado.getToken())
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofSeconds(jwtValidity))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        UsuarioSessaoDto sessao = new UsuarioSessaoDto();
        sessao.setUserId(autenticado.getUserId());
        sessao.setUsuario(autenticado.getUsuario());

        return ResponseEntity.ok(sessao);
    }

    @Operation(summary = "Sair (logout)", description = "Remove o cookie JWT, encerrando a sessão do usuário. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logout realizado com sucesso - cookie removido", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping("/sair")
    public ResponseEntity<Void> sair(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(COOKIE_NOME, "")
                .httpOnly(true).secure(false).sameSite("Strict").path("/").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar usuários", description = "Retorna todos os usuários cadastrados. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioLoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public List<UsuarioLoginResponseDto> listar() {
        return usuarioService.listar();
    }

    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário específico pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioLoginResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public UsuarioLoginResponseDto buscarPorId(
            @Parameter(description = "ID do usuário", example = "1", required = true)
            @PathVariable Integer id) {
        return usuarioService.buscarPorId(id);
    }
}

