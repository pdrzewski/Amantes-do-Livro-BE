package schoo.sptech.be_amante_livro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Exemplar;

import java.util.Optional;

public interface ExemplarRepository extends JpaRepository<Exemplar, Integer> {

    boolean existsByLivroIdLivro(Integer idLivro);

    Optional<Exemplar> findByLivro_IdLivroAndCondicao_IdCondicao(Integer idLivro, Integer idCondicao);
}