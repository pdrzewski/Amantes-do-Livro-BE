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
import schoo.sptech.be_amante_livro.dto.AutorResponseDto;
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
   @DisplayName("Deve testar metodo de cadastrar")
    class cadastrar {

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
   }
}
