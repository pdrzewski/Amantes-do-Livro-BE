package schoo.sptech.be_amante_livro.dto;

import jakarta.validation.constraints.NotBlank;

public class CondicaoRequestDto {
    @NotBlank(message = "Nome da condição não pode estar vazio")
    private String nomeCondicao;

    public String getNomeCondicao() {
        return nomeCondicao;
    }

    public void setNomeCondicao(String nomeCondicao) {
        this.nomeCondicao = nomeCondicao;
    }
}
