public class LangermannIndFactory implements IndividuoFactory {
    private IndividuoReal.TipoCrossover tipoCrossover;

    public LangermannIndFactory(IndividuoReal.TipoCrossover tipoCrossover) {
        this.tipoCrossover = tipoCrossover;
    }

    @Override
    public Individuo getIndividuo() {
        // O domínio da função Langermann é geralmente [0, 10]
        return new LangermannInd(2, this.tipoCrossover, 0, 10);
    }
}