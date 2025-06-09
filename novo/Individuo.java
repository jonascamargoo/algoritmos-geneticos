package novo;

import java.util.List;

public abstract class Individuo {
    public boolean avaliado = false;
    public boolean minimizacao = true; // no caso da NRainhas, queremos minimizar as colisoes, por isso é minimização
    public double avaliacao;// quando temos um problema de mini, quanto menor o retorno da função de avaliacao, melhor o indivíduo, consequentemente melhor seu fitness

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
