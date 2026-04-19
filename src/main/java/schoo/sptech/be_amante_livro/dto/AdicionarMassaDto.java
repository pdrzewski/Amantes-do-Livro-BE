package schoo.sptech.be_amante_livro.dto;

import java.util.List;

public class AdicionarMassaDto {

    private int totalLinhas;
    private int sucesso;
    private int falhas;
    private List<String> erros;

    public AdicionarMassaDto(int totalLinhas, int sucesso, int falhas, List<String> erros) {
        this.totalLinhas = totalLinhas;
        this.sucesso = sucesso;
        this.falhas = falhas;
        this.erros = erros;
    }

    public int getTotalLinhas() { return totalLinhas; }
    public int getSucesso() { return sucesso; }
    public int getFalhas() { return falhas; }
    public List<String> getErros() { return erros; }
}