package schoo.sptech.be_amante_livro.autor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import schoo.sptech.be_amante_livro.dto.AutorRequestDto;
import schoo.sptech.be_amante_livro.dto.AutorResponseDto;
import schoo.sptech.be_amante_livro.exception.AutorNaoEncontradoException;
import schoo.sptech.be_amante_livro.model.Autor;
import schoo.sptech.be_amante_livro.repository.AutorRepository;
import schoo.sptech.be_amante_livro.service.AutorService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AutorServiceTests {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    @Nested
    @DisplayName("Deve testar metodo de buscar")
    class buscar {

       @Test
       @DisplayName("Deve buscar autor por ID com sucesso")
       void deveBuscarAutorPorIdComSucessoTest() {
           Integer id = 1;

           Autor autor = new Autor();
           autor.setIdAutor(id);
           autor.setNome("Robert C. Martin");

           Mockito.when(autorRepository.findById(id)).thenReturn(Optional.of(autor));

           AutorResponseDto resultado = autorService.buscarPorId(id);

           Assertions.assertNotNull(resultado);
           Assertions.assertEquals(id, resultado.getIdAutor());
           Assertions.assertEquals("Robert C. Martin", resultado.getNome());
           Mockito.verify(autorRepository, Mockito.times(1)).findById(id);
       }

       @Test
       @DisplayName("Deve lançar exceção quando autor não encontrado pelo ID")
       void deveLancarExcecaoQuandoAutorNaoEncontradoPorIdTest() {
           Integer id = 99;

           Mockito.when(autorRepository.findById(id)).thenReturn(Optional.empty());

           Assertions.assertThrows(AutorNaoEncontradoException.class, () -> autorService.buscarPorId(id));
           Mockito.verify(autorRepository, Mockito.times(1)).findById(id);
       }
   }

    @Nested
    @DisplayName("deve testar o método de cadastrar")
    class cadastrar {

        @Test
        @DisplayName("deve cadastrar autor com sucesso")
        void deveCadastrarAutorComSucessoTest() {
            AutorRequestDto dto = new AutorRequestDto();
            dto.setNome("Robert C. Martin");

            Autor autor = new Autor();
            autor.setNome("Robert C. Martin");

            Autor salvo = new Autor();
            salvo.setIdAutor(1);
            salvo.setNome("Robert C. Martin");

            Mockito.when(autorRepository.save(Mockito.any())).thenReturn(salvo);

            AutorResponseDto resultado = autorService.cadastrar(dto);

            Assertions.assertNotNull(resultado);
            Assertions.assertEquals(1, resultado.getIdAutor());
            Assertions.assertEquals("Robert C. Martin", resultado.getNome());
            Mockito.verify(autorRepository, Mockito.times(1)).save(Mockito.any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando save retorna nulo")
        void deveLancarExcecaoQuandoSaveRetornaNuloTest() {
            AutorRequestDto dto = new AutorRequestDto();
            dto.setNome("Robert C. Martin");

            Mockito.when(autorRepository.save(Mockito.any())).thenReturn(null);

            Assertions.assertThrows(NullPointerException.class, () -> autorService.cadastrar(dto));
            Mockito.verify(autorRepository, Mockito.times(1)).save(Mockito.any());
        }
    }
}
