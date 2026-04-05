package schoo.sptech.be_amante_livro.dto;

import jakarta.validation.constraints.*;

public class LivroRequestDto {

    @NotBlank
    private String titulo;

    private String isbn;

    private Integer anoPublicacao;

    @NotNull
    @Positive
    private Integer idAutor;

    @NotNull
    @Positive
    private Integer idEditora;

    public @NotBlank String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NotBlank String titulo) {
        this.titulo = titulo;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public @NotNull @Positive Integer getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(@NotNull @Positive Integer idAutor) {
        this.idAutor = idAutor;
    }

    public @NotNull @Positive Integer getIdEditora() {
        return idEditora;
    }

    public void setIdEditora(@NotNull @Positive Integer idEditora) {
        this.idEditora = idEditora;
    }
}