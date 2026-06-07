package schoo.sptech.be_amante_livro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import schoo.sptech.be_amante_livro.dto.ListaDesejoCadastroDto;
import schoo.sptech.be_amante_livro.model.ListaDesejo;
import schoo.sptech.be_amante_livro.service.ListaDesejoService;

@RestController
@RequestMapping("/lista-desejo")
@Tag(name = "Lista de desejos", description = "Endpoint para cadastrar interesse de usuários em livros que não existem no estoque.")
public class ListaDesejoController {

    private final ListaDesejoService service;

    public ListaDesejoController(
            ListaDesejoService service) {

        this.service = service;
    }

    @Operation(summary = "Cadastrar Interesse", description = "Adiciona um nome e email vinculado a um livro de interesse")
    @PostMapping
    public ResponseEntity<ListaDesejo> cadastrar(
            @RequestBody ListaDesejoCadastroDto dto) {

        ListaDesejo listaDesejo =
                service.cadastrar(dto);

        return ResponseEntity
                .status(201)
                .body(listaDesejo);
    }
}