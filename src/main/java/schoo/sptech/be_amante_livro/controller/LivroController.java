package schoo.sptech.be_amante_livro.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schoo.sptech.be_amante_livro.dto.LivroRequestDto;
import schoo.sptech.be_amante_livro.dto.LivroResponseDto;
import schoo.sptech.be_amante_livro.service.LivroService;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    public ResponseEntity<LivroResponseDto> cadastrar(@RequestBody @Valid LivroRequestDto dto) {

        LivroResponseDto resposta = livroService.cadastrar(dto);

        return ResponseEntity.status(201).body(resposta);
    }


    @GetMapping
    public ResponseEntity<List<LivroResponseDto>> listar() {

        List<LivroResponseDto> lista = livroService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDto> buscarPorId(@PathVariable Integer id) {

        LivroResponseDto dto = livroService.buscarPorId(id);

        return ResponseEntity.status(200).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid LivroRequestDto dto) {

        LivroResponseDto atualizado = livroService.atualizar(id, dto);

        return ResponseEntity.status(200).body(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {

        livroService.deletar(id);

        return ResponseEntity.status(204).build();
    }
}