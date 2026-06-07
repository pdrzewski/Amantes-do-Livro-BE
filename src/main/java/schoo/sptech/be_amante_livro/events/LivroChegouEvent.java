package schoo.sptech.be_amante_livro.events;

import schoo.sptech.be_amante_livro.model.Exemplar;

public class LivroChegouEvent {

    private final String isbn;

    public LivroChegouEvent(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }
}