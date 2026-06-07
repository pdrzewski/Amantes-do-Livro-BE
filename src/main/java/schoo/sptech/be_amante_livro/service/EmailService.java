package schoo.sptech.be_amante_livro.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarLivroDisponivel(
            String email,
            String nome,
            String tituloLivro) {

        SimpleMailMessage mensagem =
                new SimpleMailMessage();

        mensagem.setTo(email);

        mensagem.setSubject(
                "Livro disponível!"
        );

        mensagem.setText(
                "Olá " + nome +
                        "\n\nO livro \"" + tituloLivro +
                        "\" que você adicionou à lista de desejos acabou de chegar ao estoque."
        );

        mailSender.send(mensagem);
    }
}
