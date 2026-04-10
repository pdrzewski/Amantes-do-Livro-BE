package schoo.sptech.be_amante_livro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.exception.CondicaoNaoEncontradaException;
import schoo.sptech.be_amante_livro.model.Condicao;
import schoo.sptech.be_amante_livro.repository.CondicaoRepository;

import java.util.Optional;

@Service
public class CondicaoService {

    @Autowired
    private CondicaoRepository repository;

    public Condicao buscarPorId(Integer id) {
        Optional<Condicao> condicaoOpt = repository.findById(id);

        if (condicaoOpt.isEmpty()) {
            throw new CondicaoNaoEncontradaException(id);
        }

        return condicaoOpt.get();
    }
}