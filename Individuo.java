import java.util.List;

// indivíduo é a olução candidata para o NRainhas
public interface Individuo {
    // Método para gerar filhos combinando com outro indivíduo
    public List<Individuo> recombinar(Individuo individuo);

    // Método para gerar uma versão mutada de si mesmo
    public Individuo mutar();

    // Método ESSENCIAL para a seleção: retorna a "qualidade" do indivíduo
    // Para NRainhas, isso retornará o número de ataques (menor é melhor) - calcula o número de pares de rainhas que se atacam. Como o objetivo é ter zero ataques, esta é uma função de custo (ou "inaptidão"). O AG buscará indivíduos com o menor valor retornado por getAvaliacao()
    public double getAvaliacao();

    public int[] getGenes();
}