package schoo.sptech.be_amante_livro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Condicao;

public interface CondicaoRepository extends JpaRepository<Condicao, Integer> {
    Condicao findByNomeCondicaoIgnoreCase(String nomeCondicao);
}