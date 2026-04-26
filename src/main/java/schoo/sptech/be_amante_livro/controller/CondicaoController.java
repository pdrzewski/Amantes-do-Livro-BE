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
import schoo.sptech.be_amante_livro.dto.CondicaoRequestDto;
import schoo.sptech.be_amante_livro.dto.CondicaoResponseDto;
import schoo.sptech.be_amante_livro.service.CondicaoService;

import java.util.List;

@RestController
@RequestMapping("/condicoes")
@Tag(name = "Condições", description = "Operações de cadastro, consulta, atualização e exclusão de condições de exemplares")
public class CondicaoController {
    private final CondicaoService condicaoService;

    public CondicaoController(CondicaoService condicaoService) {
        this.condicaoService = condicaoService;
    }

    @Operation(summary = "Cadastrar condição", description = "Cria uma nova condição para exemplares (ex: Novo, Usado, Seminovo). Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Condição criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CondicaoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: nome da condição em branco)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CondicaoResponseDto> cadastrar(@RequestBody @Valid CondicaoRequestDto dto) {
        CondicaoResponseDto resposta = condicaoService.cadastrar(dto);
        return ResponseEntity.status(201).body(resposta);
    }

    @Operation(summary = "Listar condições", description = "Retorna todas as condições cadastradas. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de condições retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CondicaoResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "Nenhuma condição cadastrada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CondicaoResponseDto>> listar() {
        List<CondicaoResponseDto> lista = condicaoService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    @Operation(summary = "Buscar condição por ID", description = "Retorna os dados de uma condição específica pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condição encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CondicaoResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Condição não encontrada para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CondicaoResponseDto> buscarPorId(
            @Parameter(description = "ID da condição", example = "1", required = true)
            @PathVariable Integer id) {
        CondicaoResponseDto dto = condicaoService.buscarPorId(id);
        return ResponseEntity.status(200).body(dto);
    }

    @Operation(summary = "Atualizar condição", description = "Atualiza os dados de uma condição existente pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Condição atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CondicaoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: nome da condição em branco)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Condição não encontrada para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CondicaoResponseDto> atualizar(
            @Parameter(description = "ID da condição", example = "1", required = true)
            @PathVariable Integer id,
            @RequestBody @Valid CondicaoRequestDto dto) {
        CondicaoResponseDto atualizado = condicaoService.atualizar(id, dto);
        return ResponseEntity.status(200).body(atualizado);
    }

    @Operation(summary = "Deletar condição", description = "Remove uma condição pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Condição deletada com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Condição não encontrada para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da condição", example = "1", required = true)
            @PathVariable Integer id) {
        condicaoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}

