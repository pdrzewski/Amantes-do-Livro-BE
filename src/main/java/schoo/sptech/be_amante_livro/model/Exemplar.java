package schoo.sptech.be_amante_livro.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idExemplar;

    @ManyToOne
    @JoinColumn(name = "id_livro")
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "id_condicao")
    private Condicao condicao;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private StatusExemplar status;

    private Integer quantidade;
    private Double preco;
    private LocalDateTime dataEntrada;

    public Integer getIdExemplar() {
        return idExemplar;
    }

    public void setIdExemplar(Integer idExemplar) {
        this.idExemplar = idExemplar;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public Condicao getCondicao() {
        return condicao;
    }

    public void setCondicao(Condicao condicao) {
        this.condicao = condicao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }


}