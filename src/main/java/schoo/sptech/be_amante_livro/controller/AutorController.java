package schoo.sptech.be_amante_livro.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schoo.sptech.be_amante_livro.dto.AutorRequestDto;
import schoo.sptech.be_amante_livro.dto.AutorResponseDto;
import schoo.sptech.be_amante_livro.service.AutorService;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<AutorResponseDto> cadastrar(@RequestBody @Valid AutorRequestDto dto) {
        AutorResponseDto resposta = autorService.cadastrar(dto);
        return ResponseEntity.status(201).body(resposta);
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDto>> listar() {
        List<AutorResponseDto> lista = autorService.listar();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDto> buscarPorId(@PathVariable Integer id) {
        AutorResponseDto dto = autorService.buscarPorId(id);
        return ResponseEntity.status(200).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid AutorRequestDto dto) {
        AutorResponseDto atualizado = autorService.atualizar(id, dto);
        return ResponseEntity.status(200).body(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        autorService.deletar(id);
        return ResponseEntity.status(204).build();
    }
}