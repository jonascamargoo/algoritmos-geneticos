public class DixonPriceInd extends IndividuoReal {

    public DixonPriceInd(int dimensao, TipoCrossover tipoCrossover, double minRange, double maxRange) {
        super(dimensao, tipoCrossover, minRange, maxRange);
    }

    private DixonPriceInd(double[] genes, int dimensao, TipoCrossover tipoCrossover) {
        super(genes, dimensao, tipoCrossover);
    }

    @Override
    public Individuo criarFilho(double[] genes) {
        return new DixonPriceInd(genes, this.dimensao, this.tipoCrossover);
    }

    @Override
    public double avaliar() {
        double[] xx = this.genes;
        int d = this.dimensao;

        if (d == 0) return 0;

        double term1 = Math.pow(xx[0] - 1, 2);
        double sum = 0;
        for (int i = 1; i < d; i++) {
            double xi = xx[i];
            double xold = xx[i - 1];
            sum += (i + 1) * Math.pow(2 * Math.pow(xi, 2) - xold, 2);
        }

        this.avaliacao = term1 + sum;
        this.avaliado = true;
        return this.avaliacao;
    }
}