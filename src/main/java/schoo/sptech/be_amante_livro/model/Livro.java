package schoo.sptech.be_amante_livro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLivro;

    @NotBlank(message = "Título é obrigatório")
    @Size(min = 3, max = 200)
    private String titulo;

    @Size(max = 20)
    private String isbn;

    @Min(value = 0, message = "Ano inválido")
    private Integer anoPublicacao;

    @ManyToOne
    @JoinColumn(name = "id_autor")
    private Autor autor;

    @ManyToOne
    @JoinColumn(name = "id_editora")
    private Editora editora;

    @OneToMany(mappedBy = "livro")
    private List<Exemplar> exemplares;

    public Integer getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Integer idLivro) {
        this.idLivro = idLivro;
    }

    public @NotBlank(message = "Título é obrigatório") @Size(min = 3, max = 200) String getTitulo() {
        return titulo;
    }

    public void setTitulo(@NotBlank(message = "Título é obrigatório") @Size(min = 3, max = 200) String titulo) {
        this.titulo = titulo;
    }

    public @Size(max = 20) String getIsbn() {
        return isbn;
    }

    public void setIsbn(@Size(max = 20) String isbn) {
        this.isbn = isbn;
    }

    public @Min(value = 0, message = "Ano inválido") Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(@Min(value = 0, message = "Ano inválido") Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }
}