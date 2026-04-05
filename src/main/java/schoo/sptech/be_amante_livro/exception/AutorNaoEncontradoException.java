package schoo.sptech.be_amante_livro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AutorNaoEncontradoException extends RuntimeException {

    public AutorNaoEncontradoException(Integer id) {
        super("Autor com id %d não encontrado".formatted(id));
    }
}
