package schoo.sptech.be_amante_livro.dto;

import org.hibernate.validator.constraints.NotBlank;

public class LoginRequestDto {

    @NotBlank
    private String usuario;

    @NotBlank
    private String senha;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
