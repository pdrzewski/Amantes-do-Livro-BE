package schoo.sptech.be_amante_livro.exception;

public class LoginNaoEncontradoException extends RuntimeException {
    public LoginNaoEncontradoException(Integer id) {
        super("Login não encontrado");
    }
}
