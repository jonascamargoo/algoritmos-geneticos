import java.util.List;

public interface Individuo {
    public List<Individuo> recombinar(Individuo individuo);
    public Individuo mutar();
    public double getAvaliacao();
    public int[] getGenes();
}