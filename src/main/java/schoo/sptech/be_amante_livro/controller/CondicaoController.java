package schoo.sptech.be_amante_livro.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schoo.sptech.be_amante_livro.dto.CondicaoRequestDto;
import schoo.sptech.be_amante_livro.dto.CondicaoResponseDto;
import schoo.sptech.be_amante_livro.service.CondicaoService;

import java.util.List;

@RestController
@RequestMapping("/condicoes")
public class CondicaoController {
    private final CondicaoService condicaoService;

    public CondicaoController(CondicaoService condicaoService) {
        this.condicaoService = condicaoService;
    }

    @PostMapping
    public ResponseEntity<CondicaoResponseDto> cadastrar(@RequestBody @Valid CondicaoRequestDto dto) {
        CondicaoResponseDto resposta = condicaoService.cadastrar(dto);
        return ResponseEntity.status(201).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<CondicaoResponseDto>> listar() {
        List<CondicaoResponseDto> lista = condicaoService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CondicaoResponseDto> buscarPorId(@PathVariable Integer id) {
        CondicaoResponseDto dto = condicaoService.buscarPorId(id);
        return ResponseEntity.status(200).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CondicaoResponseDto> atualizar(@PathVariable Integer id, @RequestBody @Valid CondicaoRequestDto dto) {
        CondicaoResponseDto atualizado = condicaoService.atualizar(id, dto);
        return ResponseEntity.status(200).body(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        condicaoService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}
