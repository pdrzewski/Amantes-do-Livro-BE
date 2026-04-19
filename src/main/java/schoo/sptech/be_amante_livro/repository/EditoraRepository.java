package schoo.sptech.be_amante_livro.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Editora;

import java.util.Optional;


public interface EditoraRepository extends JpaRepository<Editora, Integer> {
    Optional<Editora> findByNomeEditoraIgnoreCase(String nomeEditora);
}
