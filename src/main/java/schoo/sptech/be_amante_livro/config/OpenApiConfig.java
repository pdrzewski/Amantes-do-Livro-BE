package schoo.sptech.be_amante_livro.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "cookieAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Back-end Amantes do Livro")
                        .description("API REST para gestão completa de estoque de livros. " +
                                "Permite o cadastro e controle de livros, autores, editoras, exemplares e condições. " +
                                "Possui integração com Google Books para consulta por ISBN, importação e deleção em massa via CSV, " +
                                "controle de estoque (baixa) e autenticação via JWT em cookie. " +
                                "Desenvolvida com Spring Boot , Java ,  e Spring Data JPA.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("")
                                .email("")
                                )
                        .license(new License()
                                .name("MIT License")
                                .url("")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor local de desenvolvimento"));
               
    }
}

