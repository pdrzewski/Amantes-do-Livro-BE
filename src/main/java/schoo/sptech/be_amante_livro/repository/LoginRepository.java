package schoo.sptech.be_amante_livro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Login;

public interface LoginRepository extends JpaRepository<Login, Integer> {

}
