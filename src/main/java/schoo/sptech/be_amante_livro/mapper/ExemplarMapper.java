package schoo.sptech.be_amante_livro.mapper;

import schoo.sptech.be_amante_livro.dto.CondicaoDto;
import schoo.sptech.be_amante_livro.dto.ExemplarRequestDto;
import schoo.sptech.be_amante_livro.dto.ExemplarResponseDto;
import schoo.sptech.be_amante_livro.dto.LivroResumoDto;
import schoo.sptech.be_amante_livro.model.Condicao;
import schoo.sptech.be_amante_livro.model.Exemplar;
import schoo.sptech.be_amante_livro.model.Livro;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExemplarMapper {

    public static Exemplar toEntity(ExemplarRequestDto dto, Livro livro, Condicao condicao) {

        Exemplar exemplar = new Exemplar();

        exemplar.setLivro(livro);
        exemplar.setCondicao(condicao);
        exemplar.setPreco(dto.getPreco());
        exemplar.setQuantidade(dto.getQuantidade());
        exemplar.setDataEntrada(LocalDateTime.now());

        return exemplar;
    }

    public static ExemplarResponseDto toResponse(Exemplar exemplar) {

        ExemplarResponseDto dto = new ExemplarResponseDto();

        dto.setIdExemplar(exemplar.getIdExemplar());
        dto.setPreco(exemplar.getPreco());
        dto.setQuantidade(exemplar.getQuantidade());


        if (exemplar.getLivro() != null) {
            LivroResumoDto livroDto = new LivroResumoDto();

            livroDto.setId(exemplar.getLivro().getIdLivro());
            livroDto.setTitulo(exemplar.getLivro().getTitulo());

            if (exemplar.getLivro().getAutor() != null) {
                livroDto.setNomeAutor(exemplar.getLivro().getAutor().getNome());
            }

            dto.setLivro(livroDto);
        }


        if (exemplar.getCondicao() != null) {
            CondicaoDto condicaoDto = new CondicaoDto();

            condicaoDto.setId(exemplar.getCondicao().getIdCondicao());
            condicaoDto.setNome(exemplar.getCondicao().getNomeCondicao());

            dto.setCondicao(condicaoDto);
        }

        return dto;
    }

    public static List<ExemplarResponseDto> toList(List<Exemplar> lista) {

        List<ExemplarResponseDto> resposta = new ArrayList<>();

        for (Exemplar exemplar : lista) {
            resposta.add(toResponse(exemplar));
        }

        return resposta;
    }
}