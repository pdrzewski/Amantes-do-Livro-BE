package schoo.sptech.be_amante_livro.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Livro;

import java.util.Optional;


public interface LivroRepository extends JpaRepository<Livro, Integer> {
    boolean existsByAutorIdAutor(Integer idAutor);
    Optional<Livro> findByIsbn(String isbn);
}
