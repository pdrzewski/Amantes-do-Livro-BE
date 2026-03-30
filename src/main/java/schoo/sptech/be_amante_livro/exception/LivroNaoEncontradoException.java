package schoo.sptech.be_amante_livro.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LivroNaoEncontradoException extends RuntimeException {

    public LivroNaoEncontradoException(Integer id) {
        super("Livro com id %d não encontrado".formatted(id));
    }
}