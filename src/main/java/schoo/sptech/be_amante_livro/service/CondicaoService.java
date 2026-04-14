package schoo.sptech.be_amante_livro.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.dto.CondicaoRequestDto;
import schoo.sptech.be_amante_livro.dto.CondicaoResponseDto;
import schoo.sptech.be_amante_livro.exception.AutorNaoEncontradoException;
import schoo.sptech.be_amante_livro.exception.CondicaoNaoEncontradaException;
import schoo.sptech.be_amante_livro.mapper.CondicaoMapper;
import schoo.sptech.be_amante_livro.model.Condicao;
import schoo.sptech.be_amante_livro.repository.CondicaoRepository;
import java.util.List;

@Service
public class CondicaoService {

    @Autowired
    private CondicaoRepository repository;

    public CondicaoResponseDto buscarPorId(Integer id) {
        Condicao condicao = repository.findById(id)
                .orElseThrow(() -> new AutorNaoEncontradoException(id));

        return CondicaoMapper.toResponse(condicao);
    }

    public List<CondicaoResponseDto> listar() {
        List<Condicao> condicoes = repository.findAll();

        return condicoes.stream().map(CondicaoMapper::toResponse).toList();
    }

    public CondicaoResponseDto cadastrar(@Valid CondicaoRequestDto dto) {
        Condicao condicao = CondicaoMapper.toEntity(dto);
        Condicao salvo = repository.save(condicao);
        return CondicaoMapper.toResponse(salvo);
    }

    public CondicaoResponseDto atualizar(Integer id, @Valid CondicaoRequestDto dto) {
        Condicao condicao = repository.findById(id)
                .orElseThrow(() -> new CondicaoNaoEncontradaException(id));

        condicao.setNomeCondicao(dto.getNomeCondicao());
        Condicao atualizado = repository.save(condicao);

        return CondicaoMapper.toResponse(atualizado);
    }

    public void deletar(Integer id) {
        Condicao condicao = repository.findById(id)
                .orElseThrow(() -> new CondicaoNaoEncontradaException(id));

        repository.deleteById(id);
    }
}