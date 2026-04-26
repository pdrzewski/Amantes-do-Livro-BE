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
import org.springframework.web.client.RestTemplate;
import schoo.sptech.be_amante_livro.dto.LivroRequestDto;
import schoo.sptech.be_amante_livro.dto.LivroResponseDto;
import schoo.sptech.be_amante_livro.service.LivroService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Operações de cadastro, consulta, atualização, exclusão de livros e integração com Google Books")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @Operation(summary = "Cadastrar livro", description = "Cria um novo livro vinculado a um autor e editora. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: título em branco, IDs negativos)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Autor ou editora não encontrados para os IDs informados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PostMapping
    public ResponseEntity<LivroResponseDto> cadastrar(@RequestBody @Valid LivroRequestDto dto) {
        LivroResponseDto resposta = livroService.cadastrar(dto);
        return ResponseEntity.status(201).body(resposta);
    }

    @Operation(summary = "Listar livros", description = "Retorna todos os livros cadastrados com dados do autor e editora. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDto.class))),
            @ApiResponse(responseCode = "204", description = "Nenhum livro cadastrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<LivroResponseDto>> listar() {
        List<LivroResponseDto> lista = livroService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    @Operation(summary = "Buscar livro por ID", description = "Retorna os dados completos de um livro específico pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDto> buscarPorId(
            @Parameter(description = "ID do livro", example = "1", required = true)
            @PathVariable Integer id) {
        LivroResponseDto dto = livroService.buscarPorId(id);
        return ResponseEntity.status(200).body(dto);
    }

    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente pelo ID. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: título em branco, IDs negativos)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livro, autor ou editora não encontrados", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDto> atualizar(
            @Parameter(description = "ID do livro", example = "1", required = true)
            @PathVariable Integer id,
            @RequestBody @Valid LivroRequestDto dto) {
        LivroResponseDto atualizado = livroService.atualizar(id, dto);
        return ResponseEntity.status(200).body(atualizado);
    }

    @Operation(summary = "Deletar livro", description = "Remove um livro pelo ID. Não é possível excluir livros que possuem exemplares cadastrados. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado para o ID informado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito - livro possui exemplares cadastrados e não pode ser excluído", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID do livro", example = "1", required = true)
            @PathVariable Integer id) {
        livroService.deletar(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Pesquisar livro por ISBN (Google Books)", description = "Consulta informações de um livro na API do Google Books usando o ISBN. Requer autenticação JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado da consulta retornado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - JWT ausente ou inválido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro ao chamar a API do Google Books", content = @Content)
    })
    @GetMapping("google/{isbn}")
    public ResponseEntity<Map> pesquisarPorIsbn(
            @Parameter(description = "ISBN do livro", example = "9788573260275", required = true)
            @PathVariable String isbn) {
        RestTemplate restTemplate = new RestTemplate();

        String apiKey = "AIzaSyAm5ipqQ8wdkNjBsU2getBVbcFozM91Duk";
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn + "&key=" + apiKey;

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return response;
        } catch (Exception e) {
            System.out.println("Erro ao chamar a URL: " + url);
            return ResponseEntity.status(500).build();
        }
    }
}

