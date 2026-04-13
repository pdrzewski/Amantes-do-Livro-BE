package schoo.sptech.be_amante_livro.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import schoo.sptech.be_amante_livro.model.Login;
import java.util.Collection;

public class LoginDetalhesDto implements UserDetails {
    private final String usuario;
    private final String senha;

    public LoginDetalhesDto(Login login) {
        this.usuario = login.getUsuario();
        this.senha = login.getSenha();
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return null; }
    @Override public String getPassword() { return senha; }
    @Override public String getUsername() { return usuario; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}