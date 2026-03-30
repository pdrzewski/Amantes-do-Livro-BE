package schoo.sptech.be_amante_livro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import schoo.sptech.be_amante_livro.dto.LivroRequestDto;
import schoo.sptech.be_amante_livro.dto.LivroResponseDto;
import schoo.sptech.be_amante_livro.mapper.LivroMapper;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.service.LivroService;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<LivroResponseDto> criar(@RequestBody @Valid LivroRequestDto dto) {
        Livro livro = LivroMapper.toEntity(dto);
        Livro criado = service.criar(livro, dto.getAutorId(), dto.getEditoraId());
        return ResponseEntity.status(201).body(LivroMapper.toResponseDto(criado));
    }

    @GetMapping
    public ResponseEntity<List<LivroResponseDto>> listar() {
        List<Livro> livros = service.listar();
        return ResponseEntity.ok(LivroMapper.toResponseDtoList(livros));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDto> buscarPorId(@PathVariable Integer id) {
        Livro livro = service.buscarPorId(id);
        return ResponseEntity.ok(LivroMapper.toResponseDto(livro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDto> atualizar(@PathVariable Integer id,
                                                      @RequestBody @Valid LivroRequestDto dto) {
        Livro livro = LivroMapper.toEntity(dto);
        Livro atualizado = service.atualizar(id, livro, dto.getAutorId(), dto.getEditoraId());
        return ResponseEntity.ok(LivroMapper.toResponseDto(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}