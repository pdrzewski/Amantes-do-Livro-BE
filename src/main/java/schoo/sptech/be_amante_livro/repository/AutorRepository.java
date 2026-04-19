package schoo.sptech.be_amante_livro.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Autor;

import java.util.Optional;


public interface AutorRepository extends JpaRepository<Autor, Integer> {
    Optional<Autor> findByNomeIgnoreCase(String nome);
}