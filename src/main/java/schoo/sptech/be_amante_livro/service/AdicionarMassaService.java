package schoo.sptech.be_amante_livro.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import schoo.sptech.be_amante_livro.dto.AdicionarMassaDto;
import schoo.sptech.be_amante_livro.model.*;
import schoo.sptech.be_amante_livro.repository.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdicionarMassaService {

    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final EditoraRepository editoraRepository;
    private final CondicaoRepository condicaoRepository;
    private final ExemplarRepository exemplarRepository;



    public AdicionarMassaService(LivroRepository livroRepository,
                            AutorRepository autorRepository,
                            EditoraRepository editoraRepository,
                            CondicaoRepository condicaoRepository,
                            ExemplarRepository exemplarRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.editoraRepository = editoraRepository;
        this.condicaoRepository = condicaoRepository;
        this.exemplarRepository = exemplarRepository;
    }

    public AdicionarMassaDto importarEstanteVirtual(MultipartFile arquivo) {

        if (arquivo.isEmpty()) {
            throw new RuntimeException("Arquivo CSV não pode estar vazio");
        }

        List<String> erros = new ArrayList<>();
        int sucesso = 0;
        int totalLinhas = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(arquivo.getInputStream(), StandardCharsets.UTF_8))) {

            String linha;
            boolean primeiraLinha = true;

            while ((linha = reader.readLine()) != null) {

                if (primeiraLinha) { primeiraLinha = false; continue; }
                if (linha.isBlank()) continue;

                totalLinhas++;

                try {
                    String[] col = linha.split(";", -1);

                    if (col.length < 8) {
                        throw new IllegalArgumentException(
                                "Esperado 8 colunas, encontrado: " + col.length
                        );
                    }

                    String isbn            = col[0].trim();
                    String titulo          = col[1].trim();
                    String nomeAutor       = col[2].trim();
                    String nomeEditora     = col[3].trim();
                    int anoPublicacao      = Integer.parseInt(col[4].trim());
                    String nomeCondicao    = col[5].trim();
                    Double preco = Double.parseDouble(col[6].trim().replace(",", "."));

                    if (isbn.isBlank() || titulo.isBlank() || nomeAutor.isBlank() || nomeEditora.isBlank()) {
                        throw new IllegalArgumentException("ISBN, título, autor e editora são obrigatórios");
                    }

                    if (preco <= 0) {
                        throw new IllegalArgumentException("Preço deve ser maior que zero");
                    }

                    Autor autor = autorRepository.findByNomeIgnoreCase(nomeAutor)
                            .orElseGet(() -> {
                                Autor novo = new Autor();
                                novo.setNome(nomeAutor);
                                return autorRepository.save(novo);
                            });

                    Editora editora = editoraRepository.findByNomeEditoraIgnoreCase(nomeEditora)
                            .orElseGet(() -> {
                                Editora nova = new Editora();
                                nova.setNomeEditora(nomeEditora);
                                return editoraRepository.save(nova);
                            });

                    Livro livro = livroRepository.findByIsbn(isbn)
                            .orElseGet(() -> {
                                Livro novo = new Livro();
                                novo.setIsbn(isbn);
                                novo.setTitulo(titulo);
                                novo.setAnoPublicacao(anoPublicacao);
                                novo.setAutor(autor);
                                novo.setEditora(editora);
                                return livroRepository.save(novo);
                            });

                    Condicao condicao = resolverCondicao(nomeCondicao);

                    Optional<Exemplar> exemplarExistente = exemplarRepository
                            .findByLivro_IdLivroAndCondicao_IdCondicao(livro.getIdLivro(), condicao.getIdCondicao());

                    if (exemplarExistente.isPresent()) {
                        Exemplar exemplar = exemplarExistente.get();
                        exemplar.setQuantidade(exemplar.getQuantidade() + 1);
                        exemplarRepository.save(exemplar);
                    } else {
                        Exemplar exemplar = new Exemplar();
                        exemplar.setLivro(livro);
                        exemplar.setCondicao(condicao);
                        exemplar.setPreco(preco);
                        exemplar.setQuantidade(1);
                        exemplarRepository.save(exemplar);
                    }
                    sucesso++;

                } catch (NumberFormatException e) {
                    erros.add("Linha " + totalLinhas + ": valor numérico inválido → " + linha);
                } catch (IllegalArgumentException e) {
                    erros.add("Linha " + totalLinhas + ": " + e.getMessage());
                } catch (Exception e) {
                    erros.add("Linha " + totalLinhas + ": erro inesperado → " + e.getMessage());
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler o arquivo CSV: " + e.getMessage());
        }

        return new AdicionarMassaDto(totalLinhas, sucesso, erros.size(), erros);
    }

    private Condicao resolverCondicao(String nomeCondicao) {

        String normalizado = switch (nomeCondicao.toLowerCase().trim()) {
            case "novo", "new"                  -> "Novo";
            case "seminovo", "semi-novo",
                 "semi novo", "used - good"     -> "Seminovo";
            case "usado", "used", "old"         -> "Usado";
            default -> nomeCondicao;
        };

        return condicaoRepository.findOptionalByNomeCondicaoIgnoreCase(normalizado)
                .orElseGet(() -> {
                    Condicao nova = new Condicao();
                    nova.setNomeCondicao(normalizado);
                    return condicaoRepository.save(nova);
                });
    }
}