package schoo.sptech.be_amante_livro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import schoo.sptech.be_amante_livro.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsuario(String usuario);
}
