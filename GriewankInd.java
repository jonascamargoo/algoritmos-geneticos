public class GriewankInd extends IndividuoReal {

    public GriewankInd(int dimensao, TipoCrossover tipoCrossover, double minRange, double maxRange) {
        super(dimensao, tipoCrossover, minRange, maxRange);
    }

    private GriewankInd(double[] genes, int dimensao, TipoCrossover tipoCrossover) {
        super(genes, dimensao, tipoCrossover);
    }
    
    @Override
    public Individuo criarFilho(double[] genes) {
        return new GriewankInd(genes, this.dimensao, this.tipoCrossover);
    }

    @Override
    public double avaliar() {
        double[] xx = this.genes;
        int d = this.dimensao;
        double sum = 0;
        double prod = 1;

        for (int i = 0; i < d; i++) {
            double xi = xx[i];
            sum += Math.pow(xi, 2) / 4000.0;
            prod *= Math.cos(xi / Math.sqrt(i + 1));
        }

        this.avaliacao = sum - prod + 1;
        this.avaliado = true;
        return this.avaliacao;
    }
}