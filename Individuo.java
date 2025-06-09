import java.util.List;

// A ideia de ter interface indivíduos é que podemos utilizar para mais de um problema de AG. Ainda dentro do mesmo problema, podemos ter operações diferentes.
public interface Individuo {
    public List<Individuo> recombinar(Individuo individuo);
    public Individuo mutar();
    public double getAvaliacao();
    public int[] getGenes();

}




// public interface Individual {
//     List<Individual> crossover(Individual partner);
//     Individual mutate();
//     double getFitness();
//     int[] getGenome();
// }
