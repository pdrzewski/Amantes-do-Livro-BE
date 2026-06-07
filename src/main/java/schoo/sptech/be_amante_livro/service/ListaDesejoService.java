package schoo.sptech.be_amante_livro.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schoo.sptech.be_amante_livro.dto.ListaDesejoCadastroDto;
import schoo.sptech.be_amante_livro.model.ListaDesejo;
import schoo.sptech.be_amante_livro.repository.ListaDesejoRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ListaDesejoService {

    private final ListaDesejoRepository repository;

    public ListaDesejo cadastrar(
            ListaDesejoCadastroDto dto){

        ListaDesejo listaDesejo =
                ListaDesejo.builder()
                        .nome(dto.getNome())
                        .email(dto.getEmail())
                        .isbn(dto.getIsbn())
                        .dataCadastro(LocalDateTime.now())
                        .notificado(false)
                        .build();

        return repository.save(listaDesejo);
    }
}
