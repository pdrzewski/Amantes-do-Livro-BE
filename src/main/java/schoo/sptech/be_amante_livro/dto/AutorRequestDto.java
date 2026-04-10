package schoo.sptech.be_amante_livro.dto;

import jakarta.validation.constraints.NotBlank;

public class AutorRequestDto {

    @NotBlank
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
