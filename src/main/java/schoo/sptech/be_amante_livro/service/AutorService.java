package schoo.sptech.be_amante_livro.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.dto.AutorRequestDto;
import schoo.sptech.be_amante_livro.dto.AutorResponseDto;
import schoo.sptech.be_amante_livro.exception.AutorNaoEncontradoException;
import schoo.sptech.be_amante_livro.mapper.AutorMapper;
import schoo.sptech.be_amante_livro.model.Autor;
import schoo.sptech.be_amante_livro.repository.AutorRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;

import java.util.List;

@Service
public class AutorService {

    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;

    public AutorService(AutorRepository autorRepository, LivroRepository livroRepository) {
        this.autorRepository = autorRepository;
        this.livroRepository = livroRepository;
    }
    public List<AutorResponseDto> listar() {
        List<Autor> autores = autorRepository.findAll();

        return autores.stream()
                .map(AutorMapper::toResponse)
                .toList();
    }

    public AutorResponseDto buscarPorId(Integer id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new AutorNaoEncontradoException(id));

        return AutorMapper.toResponse(autor);
    }

    public AutorResponseDto cadastrar(@Valid AutorRequestDto dto) {
        Autor autor = AutorMapper.toEntity(dto);
        Autor salvo = autorRepository.save(autor);
        return AutorMapper.toResponse(salvo);
    }


    public AutorResponseDto atualizar(Integer id, @Valid AutorRequestDto dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new AutorNaoEncontradoException(id));

        autor.setNome(dto.getNome());
        Autor atualizado = autorRepository.save(autor);

        return AutorMapper.toResponse(atualizado);
    }

    public void deletar(Integer id) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new AutorNaoEncontradoException(id));

        if (livroRepository.existsByAutorIdAutor(id)) {
            throw new RuntimeException("Não é possível excluir autor que possui livros cadastrados");
        }

        autorRepository.deleteById(id);
    }


}
