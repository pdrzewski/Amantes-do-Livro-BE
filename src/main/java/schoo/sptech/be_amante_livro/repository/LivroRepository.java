package schoo.sptech.be_amante_livro.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Livro;


public interface LivroRepository extends JpaRepository<Livro, Integer> {
    boolean existsByAutorIdAutor(Integer idAutor);
}
