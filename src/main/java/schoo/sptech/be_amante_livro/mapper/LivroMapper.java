package schoo.sptech.be_amante_livro.mapper;

import schoo.sptech.be_amante_livro.dto.LivroRequestDto;
import schoo.sptech.be_amante_livro.dto.LivroResponseDto;
import schoo.sptech.be_amante_livro.model.Autor;
import schoo.sptech.be_amante_livro.model.Editora;
import schoo.sptech.be_amante_livro.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class LivroMapper {

    public Livro toEntity(LivroRequestDto dto, Autor autor, Editora editora) {
        Livro livro = new Livro();

        livro.setTitulo(dto.getTitulo());
        livro.setIsbn(dto.getIsbn());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setAutor(autor);
        livro.setEditora(editora);

        return livro;
    }

    public static LivroResponseDto toResponse(Livro livro) {
        LivroResponseDto dto = new LivroResponseDto();

        dto.setIdLivro(livro.getIdLivro());
        dto.setTitulo(livro.getTitulo());
        dto.setIsbn(livro.getIsbn());
        dto.setAnoPublicacao(livro.getAnoPublicacao());

        // AUTOR
        if (livro.getAutor() != null) {
            LivroResponseDto.AutorDto autorDto = new LivroResponseDto.AutorDto();
            autorDto.setId(livro.getAutor().getIdAutor());
            autorDto.setNome(livro.getAutor().getNome());

            dto.setAutor(autorDto);
        }

        // EDITORA
        if (livro.getEditora() != null) {
            LivroResponseDto.EditoraDto editoraDto = new LivroResponseDto.EditoraDto();
            editoraDto.setId(livro.getEditora().getIdEditora());
            editoraDto.setNome(livro.getEditora().getNomeEditora());

            dto.setEditora(editoraDto);
        }

        return dto;
    }

    public static List<LivroResponseDto> toResponseDtoList(List<Livro> livros) {

        List<LivroResponseDto> listaDto = new ArrayList<>();

        for (int i = 0; i < livros.size(); i++) {
            Livro livro = livros.get(i);

            LivroResponseDto dto = toResponse(livro);

            listaDto.add(dto);
        }

        return listaDto;
    }
}