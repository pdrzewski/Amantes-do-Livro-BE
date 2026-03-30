package schoo.sptech.be_amante_livro.mapper;
import schoo.sptech.be_amante_livro.dto.LivroRequestDto;
import schoo.sptech.be_amante_livro.dto.LivroResponseDto;
import schoo.sptech.be_amante_livro.model.Livro;

import java.util.List;

public class LivroMapper {

    public static Livro toEntity(LivroRequestDto dto) {
        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        return livro;
    }

    public static LivroResponseDto toResponseDto(Livro livro) {

        LivroResponseDto dto = new LivroResponseDto();

        dto.setId(livro.getIdLivro());
        dto.setTitulo(livro.getTitulo());
        dto.setIsbn(livro.getIsbn());
        dto.setAnoPublicacao(livro.getAnoPublicacao());

        // Autor
        LivroResponseDto.AutorDto autorDto = new LivroResponseDto.AutorDto();
        autorDto.setId(livro.getAutor().getIdAutor());
        autorDto.setNome(livro.getAutor().getNome());

        // Editora
        LivroResponseDto.EditoraDto editoraDto = new LivroResponseDto.EditoraDto();
        editoraDto.setId(livro.getEditora().getIdEditora());
        editoraDto.setNome(livro.getEditora().getNomeEditora());

        dto.setAutor(autorDto);
        dto.setEditora(editoraDto);

        return dto;
    }

    public static List<LivroResponseDto> toResponseDtoList(List<Livro> livros) {
        return livros.stream().map(LivroMapper::toResponseDto).toList();
    }
}