package schoo.sptech.be_amante_livro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCadastroDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String usuario;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank
    @Size(min = 6, max = 100)
    private String senha;

    public @NotBlank @Size(min = 3, max = 50) String getUsuario() {
        return usuario;
    }

    public void setUsuario(@NotBlank @Size(min = 3, max = 50) String usuario) {
        this.usuario = usuario;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 3, max = 100) String getNome() {
        return nome;
    }

    public void setNome(@NotBlank @Size(min = 3, max = 100) String nome) {
        this.nome = nome;
    }

    public @NotBlank @Size(min = 6, max = 100) String getSenha() {
        return senha;
    }

    public void setSenha(@NotBlank @Size(min = 6, max = 100) String senha) {
        this.senha = senha;
    }
}