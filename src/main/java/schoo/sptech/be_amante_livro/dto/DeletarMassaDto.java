package schoo.sptech.be_amante_livro.dto;

import java.util.List;

public record DeletarMassaDto(
        int totalLinhas,
        int sucesso,
        int errosCount,
        List<String> erros
) {}