package schoo.sptech.be_amante_livro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Condicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCondicao;

    private String nomeCondicao;

    // 👇 TEM QUE TER ISSO
    public Integer getIdCondicao() {
        return idCondicao;
    }

    public void setIdCondicao(Integer idCondicao) {
        this.idCondicao = idCondicao;
    }

    public String getNomeCondicao() {
        return nomeCondicao;
    }

    public void setNomeCondicao(String nomeCondicao) {
        this.nomeCondicao = nomeCondicao;
    }
}