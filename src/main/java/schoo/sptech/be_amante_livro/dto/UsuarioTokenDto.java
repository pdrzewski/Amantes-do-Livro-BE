package schoo.sptech.be_amante_livro.dto;

public class UsuarioTokenDto {
    private Integer userId;
    private String usuario;
    private String token;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}