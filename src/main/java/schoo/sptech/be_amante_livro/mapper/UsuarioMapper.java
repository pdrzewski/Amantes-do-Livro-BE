package schoo.sptech.be_amante_livro.mapper;

import schoo.sptech.be_amante_livro.dto.UsuarioCadastroDto;
import schoo.sptech.be_amante_livro.dto.UsuarioLoginRequestDto;
import schoo.sptech.be_amante_livro.dto.UsuarioLoginResponseDto;
import schoo.sptech.be_amante_livro.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioMapper {
    public static Usuario toEntity (UsuarioLoginRequestDto dto) {
        Usuario login = new Usuario();

        login.setUsuario(dto.getUsuario());
        login.setSenha(dto.getSenha());

        return login;
    }

    public static UsuarioLoginResponseDto toResponse (Usuario dto) {
        UsuarioLoginResponseDto login = new UsuarioLoginResponseDto();

        login.setId(dto.getId());
        login.setUsuario(dto.getUsuario());
        login.setSenha(dto.getSenha());

        return login;
    }

    public static List<UsuarioLoginResponseDto> toResponsrDtoList(List<Usuario> logins) {

        List<UsuarioLoginResponseDto> listaDto = new ArrayList<>();

        for (int i = 0; i < logins.size(); i++) {
            Usuario login = logins.get(i);

            UsuarioLoginResponseDto dto = toResponse(login);

            listaDto.add(dto);
        }

        return listaDto;
    }

    public static Usuario toEntityCadastro(
            UsuarioCadastroDto dto
    ) {

        Usuario usuario = new Usuario();

        usuario.setUsuario(dto.getUsuario());
        usuario.setEmail(dto.getEmail());
        usuario.setNome(dto.getNome());
        usuario.setSenha(dto.getSenha());

        return usuario;
    }
}