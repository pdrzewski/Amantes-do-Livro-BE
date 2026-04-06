package schoo.sptech.be_amante_livro.dto;



    public class ExemplarResponseDto {

        private Integer idExemplar;
        private Double preco;
        private Integer quantidade;
        private LivroResumoDto livro;
        private CondicaoDto condicao;


        public Integer getIdExemplar() {
            return idExemplar;
        }

        public void setIdExemplar(Integer idExemplar) {
            this.idExemplar = idExemplar;
        }

        public Double getPreco() {
            return preco;
        }

        public void setPreco(Double preco) {
            this.preco = preco;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }

        public LivroResumoDto getLivro() {
            return livro;
        }

        public void setLivro(LivroResumoDto livro) {
            this.livro = livro;
        }

        public CondicaoDto getCondicao() {
            return condicao;
        }

        public void setCondicao(CondicaoDto condicao) {
            this.condicao = condicao;
        }
    }
