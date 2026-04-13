package schoo.sptech.be_amante_livro.exception;

public class UsernameNaoEncontradoException extends RuntimeException {
    public UsernameNaoEncontradoException(String username) {
        super("Usuario não encontrado");
    }
}
