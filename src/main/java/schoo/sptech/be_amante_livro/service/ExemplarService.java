package schoo.sptech.be_amante_livro.service;

import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.dto.ExemplarRequestDto;
import schoo.sptech.be_amante_livro.dto.ExemplarResponseDto;
import schoo.sptech.be_amante_livro.exception.CondicaoNaoEncontradaException;
import schoo.sptech.be_amante_livro.exception.ExemplarNaoEncontradoException;
import schoo.sptech.be_amante_livro.exception.LivroNaoEncontradoException;
import schoo.sptech.be_amante_livro.mapper.ExemplarMapper;
import schoo.sptech.be_amante_livro.model.Condicao;
import schoo.sptech.be_amante_livro.model.Exemplar;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.CondicaoRepository;
import schoo.sptech.be_amante_livro.repository.ExemplarRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ExemplarService {

    private final ExemplarRepository exemplarRepository;
    private final LivroRepository livroRepository;
    private final CondicaoRepository condicaoRepository;

    public ExemplarService(ExemplarRepository exemplarRepository,
                           LivroRepository livroRepository,
                           CondicaoRepository condicaoRepository) {

        this.exemplarRepository = exemplarRepository;
        this.livroRepository = livroRepository;
        this.condicaoRepository = condicaoRepository;
    }

    // CREATE
    public ExemplarResponseDto cadastrar(ExemplarRequestDto dto) {

        Optional<Livro> livroOpt = livroRepository.findById(dto.getIdLivro());

        if (!livroOpt.isPresent()) {
            throw new LivroNaoEncontradoException(dto.getIdLivro());
        }

        Optional<Condicao> condicaoOpt = condicaoRepository.findById(dto.getIdCondicao());

        if (!condicaoOpt.isPresent()) {
            throw new CondicaoNaoEncontradaException(dto.getIdCondicao());
        }

        Exemplar exemplar = ExemplarMapper.toEntity(
                dto,
                livroOpt.get(),
                condicaoOpt.get()
        );

        exemplarRepository.save(exemplar);

        return ExemplarMapper.toResponse(exemplar);
    }


    public List<ExemplarResponseDto> listar() {

        List<Exemplar> lista = exemplarRepository.findAll();

        return ExemplarMapper.toList(lista);
    }

    public ExemplarResponseDto buscarPorId(Integer id) {

        Optional<Exemplar> exemplarOpt = exemplarRepository.findById(id);

        if (!exemplarOpt.isPresent()) {
            throw new ExemplarNaoEncontradoException(id);
        }

        return ExemplarMapper.toResponse(exemplarOpt.get());
    }

    public ExemplarResponseDto atualizar(Integer id, ExemplarRequestDto dto) {

        Optional<Exemplar> exemplarOpt = exemplarRepository.findById(id);

        if (!exemplarOpt.isPresent()) {
            throw new ExemplarNaoEncontradoException(id);
        }

        Optional<Livro> livroOpt = livroRepository.findById(dto.getIdLivro());

        if (!livroOpt.isPresent()) {
            throw new LivroNaoEncontradoException(dto.getIdLivro());
        }

        Optional<Condicao> condicaoOpt = condicaoRepository.findById(dto.getIdCondicao());

        if (!condicaoOpt.isPresent()) {
            throw new CondicaoNaoEncontradaException(dto.getIdCondicao());
        }

        Exemplar exemplar = exemplarOpt.get();

        exemplar.setLivro(livroOpt.get());
        exemplar.setCondicao(condicaoOpt.get());
        exemplar.setPreco(dto.getPreco());
        exemplar.setQuantidade(dto.getQuantidade());

        exemplarRepository.save(exemplar);

        return ExemplarMapper.toResponse(exemplar);
    }


    public void deletar(Integer id) {

        Optional<Exemplar> exemplarOpt = exemplarRepository.findById(id);

        if (!exemplarOpt.isPresent()) {
            throw new ExemplarNaoEncontradoException(id);
        }

        exemplarRepository.deleteById(id);
    }

    public void baixarEstoque(Integer id, Integer quantidade) {

        Optional<Exemplar> exemplarOpt = exemplarRepository.findById(id);

        if (!exemplarOpt.isPresent()) {
            throw new ExemplarNaoEncontradoException(id);
        }

        if (quantidade <= 0) {
            throw new RuntimeException("Quantidade inválida");
        }

        Exemplar exemplar = exemplarOpt.get();

        if (exemplar.getQuantidade() < quantidade) {
            throw new RuntimeException("Estoque insuficiente");
        }

        exemplar.setQuantidade(exemplar.getQuantidade() - quantidade);

        exemplarRepository.save(exemplar);
    }
}