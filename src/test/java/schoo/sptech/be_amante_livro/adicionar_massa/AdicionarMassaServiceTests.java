package schoo.sptech.be_amante_livro.adicionar_massa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import schoo.sptech.be_amante_livro.dto.AdicionarMassaDto;
import schoo.sptech.be_amante_livro.model.Autor;
import schoo.sptech.be_amante_livro.model.Condicao;
import schoo.sptech.be_amante_livro.model.Editora;
import schoo.sptech.be_amante_livro.model.Livro;
import schoo.sptech.be_amante_livro.repository.*;
import schoo.sptech.be_amante_livro.service.AdicionarMassaService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdicionarMassaServiceTests {

    @Mock private AutorRepository autorRepository;
    @Mock private EditoraRepository editoraRepository;
    @Mock private LivroRepository livroRepository;
    @Mock private ExemplarRepository exemplarRepository;
    @Mock private CondicaoRepository condicaoRepository;

    @InjectMocks
    private AdicionarMassaService service;

    @Test
    void importarEstanteVirtual_deveRetornarSucessoQuandoCsvValido() {
        Autor autor = new Autor();
        Editora editora = new Editora();
        Livro livro = new Livro(); livro.setIdLivro(1);
        Condicao condicao = new Condicao(); condicao.setIdCondicao(1); condicao.setNomeCondicao("Novo");

        when(autorRepository.findByNomeIgnoreCase(any())).thenReturn(Optional.of(autor));
        when(editoraRepository.findByNomeEditoraIgnoreCase(any())).thenReturn(Optional.of(editora));
        when(livroRepository.findByIsbn(any())).thenReturn(Optional.of(livro));
        when(condicaoRepository.findOptionalByNomeCondicaoIgnoreCase("Novo")).thenReturn(Optional.of(condicao));
        when(exemplarRepository.findByLivro_IdLivroAndCondicao_IdCondicao(1, 1)).thenReturn(Optional.empty());
        when(exemplarRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        String conteudo = "isbn;titulo;autor;editora;ano;condicao;preco;extra\n" +
                "978-3-16-148410-0;Clean Code;Robert Martin;Prentice Hall;2008;Novo;49,90;x\n";
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "estante.csv", "text/csv",
                conteudo.getBytes(StandardCharsets.UTF_8));

        AdicionarMassaDto dto = service.importarEstanteVirtual(arquivo);

        assertThat(dto.getTotalLinhas()).isEqualTo(1);
        assertThat(dto.getSucesso()).isEqualTo(1);
        assertThat(dto.getErros()).isEmpty();
    }

    @Test
    void importarEstanteVirtual_deveLancarExcecaoQuandoArquivoVazio() {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "vazio.csv", "text/csv", new byte[0]);

        assertThatThrownBy(() -> service.importarEstanteVirtual(arquivo))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Arquivo CSV não pode estar vazio");
    }
}