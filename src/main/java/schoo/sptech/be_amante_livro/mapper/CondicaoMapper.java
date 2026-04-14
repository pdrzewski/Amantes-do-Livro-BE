package schoo.sptech.be_amante_livro.mapper;

import schoo.sptech.be_amante_livro.dto.CondicaoRequestDto;
import schoo.sptech.be_amante_livro.dto.CondicaoResponseDto;
import schoo.sptech.be_amante_livro.model.Condicao;

public class CondicaoMapper {
    public static Condicao toEntity (CondicaoRequestDto dto) {
        Condicao condicao = new Condicao();

        condicao.setNomeCondicao(dto.getNomeCondicao());

        return condicao;
    }

    public static CondicaoResponseDto toResponse (Condicao dto) {
        CondicaoResponseDto condicaoResponseDto = new CondicaoResponseDto();

        condicaoResponseDto.setIdCondicao(dto.getIdCondicao());
        condicaoResponseDto.setNomeCondicao(dto.getNomeCondicao());

        return condicaoResponseDto;
    }
}
