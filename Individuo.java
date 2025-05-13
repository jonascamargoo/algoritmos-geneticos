import java.util.List;

public interface Individuo {
    // Método para gerar filhos combinando com outro indivíduo
    public List<Individuo> recombinar(Individuo individuo);

    // Método para gerar uma versão mutada de si mesmo
    public Individuo mutar();

    // Método ESSENCIAL para a seleção: retorna a "qualidade" do indivíduo
    // Para NRainhas, isso retornará o número de ataques (menor é melhor)
    public double getAvaliacao();
}