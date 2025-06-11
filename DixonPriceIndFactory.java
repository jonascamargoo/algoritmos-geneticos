public class DixonPriceIndFactory implements IndividuoFactory {
    private int dimensao;
    private IndividuoReal.TipoCrossover tipoCrossover;

    public DixonPriceIndFactory(int dimensao, IndividuoReal.TipoCrossover tipoCrossover) {
        this.dimensao = dimensao;
        this.tipoCrossover = tipoCrossover;
    }

    @Override
    public Individuo getIndividuo() {
        // O domínio da função Dixon-Price é geralmente [-10, 10]
        return new DixonPriceInd(this.dimensao, this.tipoCrossover, -10, 10);
    }
}