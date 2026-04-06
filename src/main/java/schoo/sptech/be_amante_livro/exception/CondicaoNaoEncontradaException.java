package schoo.sptech.be_amante_livro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CondicaoNaoEncontradaException extends RuntimeException {

    public CondicaoNaoEncontradaException(Integer id) {
        super("Condição com id %d não encontrada".formatted(id));
    }
}