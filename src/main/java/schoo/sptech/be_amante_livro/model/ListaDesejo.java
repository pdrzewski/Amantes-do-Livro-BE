package schoo.sptech.be_amante_livro.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lista_desejo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListaDesejo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String email;

    private String isbn;

    private LocalDateTime dataCadastro;

    private Boolean notificado;
}