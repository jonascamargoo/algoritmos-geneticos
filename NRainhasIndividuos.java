import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class NRainhasIndividuos implements Individuo {

    private int[] genes;
    private int tamanhoTabuleiro; 
    private double avaliacao = -1; 
    private static final Random random = new Random(); 
    private static final double TAXA_MUTACAO = 0.20; 

    public NRainhasIndividuos(int tamanhoTabuleiro) {
        if (tamanhoTabuleiro <= 0) {
            throw new IllegalArgumentException("N deve ser maior que 0.");
        }
        this.tamanhoTabuleiro = tamanhoTabuleiro;
        this.genes = new int[tamanhoTabuleiro];
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            this.genes[i] = random.nextInt(tamanhoTabuleiro);
        }
    }

    private NRainhasIndividuos(int[] genes) {
        this.tamanhoTabuleiro = genes.length;
        this.genes = genes;
    }

    public int getTamanhoTabuleiro() {
        return tamanhoTabuleiro;
    }

    public int[] getGenes() {
        return Arrays.copyOf(this.genes, this.tamanhoTabuleiro);
    }

    @Override
    public double getAvaliacao() {
        if (this.avaliacao != -1) {
            return this.avaliacao;
        }

        int ataques = 0;
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            for (int j = i + 1; j < tamanhoTabuleiro; j++) {
                if (genes[i] == genes[j]) {
                    ataques++;
                }
                if (Math.abs(genes[i] - genes[j]) == Math.abs(i - j)) {
                    ataques++;
                }
            }
        }
        this.avaliacao = ataques;
        return this.avaliacao;
    }


    @Override
    public List<Individuo> recombinar(Individuo outroIndividuo) {
        if (
            !(outroIndividuo instanceof NRainhasIndividuos) ||
             ((NRainhasIndividuos) outroIndividuo).getTamanhoTabuleiro() != this.tamanhoTabuleiro
            ) {
            throw new IllegalArgumentException("Indivíduo incompatível para recombinação.");
        }

        NRainhasIndividuos parceiro = (NRainhasIndividuos) outroIndividuo;
        int[] genesParceiro = parceiro.genes;
        int[] genesFilho1 = new int[tamanhoTabuleiro];
        int[] genesFilho2 = new int[tamanhoTabuleiro];

        int pontoCorte = (tamanhoTabuleiro > 1) ? random.nextInt(tamanhoTabuleiro - 1) + 1 : 0;
        for (int i = 0; i < tamanhoTabuleiro; i++) {
            if (i < pontoCorte) {
                genesFilho1[i] = this.genes[i];
                genesFilho2[i] = genesParceiro[i];
            } else {
                genesFilho1[i] = genesParceiro[i];
                genesFilho2[i] = this.genes[i];
            }
        }

        List<Individuo> filhos = new ArrayList<>();
        filhos.add(new NRainhasIndividuos(genesFilho1));
        filhos.add(new NRainhasIndividuos(genesFilho2));
        return filhos;
    }

    @Override
    public Individuo mutar() {
        int[] genesMutante = Arrays.copyOf(this.genes, this.tamanhoTabuleiro);
        boolean mutacaoOcorreu = false;

        for (int i = 0; i < tamanhoTabuleiro; i++) {
            if (random.nextDouble() < TAXA_MUTACAO) {
                mutacaoOcorreu = true;
                int valorAtual = genesMutante[i];
                int novoValor;
                do {
                    novoValor = random.nextInt(tamanhoTabuleiro);
                } while (tamanhoTabuleiro > 1 && novoValor == valorAtual);
                genesMutante[i] = novoValor;
            }
        }

        if (!mutacaoOcorreu && tamanhoTabuleiro > 0) {
             int indiceMutacaoForcada = random.nextInt(tamanhoTabuleiro);
             int valorAtual = genesMutante[indiceMutacaoForcada];
             int novoValor;
             do {
                 novoValor = random.nextInt(tamanhoTabuleiro);
             } while (tamanhoTabuleiro > 1 && novoValor == valorAtual);
             genesMutante[indiceMutacaoForcada] = novoValor;
        }

        return new NRainhasIndividuos(genesMutante);
    }

    @Override
    public String toString() {
        return "Ataques: " + getAvaliacao() + ", Genes: " + Arrays.toString(this.genes);
    }

    public static class NRainhasFactory implements IndividuoFactory {
        private int tamanhoTabuleiro;

        public NRainhasFactory(int tamanhoTabuleiro) {
             if (tamanhoTabuleiro <= 0) {
                throw new IllegalArgumentException("N da fábrica deve ser maior que 0.");
            }
            this.tamanhoTabuleiro = tamanhoTabuleiro;
        }

        @Override
        public Individuo getInstance() {
            return new NRainhasIndividuos(tamanhoTabuleiro);
        }
    }

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