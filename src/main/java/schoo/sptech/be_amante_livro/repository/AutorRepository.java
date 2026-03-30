package schoo.sptech.be_amante_livro.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Autor;


public interface AutorRepository extends JpaRepository<Autor, Integer> {
}