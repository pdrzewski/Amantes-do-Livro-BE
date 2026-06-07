package schoo.sptech.be_amante_livro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import schoo.sptech.be_amante_livro.model.ListaDesejo;

import java.util.List;

@Repository
public interface ListaDesejoRepository
        extends JpaRepository<ListaDesejo, Integer> {

    List<ListaDesejo>
    findByIsbnAndNotificadoFalse(String isbn);
}
