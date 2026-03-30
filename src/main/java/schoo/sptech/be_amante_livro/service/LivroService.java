package schoo.sptech.be_amante_livro.service;


import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.exception.LivroNaoEncontradoException;
import schoo.sptech.be_amante_livro.model.Autor;
import schoo.sptech.be_amante_livro.model.Editora;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.AutorRepository;
import schoo.sptech.be_amante_livro.repository.EditoraRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;


import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final EditoraRepository editoraRepository;

    public LivroService(LivroRepository livroRepository,
                        AutorRepository autorRepository,
                        EditoraRepository editoraRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.editoraRepository = editoraRepository;
    }

    public Livro criar(Livro livro, Integer autorId, Integer editoraId) {

        Autor autor = autorRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Editora editora = editoraRepository.findById(editoraId)
                .orElseThrow(() -> new RuntimeException("Editora não encontrada"));

        livro.setAutor(autor);
        livro.setEditora(editora);

        return livroRepository.save(livro);
    }

    public List<Livro> listar() {
        return livroRepository.findAll();
    }

    public Livro buscarPorId(Integer id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new LivroNaoEncontradoException(id));
    }

    public Livro atualizar(Integer id, Livro livro, Integer autorId, Integer editoraId) {

        Livro existente = buscarPorId(id);

        Autor autor = autorRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        Editora editora = editoraRepository.findById(editoraId)
                .orElseThrow(() -> new RuntimeException("Editora não encontrada"));

        existente.setTitulo(livro.getTitulo());
        existente.setIsbn(livro.getIsbn());
        existente.setAnoPublicacao(livro.getAnoPublicacao());
        existente.setAutor(autor);
        existente.setEditora(editora);

        return livroRepository.save(existente);
    }

    public void deletar(Integer id) {
        Livro livro = buscarPorId(id);
        livroRepository.delete(livro);
    }
}
