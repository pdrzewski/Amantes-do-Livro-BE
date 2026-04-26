package schoo.sptech.be_amante_livro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schoo.sptech.be_amante_livro.dto.AutorRequestDto;
import schoo.sptech.be_amante_livro.dto.AutorResponseDto;
import schoo.sptech.be_amante_livro.service.AutorService;

import java.util.List;

@RestController
@RequestMapping("/autores")
@Tag(name = "Autores", description = "Operações de cadastro, consulta, atualização e exclusão de autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @Operation(summary = "Cadastrar autor", description = "Cria um novo autor no sistema. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: nome em branco)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AutorResponseDto> cadastrar(@RequestBody @Valid AutorRequestDto dto) {
        AutorResponseDto resposta = autorService.cadastrar(dto);
        return ResponseEntity.status(201).body(resposta);
    }

    @Operation(summary = "Listar autores", description = "Retorna todos os autores cadastrados. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de autores retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum autor cadastrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AutorResponseDto>> listar() {
        List<AutorResponseDto> lista = autorService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    @Operation(summary = "Buscar autor por ID", description = "Retorna os dados de um autor específico pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDto> buscarPorId(
            @Parameter(description = "ID do autor", example = "1", required = true)
            @PathVariable Integer id) {
        AutorResponseDto dto = autorService.buscarPorId(id);
        return ResponseEntity.status(200).body(dto);
    }

    @Operation(summary = "Atualizar autor", description = "Atualiza os dados de um autor existente pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AutorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: nome em branco)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDto> atualizar(
            @Parameter(description = "ID do autor", example = "1", required = true)
            @PathVariable Integer id,
            @RequestBody @Valid AutorRequestDto dto) {
        AutorResponseDto atualizado = autorService.atualizar(id, dto);
        return ResponseEntity.status(200).body(atualizado);
    }

    @Operation(summary = "Deletar autor", description = "Remove um autor pelo ID. Não é possível excluir autores que possuem livros cadastrados. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Autor deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito - autor possui livros cadastrados e não pode ser excluído", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do autor", example = "1", required = true)
            @PathVariable Integer id) {
        autorService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}

