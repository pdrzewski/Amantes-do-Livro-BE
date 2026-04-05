package schoo.sptech.be_amante_livro.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EditoraNaoEncontradaException extends RuntimeException {

    public EditoraNaoEncontradaException(Integer id) {
        super("Editora com id %d não encontrada".formatted(id));
    }
}