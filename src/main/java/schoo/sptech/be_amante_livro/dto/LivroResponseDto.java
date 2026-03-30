package schoo.sptech.be_amante_livro.dto;


public class LivroResponseDto {

    private Integer id;
    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private AutorDto autor;
    private EditoraDto editora;

    public static class AutorDto {
        private Integer id;
        private String nome;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    public static class EditoraDto {
        private Integer id;
        private String nome;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    // getters e setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public AutorDto getAutor() { return autor; }
    public void setAutor(AutorDto autor) { this.autor = autor; }

    public EditoraDto getEditora() { return editora; }
    public void setEditora(EditoraDto editora) { this.editora = editora; }
}