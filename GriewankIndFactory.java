public class GriewankIndFactory implements IndividuoFactory {
    private int dimensao;
    private IndividuoReal.TipoCrossover tipoCrossover;

    public GriewankIndFactory(int dimensao, IndividuoReal.TipoCrossover tipoCrossover) {
        this.dimensao = dimensao;
        this.tipoCrossover = tipoCrossover;
    }

    @Override
    public Individuo getIndividuo() {
        // O domínio da função Griewank é geralmente [-600, 600]
        return new GriewankInd(this.dimensao, this.tipoCrossover, -600, 600);
    }
}