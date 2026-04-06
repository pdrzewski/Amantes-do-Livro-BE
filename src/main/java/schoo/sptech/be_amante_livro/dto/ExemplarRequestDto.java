package schoo.sptech.be_amante_livro.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public class ExemplarRequestDto {

    @NotNull
    private Integer idLivro;

    @NotNull
    private Integer idCondicao;

    @NotNull
    private Double preco;

    @NotNull
    @Min(1)
    private Integer quantidade;

    private Integer idStatus;

    public @NotNull Integer getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(@NotNull Integer idLivro) {
        this.idLivro = idLivro;
    }

    public @NotNull Integer getIdCondicao() {
        return idCondicao;
    }

    public void setIdCondicao(@NotNull Integer idCondicao) {
        this.idCondicao = idCondicao;
    }

    public @NotNull Double getPreco() {
        return preco;
    }

    public void setPreco(@NotNull Double preco) {
        this.preco = preco;
    }

    public @NotNull @Min(1) Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(@NotNull @Min(1) Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(Integer idStatus) {
        this.idStatus = idStatus;
    }
}


