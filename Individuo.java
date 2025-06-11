import java.util.List;

public abstract class Individuo {
    public boolean avaliado = false;
    public boolean minimizacao = true;
    public double avaliacao;

    public abstract List<Individuo> recombinar(Individuo p2);
    public abstract Individuo mutar();
    public abstract double avaliar();

    public double getAvaliacao() {
        if(!avaliado) {
            avaliacao = avaliar();
        }
        return avaliacao;
    }
    
    public boolean isMinimizacao() {
        return minimizacao;
    }
}
