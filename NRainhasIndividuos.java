import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

// Implementa a interface Individuo para o problema das N-Rainhas
// Esta classe representa um único indivíduo na população do AG. No nosso caso, um indivíduo é uma solução candidata para o problema das N-Rainhas.
public class NRainhasIndividuos implements Individuo {

    // --- Atributos ---
    private int[] genes; // genótipo do indivíduo - Posição (linha) da rainha em cada coluna (índice)
    private int tamanhoTabuleiro;       // Tamanho do tabuleiro. Se for 8 é 8x8
    private double avaliacao = -1; // Cache do fitness (número de ataques)
    private static final Random random = new Random(); // Gerador aleatório compartilhado
    private static final double TAXA_MUTACAO = 0.20; // Probabilidade de mutação por gene

    // --- Construtor Principal (Indivíduo Aleatório) ---
    public NRainhasIndividuos(int tamanhoTabuleiro) {
        if (tamanhoTabuleiro <= 0) {
            throw new IllegalArgumentException("N deve ser maior que 0.");
        }
        this.tamanhoTabuleiro = tamanhoTabuleiro;
        this.genes = new int[tamanhoTabuleiro];
        // Preenche com posições de linha aleatórias para cada coluna
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            this.genes[i] = random.nextInt(tamanhoTabuleiro);
        }
    }

    // --- Construtor Privado (Para uso interno - recombinar/mutar) ---
    private NRainhasIndividuos(int[] genes) {
        this.tamanhoTabuleiro = genes.length;
        this.genes = genes;
    }

    // --- Getters ---
    public int getTamanhoTabuleiro() {
        return tamanhoTabuleiro;
    }

    public int[] getGenes() {
        // Retorna cópia para segurança
        return Arrays.copyOf(this.genes, this.tamanhoTabuleiro);
    }

    // --- Métodos da Interface Individuo ---

    /**
     * Calcula o número de pares de rainhas que se atacam (Fitness).
     * Menor valor é melhor (0 = solução perfeita).
     * @return Número de ataques.
     */
    @Override
    public double getAvaliacao() {
        if (this.avaliacao != -1) { // Usa cache. Se já calculado, retorna o valor armazenado
            return this.avaliacao;
        }

        int ataques = 0; // conta o número total de pares de rainhas que se atacam.
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            for (int j = i + 1; j < tamanhoTabuleiro; j++) { // j = i + 1: Isso é importante para garantir que cada par de rainhas seja verificado apenas uma vez (evita verificar o par (A,B) e depois (B,A)) e também impede que uma rainha seja comparada consigo mesma.
                // Ataque na Linha
                if (genes[i] == genes[j]) {
                    ataques++;
                }
                // Ataque na Diagonal
                if (Math.abs(genes[i] - genes[j]) == Math.abs(i - j)) {
                    ataques++;
                }
            }
        }
        this.avaliacao = ataques; // Guarda no cache
        return this.avaliacao;
    }

    /**
     * Realiza Crossover de Um Ponto com outro indivíduo.
     * @param outroIndividuo O segundo pai.
     * @return Lista com dois novos indivíduos (filhos).
     */
    @Override
    public List<Individuo> recombinar(Individuo outroIndividuo) {
        if ( // verifica se o outro indivíduo é do tipo NRainhas e se seu tabuleiro é do mesmo tamanho
            !(outroIndividuo instanceof NRainhasIndividuos) ||
             ((NRainhasIndividuos) outroIndividuo).getTamanhoTabuleiro() != this.tamanhoTabuleiro
            ) {
            throw new IllegalArgumentException("Indivíduo incompatível para recombinação.");
        }

        NRainhasIndividuos parceiro = (NRainhasIndividuos) outroIndividuo;
        int[] genesParceiro = parceiro.genes;
        int[] genesFilho1 = new int[tamanhoTabuleiro];
        int[] genesFilho2 = new int[tamanhoTabuleiro];
        // O crossover de um ponto só faz sentido se houver pelo menos dois genes para "cortar" entre eles. O ponto de corte define após qual gene o corte será feito.
        int pontoCorte = (tamanhoTabuleiro > 1) ? random.nextInt(tamanhoTabuleiro - 1) + 1 : 0;
        // onde a troca ocorre
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            if (i < pontoCorte) {
                // copiando os genes do parceiro1 para filho1, sem crossingover
                genesFilho1[i] = this.genes[i];
                genesFilho2[i] = genesParceiro[i];
            } else {
                // aqui ocorre as manipulações. O filho1 recebe gene do parceiro2 e o filho2 recebe gene do parceiro1
                genesFilho1[i] = genesParceiro[i];
                genesFilho2[i] = this.genes[i];
            }
        }

        List<Individuo> filhos = new ArrayList<>();
        filhos.add(new NRainhasIndividuos(genesFilho1));
        filhos.add(new NRainhasIndividuos(genesFilho2));
        return filhos;
    }

    /**
     * Cria um NOVO indivíduo que é uma cópia mutada do atual.
     * @return O indivíduo mutante.
     * O objetivo principal do operador de mutação em um algoritmo genético é introduzir pequenas variações aleatórias nos indivíduos de uma população. Isso ajuda a:
    Manter a diversidade genética, evitando que a população se torne muito homogênea rapidamente.
    Explorar novas áreas do espaço de busca, potencialmente ajudando o algoritmo a escapar de ótimos locais (soluções boas, mas não as melhores globalmente).
    Reintroduzir características genéticas que podem ter sido perdidas em gerações anteriores.
    Este método mutar() cria um novo indivíduo que é uma cópia do indivíduo original (this), mas com a possibilidade de algumas de suas características (genes) terem sido alteradas aleatoriamente. O indivíduo original não é modificado.
     */
    @Override
    public Individuo mutar() {
        int[] genesMutante = Arrays.copyOf(this.genes, this.tamanhoTabuleiro); // Trabalha na cópia, nao vamos alterar os genes do pai
        boolean mutacaoOcorreu = false;

        // Tenta mutar cada gene com base na taxa
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            if (random.nextDouble() < TAXA_MUTACAO) {
                // ta dentro da taxa, entao deve ocorrer a mutação
                mutacaoOcorreu = true;
                int valorAtual = genesMutante[i];
                int novoValor;
                do { // Garante que o novo valor seja diferente do valor atual
                    novoValor = random.nextInt(tamanhoTabuleiro);
                } while (tamanhoTabuleiro > 1 && novoValor == valorAtual);
                genesMutante[i] = novoValor;
            }
        }

        // Garante pelo menos uma mutação se nenhuma ocorreu aleatoriamente
        if (!mutacaoOcorreu && tamanhoTabuleiro > 0) {
             int indiceMutacaoForcada = random.nextInt(tamanhoTabuleiro);
             int valorAtual = genesMutante[indiceMutacaoForcada];
             int novoValor;
             do { // mesma função que o do while acima
                 novoValor = random.nextInt(tamanhoTabuleiro);
             } while (tamanhoTabuleiro > 1 && novoValor == valorAtual);
             genesMutante[indiceMutacaoForcada] = novoValor;
        }

        return new NRainhasIndividuos(genesMutante);
    }

    // --- Método Utilitário ---
    @Override
    public String toString() {
        // Retorna string com avaliação e genes para fácil visualização
        return "Ataques: " + getAvaliacao() + ", Genes: " + Arrays.toString(this.genes);
    }

    // --- Implementação da Factory (Classe Interna Estática) ---
    /**
     * Fábrica para criar instâncias aleatórias de NRainhasIndividuos.
     * Implementa a interface IndividuoFactory.
     */
    public static class NRainhasFactory implements IndividuoFactory {
        private int tamanhoTabuleiro; // Tamanho do tabuleiro para os indivíduos a serem criados

        // Construtor da fábrica
        public NRainhasFactory(int tamanhoTabuleiro) {
             if (tamanhoTabuleiro <= 0) {
                throw new IllegalArgumentException("N da fábrica deve ser maior que 0.");
            }
            this.tamanhoTabuleiro = tamanhoTabuleiro;
        }

        /**
         * Cria e retorna uma nova instância aleatória de NRainhasIndividuos.
         * @return Um novo Indivíduo aleatório.
         */
        @Override
        public Individuo getInstance() {
            // Chama o construtor público da classe externa NRainhasIndividuos
            return new NRainhasIndividuos(tamanhoTabuleiro);
        }
    }

    // --- Main para Teste Rápido (Opcional) ---
    public static void main(String[] args) {
        int n = 8;
        IndividuoFactory factory = new NRainhasFactory(n);
        Individuo ind1 = factory.getInstance();
        System.out.println("Indivíduo Aleatório: " + ind1);
        System.out.println("Avaliação: " + ind1.getAvaliacao());

        Individuo mutante = ind1.mutar();
        System.out.println("Mutante: " + mutante);

        Individuo ind2 = factory.getInstance();
        System.out.println("Outro Indivíduo: " + ind2);
        List<Individuo> filhos = ind1.recombinar(ind2);
        System.out.println("Filho 1 (crossover): " + filhos.get(0));
        System.out.println("Filho 2 (crossover): " + filhos.get(1));
    }
}