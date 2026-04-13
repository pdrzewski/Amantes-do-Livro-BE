package schoo.sptech.be_amante_livro.mapper;

import schoo.sptech.be_amante_livro.dto.LivroResponseDto;
import schoo.sptech.be_amante_livro.dto.LoginRequestDto;
import schoo.sptech.be_amante_livro.dto.LoginResponseDto;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.model.Login;

import java.util.ArrayList;
import java.util.List;

public class LoginMapper {
    public static Login toEntity (LoginRequestDto dto) {
        Login login = new Login();

        login.setUsuario(dto.getUsuario());
        login.setSenha(dto.getSenha());

        return login;
    }

    public static LoginResponseDto toResponse (Login dto) {
        LoginResponseDto login = new LoginResponseDto();

        login.setId(dto.getIdLogin());
        login.setUsuario(dto.getUsuario());
        login.setSenha(dto.getSenha());

        return login;
    }

    public static List<LoginResponseDto> toResponsrDtoList(List<Login> logins) {

        List<LoginResponseDto> listaDto = new ArrayList<>();

        for (int i = 0; i < logins.size(); i++) {
            Login login = logins.get(i);

            LoginResponseDto dto = toResponse(login);

            listaDto.add(dto);
        }

        return listaDto;
    }
}