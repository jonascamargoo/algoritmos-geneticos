public class LangermannIndFactory implements IndividuoFactory {
    private IndividuoReal.TipoCrossover tipoCrossover;

    public LangermannIndFactory(IndividuoReal.TipoCrossover tipoCrossover) {
        // A dimensão está fixa em 2 para esta implementação
        this.tipoCrossover = tipoCrossover;
    }

    @Override
    public Individuo getIndividuo() {
        // O domínio da função Langermann é geralmente [0, 10]
        return new LangermannInd(2, this.tipoCrossover, 0, 10);
    }
}