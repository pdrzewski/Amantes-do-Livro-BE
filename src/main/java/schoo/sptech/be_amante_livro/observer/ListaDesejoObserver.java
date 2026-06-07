package schoo.sptech.be_amante_livro.observer;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import schoo.sptech.be_amante_livro.events.LivroChegouEvent;
import schoo.sptech.be_amante_livro.model.ListaDesejo;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.ListaDesejoRepository;
import schoo.sptech.be_amante_livro.repository.LivroRepository;
import schoo.sptech.be_amante_livro.service.EmailService;

import java.util.List;

@Component
public class ListaDesejoObserver {

    private final ListaDesejoRepository listaDesejoRepository;
    private final LivroRepository livroRepository;
    private final EmailService emailService;

    public ListaDesejoObserver(
            ListaDesejoRepository listaDesejoRepository,
            LivroRepository livroRepository,
            EmailService emailService) {

        this.listaDesejoRepository = listaDesejoRepository;
        this.livroRepository = livroRepository;
        this.emailService = emailService;
    }

    @EventListener
    public void processarLivroChegou(
            LivroChegouEvent event) {

        String isbn = event.getIsbn();

        List<ListaDesejo> interessados =
                listaDesejoRepository
                        .findByIsbnAndNotificadoFalse(isbn);

        if (interessados.isEmpty()) {
            return;
        }

        Livro livro =
                livroRepository
                        .findByIsbn(isbn)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Livro não encontrado para ISBN: " + isbn
                                ));

        for (ListaDesejo interessado : interessados) {
            try {
                emailService.enviarLivroDisponivel(
                        interessado.getEmail(),
                        interessado.getNome(),
                        livro.getTitulo()
                );

                interessado.setNotificado(true);

            } catch (Exception e) {
                System.out.println(
                        "Erro ao enviar email para "
                                + interessado.getEmail()
                );
                e.printStackTrace();
            }
        }

        listaDesejoRepository.saveAll(interessados);
    }
}