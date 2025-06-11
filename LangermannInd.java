public class LangermannInd extends IndividuoReal {
    // Parâmetros fixos para a função Langermann (m=5, d=2)
    private static final int m = 5;
    private static final double[] c = {1, 2, 5, 2, 3};
    private static final double[][] A = {
        {3, 5},
        {5, 2},
        {2, 1},
        {1, 4},
        {7, 9}
    };

    public LangermannInd(int dimensao, TipoCrossover tipoCrossover, double minRange, double maxRange) {
        super(dimensao, tipoCrossover, minRange, maxRange);
        if (dimensao != 2) {
            throw new IllegalArgumentException("A implementação da função Langermann está fixa para dimensão d=2.");
        }
    }

    private LangermannInd(double[] genes, int dimensao, TipoCrossover tipoCrossover) {
        super(genes, dimensao, tipoCrossover);
    }
    
    @Override
    public Individuo criarFilho(double[] genes) {
        return new LangermannInd(genes, this.dimensao, this.tipoCrossover);
    }

    @Override
    public double avaliar() {
        double[] xx = this.genes;
        double outer = 0;

        for (int i = 0; i < m; i++) {
            double inner = 0;
            for (int j = 0; j < this.dimensao; j++) {
                inner += Math.pow(xx[j] - A[i][j], 2);
            }
            outer += c[i] * Math.exp(-inner / Math.PI) * Math.cos(Math.PI * inner);
        }
        
        // A função de Langermann é normalmente maximizada. Para usar nosso AG de minimização,
        // retornamos o valor negativo.
        this.avaliacao = -outer; 
        this.avaliado = true;
        return this.avaliacao;
    }
}