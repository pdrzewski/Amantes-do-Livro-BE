package schoo.sptech.be_amante_livro.mapper;

import schoo.sptech.be_amante_livro.dto.AutorRequestDto;
import schoo.sptech.be_amante_livro.dto.AutorResponseDto;
import schoo.sptech.be_amante_livro.model.Autor;

public class AutorMapper {
    public static Autor toEntity (AutorRequestDto dto) {
        Autor autor = new Autor();

        autor.setNome(dto.getNome());

        return autor;
    }

    public static AutorResponseDto toResponse (Autor dto) {
        AutorResponseDto autor = new AutorResponseDto();

        autor.setIdAutor(dto.getIdAutor());
        autor.setNome(dto.getNome());

        return autor;
    }
}
