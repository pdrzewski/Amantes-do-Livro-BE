package schoo.sptech.be_amante_livro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import schoo.sptech.be_amante_livro.dto.AdicionarMassaDto;
import schoo.sptech.be_amante_livro.dto.DeletarMassaDto;
import schoo.sptech.be_amante_livro.dto.ExemplarRequestDto;
import schoo.sptech.be_amante_livro.dto.ExemplarResponseDto;
import schoo.sptech.be_amante_livro.service.AdicionarMassaService;
import schoo.sptech.be_amante_livro.service.ExemplarService;

import java.util.List;

@RestController
@RequestMapping("/exemplares")
@Tag(name = "Exemplares", description = "Operações de cadastro, consulta, atualização, exclusão, baixa de estoque e importação/em massa de exemplares")
public class ExemplarController {

    private final ExemplarService service;
    private final AdicionarMassaService adicionarMassaService;

    public ExemplarController(ExemplarService service, AdicionarMassaService adicionarMassaService) {
        this.service = service;
        this.adicionarMassaService = adicionarMassaService;
    }

    @Operation(summary = "Cadastrar exemplar", description = "Cria um novo exemplar vinculado a um livro e condição. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Exemplar criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou IDs de livro/condição inexistentes", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livro ou condição não encontrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ExemplarResponseDto> cadastrar(@Valid @RequestBody ExemplarRequestDto dto) {
        return ResponseEntity.status(201).body(service.cadastrar(dto));
    }

    @Operation(summary = "Listar exemplares", description = "Retorna todos os exemplares cadastrados. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de exemplares retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum exemplar cadastrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ExemplarResponseDto>> listar() {
        List<ExemplarResponseDto> lista = service.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Buscar exemplar por ID", description = "Retorna os dados de um exemplar específico pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exemplar encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ExemplarResponseDto> buscar(
            @Parameter(description = "ID do exemplar", example = "1", required = true)
            @PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Atualizar exemplar", description = "Atualiza os dados de um exemplar existente pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exemplar atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou IDs de livro/condição inexistentes", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exemplar, livro ou condição não encontrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ExemplarResponseDto> atualizar(
            @Parameter(description = "ID do exemplar", example = "1", required = true)
            @PathVariable Integer id,
            @Valid @RequestBody ExemplarRequestDto dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Deletar exemplar", description = "Remove um exemplar pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Exemplar deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do exemplar", example = "1", required = true)
            @PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Baixar estoque", description = "Reduz a quantidade em estoque de um exemplar. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Estoque baixado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Quantidade inválida (menor ou igual a zero) ou estoque insuficiente", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PatchMapping("/{id}/baixa/{qtd}")
    public ResponseEntity<Void> baixarEstoque(
            @Parameter(description = "ID do exemplar", example = "1", required = true)
            @PathVariable Integer id,
            @Parameter(description = "Quantidade a ser baixada do estoque", example = "5", required = true)
            @PathVariable Integer qtd) {
        service.baixarEstoque(id, qtd);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Importar CSV", description = "Importa exemplares em massa via arquivo CSV. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "207", description = "Importação parcial - alguns registros foram inseridos, outros falharam",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdicionarMassaDto.class))),
            @ApiResponse(responseCode = "400", description = "Nenhum registro inserido com sucesso ou arquivo inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AdicionarMassaDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping(value = "/importar-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdicionarMassaDto> importarCsv(
            @Parameter(description = "Arquivo CSV para importação", required = true)
            @RequestPart("arquivo") MultipartFile arquivo) {
        AdicionarMassaDto resultado = adicionarMassaService.importarEstanteVirtual(arquivo);

        if (resultado.getSucesso() == 0) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.status(207).body(resultado);
    }

    @Operation(summary = "Deletar em massa via CSV", description = "Deleta exemplares em massa com base em um arquivo CSV. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleção em massa realizada (pode conter erros parciais)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeletarMassaDto.class))),
            @ApiResponse(responseCode = "400", description = "Nenhum registro deletado com sucesso ou arquivo inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeletarMassaDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping(value = "/deletar-massa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DeletarMassaDto> deletarMassa(
            @Parameter(description = "Arquivo CSV com os IDs dos exemplares a serem deletados", required = true)
            @RequestPart("arquivo") MultipartFile arquivo) {
        DeletarMassaDto resultado = service.deletarEmMassa(arquivo);

        if (resultado.sucesso() == 0) {
            return ResponseEntity.badRequest().body(resultado);
        }

        return ResponseEntity.ok(resultado);
    }
}

