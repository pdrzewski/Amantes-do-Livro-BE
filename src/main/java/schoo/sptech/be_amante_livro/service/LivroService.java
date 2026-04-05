package schoo.sptech.be_amante_livro.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.dto.LivroRequestDto;
import schoo.sptech.be_amante_livro.dto.LivroResponseDto;
import schoo.sptech.be_amante_livro.exception.AutorNaoEncontradoException;
import schoo.sptech.be_amante_livro.exception.EditoraNaoEncontradaException;
import schoo.sptech.be_amante_livro.exception.LivroNaoEncontradoException;
import schoo.sptech.be_amante_livro.mapper.LivroMapper;
import schoo.sptech.be_amante_livro.model.Autor;
import schoo.sptech.be_amante_livro.model.Editora;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.AutorRepository;
import schoo.sptech.be_amante_livro.repository.EditoraRepository;
import schoo.sptech.be_amante_livro.repository.ExemplarRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final EditoraRepository editoraRepository;
    private final ExemplarRepository exemplarRepository;

    public LivroService(LivroRepository livroRepository,
                        AutorRepository autorRepository,
                        EditoraRepository editoraRepository, ExemplarRepository exemplarRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.editoraRepository = editoraRepository;
        this.exemplarRepository = exemplarRepository;
    }

    public LivroResponseDto cadastrar(@Valid LivroRequestDto dto) {

        Optional<Autor> autorOpt = autorRepository.findById(dto.getIdAutor());
        if (autorOpt.isEmpty()) {
            throw new LivroNaoEncontradoException(dto.getIdAutor());
        }

        Optional<Editora> editoraOpt = editoraRepository.findById(dto.getIdEditora());
        if (editoraOpt.isEmpty()) {
            throw new EditoraNaoEncontradaException(dto.getIdEditora());
        }

        Autor autor = autorOpt.get();
        Editora editora = editoraOpt.get();

        Livro livro = new LivroMapper().toEntity(dto, autor, editora);

        Livro salvo = livroRepository.save(livro);

        return LivroMapper.toResponse(salvo);
    }


    public List<LivroResponseDto> listar() {

        List<Livro> livros = livroRepository.findAll();

        return LivroMapper.toResponseDtoList(livros);
    }


    public LivroResponseDto buscarPorId(Integer id) {

        Optional<Livro> livroOpt = livroRepository.findById(id);

        if (livroOpt.isEmpty()) {
            throw new LivroNaoEncontradoException(id);
        }

        return LivroMapper.toResponse(livroOpt.get());
    }


    public LivroResponseDto atualizar(Integer id, @Valid LivroRequestDto dto) {

        Optional<Livro> livroOpt = livroRepository.findById(id);
        if (livroOpt.isEmpty()) {
            throw new LivroNaoEncontradoException(id);
        }

        Optional<Autor> autorOpt = autorRepository.findById(dto.getIdAutor());
        if (autorOpt.isEmpty()) {
            throw new AutorNaoEncontradoException(dto.getIdAutor());
        }

        Optional<Editora> editoraOpt = editoraRepository.findById(dto.getIdEditora());
        if (editoraOpt.isEmpty()) {
            throw new EditoraNaoEncontradaException(dto.getIdEditora());
        }

        Livro livro = livroOpt.get();

        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setAutor(autorOpt.get());
        livro.setEditora(editoraOpt.get());

        Livro atualizado = livroRepository.save(livro);

        return LivroMapper.toResponse(atualizado);
    }

    public void deletar(Integer id) {

        Optional<Livro> livroOpt = livroRepository.findById(id);

        if (livroOpt.isEmpty()) {
            throw new LivroNaoEncontradoException(id);
        }

        if (exemplarRepository.existsByLivroIdLivro(id)) {
            throw new RuntimeException("Não é possível excluir livro que possui exemplares cadastrados");
        }

        livroRepository.deleteById(id);
    }
}