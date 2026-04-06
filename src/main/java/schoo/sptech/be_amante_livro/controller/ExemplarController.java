package schoo.sptech.be_amante_livro.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schoo.sptech.be_amante_livro.dto.ExemplarRequestDto;
import schoo.sptech.be_amante_livro.dto.ExemplarResponseDto;
import schoo.sptech.be_amante_livro.service.ExemplarService;

import java.util.List;

@RestController
@RequestMapping("/exemplares")
public class ExemplarController {

    private final ExemplarService service;

    public ExemplarController(ExemplarService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ExemplarResponseDto> cadastrar(@Valid @RequestBody ExemplarRequestDto dto) {
        return ResponseEntity.status(201).body(service.cadastrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ExemplarResponseDto>> listar() {

        List<ExemplarResponseDto> lista = service.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExemplarResponseDto> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExemplarResponseDto> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ExemplarRequestDto dto) {

        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{id}/baixa/{qtd}")
    public ResponseEntity<Void> baixarEstoque(
            @PathVariable Integer id,
            @PathVariable Integer qtd) {

        service.baixarEstoque(id, qtd);

        return ResponseEntity.noContent().build();
    }
}