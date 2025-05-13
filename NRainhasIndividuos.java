import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

// Implementa a interface Individuo para o problema das N-Rainhas
// Esta classe representa um único indivíduo na população do AG. No nosso caso, um indivíduo é uma solução candidata para o problema das N-Rainhas.
public class NRainhasIndividuos implements Individuo {

    // --- Atributos ---
    private int[] genes; // Posição (linha) da rainha em cada coluna (índice)
    private int n;       // Tamanho do tabuleiro
    private double avaliacao = -1; // Cache do fitness (número de ataques)
    private static final Random random = new Random(); // Gerador aleatório compartilhado
    private static final double TAXA_MUTACAO = 0.20; // Probabilidade de mutação por gene

    // --- Construtor Principal (Indivíduo Aleatório) ---
    public NRainhasIndividuos(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N deve ser maior que 0.");
        }
        this.n = n;
        this.genes = new int[n];
        // Preenche com posições de linha aleatórias para cada coluna
        for (int i = 0; i < n; i++) {
            this.genes[i] = random.nextInt(n);
        }
    }

    // --- Construtor Privado (Para uso interno - recombinar/mutar) ---
    private NRainhasIndividuos(int[] genes) {
        this.n = genes.length;
        this.genes = genes;
    }

    // --- Getters ---
    public int getN() {
        return n;
    }

    public int[] getGenes() {
        // Retorna cópia para segurança
        return Arrays.copyOf(this.genes, this.n);
    }

    // --- Métodos da Interface Individuo ---

    /**
     * Calcula o número de pares de rainhas que se atacam (Fitness).
     * Menor valor é melhor (0 = solução perfeita).
     * @return Número de ataques.
     */
    @Override
    public double getAvaliacao() {
        if (this.avaliacao != -1) { // Usa cache
            return this.avaliacao;
        }

        int ataques = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
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
        if (!(outroIndividuo instanceof NRainhasIndividuos) || ((NRainhasIndividuos) outroIndividuo).getN() != this.n) {
            throw new IllegalArgumentException("Indivíduo incompatível para recombinação.");
        }

        NRainhasIndividuos parceiro = (NRainhasIndividuos) outroIndividuo;
        int[] genesParceiro = parceiro.genes;
        int[] genesFilho1 = new int[n];
        int[] genesFilho2 = new int[n];
        int pontoCorte = (n > 1) ? random.nextInt(n - 1) + 1 : 0;

        for (int i = 0; i < n; i++) {
            if (i < pontoCorte) {
                genesFilho1[i] = this.genes[i];
                genesFilho2[i] = genesParceiro[i];
            } else {
                genesFilho1[i] = genesParceiro[i];
                genesFilho2[i] = this.genes[i];
            }
        }

        List<Individuo> filhos = new ArrayList<>();
        // Usa o construtor privado para criar os filhos
        filhos.add(new NRainhasIndividuos(genesFilho1));
        filhos.add(new NRainhasIndividuos(genesFilho2));
        return filhos;
    }

    /**
     * Cria um NOVO indivíduo que é uma cópia mutada do atual.
     * @return O indivíduo mutante.
     */
    @Override
    public Individuo mutar() {
        int[] genesMutante = Arrays.copyOf(this.genes, this.n); // Trabalha na cópia
        boolean mutacaoOcorreu = false;

        // Tenta mutar cada gene com base na taxa
        for (int i = 0; i < n; i++) {
            if (random.nextDouble() < TAXA_MUTACAO) {
                mutacaoOcorreu = true;
                int valorAtual = genesMutante[i];
                int novoValor;
                do { // Garante que o novo valor seja diferente
                    novoValor = random.nextInt(n);
                } while (n > 1 && novoValor == valorAtual);
                genesMutante[i] = novoValor;
            }
        }

        // Garante pelo menos uma mutação se nenhuma ocorreu aleatoriamente
        if (!mutacaoOcorreu && n > 0) {
             int indiceMutacaoForcada = random.nextInt(n);
             int valorAtual = genesMutante[indiceMutacaoForcada];
             int novoValor;
             do {
                 novoValor = random.nextInt(n);
             } while (n > 1 && novoValor == valorAtual);
             genesMutante[indiceMutacaoForcada] = novoValor;
        }

        // Usa o construtor privado para criar o mutante
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
        private int n; // Tamanho do tabuleiro para os indivíduos a serem criados

        // Construtor da fábrica
        public NRainhasFactory(int n) {
             if (n <= 0) {
                throw new IllegalArgumentException("N da fábrica deve ser maior que 0.");
            }
            this.n = n;
        }

        /**
         * Cria e retorna uma nova instância aleatória de NRainhasIndividuos.
         * @return Um novo Indivíduo aleatório.
         */
        @Override
        public Individuo getInstance() {
            // Chama o construtor público da classe externa NRainhasIndividuos
            return new NRainhasIndividuos(n);
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