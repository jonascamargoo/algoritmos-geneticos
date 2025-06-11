import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe base abstrata para indivíduos com representação de valor real.
 * Implementa os operadores genéticos comuns (Crossover e Mutação) para este domínio.
 */
public abstract class IndividuoReal extends Individuo {

    public enum TipoCrossover {
        ARITMETICO,
        BLX_ALPHA
    }

    protected double[] genes;
    protected int dimensao;
    protected TipoCrossover tipoCrossover;

    /**
     * Construtor para criar um indivíduo aleatório dentro de um intervalo.
     */
    public IndividuoReal(int dimensao, TipoCrossover tipoCrossover, double minRange, double maxRange) {
        this.minimizacao = true; // Funções de benchmark são de minimização
        this.dimensao = dimensao;
        this.tipoCrossover = tipoCrossover;
        this.genes = new double[dimensao];
        Random rd = new Random();
        for (int i = 0; i < dimensao; i++) {
            this.genes[i] = minRange + (maxRange - minRange) * rd.nextDouble();
        }
    }

    /**
     * Construtor para criar um indivíduo a partir de genes pré-definidos (filhos).
     */
    protected IndividuoReal(double[] genes, int dimensao, TipoCrossover tipoCrossover) {
        this.minimizacao = true;
        this.dimensao = dimensao;
        this.tipoCrossover = tipoCrossover;
        this.genes = genes;
    }

    @Override
    public List<Individuo> recombinar(Individuo p2) {
        if (this.tipoCrossover == TipoCrossover.BLX_ALPHA) {
            return recombinarBlxAlpha(p2);
        }
        return recombinarAritmetico(p2);
    }

    private List<Individuo> recombinarAritmetico(Individuo p2) {
        IndividuoReal pai2 = (IndividuoReal) p2;
        Random rd = new Random();
        double alpha = rd.nextDouble(); // Alpha aleatório para o crossover aritmético

        double[] genesFilho1 = new double[dimensao];
        double[] genesFilho2 = new double[dimensao];

        for (int i = 0; i < dimensao; i++) {
            double genePai1 = this.genes[i];
            double genePai2 = pai2.genes[i];

            // Filho1 = α * P1 + (1-α) * P2
            genesFilho1[i] = alpha * genePai1 + (1 - alpha) * genePai2;
            // Filho2 = (1-α) * P1 + α * P2
            genesFilho2[i] = (1 - alpha) * genePai1 + alpha * genePai2;
        }

        List<Individuo> filhos = new ArrayList<>();
        filhos.add(criarFilho(genesFilho1));
        filhos.add(criarFilho(genesFilho2));
        return filhos;
    }

    private List<Individuo> recombinarBlxAlpha(Individuo p2) {
        IndividuoReal pai2 = (IndividuoReal) p2;
        Random rd = new Random();
        final double alpha = 0.5;

        double[] genesFilho1 = new double[dimensao];
        double[] genesFilho2 = new double[dimensao];

        for (int i = 0; i < dimensao; i++) {
            double genePai1 = this.genes[i];
            double genePai2 = pai2.genes[i];
            double distancia = Math.abs(genePai1 - genePai2);
            double min = Math.min(genePai1, genePai2) - distancia * alpha;
            double max = Math.max(genePai1, genePai2) + distancia * alpha;
            genesFilho1[i] = min + rd.nextDouble() * (max - min);
            genesFilho2[i] = min + rd.nextDouble() * (max - min);
        }

        List<Individuo> filhos = new ArrayList<>();
        filhos.add(criarFilho(genesFilho1));
        filhos.add(criarFilho(genesFilho2));
        return filhos;
    }

    @Override
    public Individuo mutar() {
        Random rd = new Random();
        double[] genesMutante = this.genes.clone();
        final double sigma = 0.5; // Desvio padrão para a mutação

        for (int i = 0; i < dimensao; i++) {
            double valorGaussiano = rd.nextGaussian() * sigma;
            genesMutante[i] += valorGaussiano;
        }
        return criarFilho(genesMutante);
    }
    
    // Método abstrato que força as subclasses a criarem uma instância de si mesmas.
    // Isso é necessário para que a recombinação e mutação possam gerar filhos do tipo correto.
    public abstract Individuo criarFilho(double[] genes);

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Avaliação: %.6f | Genes: [", getAvaliacao()));
        for (int i = 0; i < dimensao; i++) {
            sb.append(String.format("%.4f", genes[i]));
            if (i < dimensao - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}