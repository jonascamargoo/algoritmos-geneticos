package ai;
import java.util.List;
import java.util.ArrayList; 
import java.util.Random;


public class Selecao {

    private static final Random random = new Random(); 

    public static Individuo selecionarPorRoleta(List<Individuo> populacao, boolean ehMinimizacao) {

        if (populacao == null || populacao.isEmpty()) {
            throw new IllegalArgumentException("A população não pode ser nula ou vazia.");
        }

        double somaContribuicoes = 0.0;

        for (Individuo ind : populacao) {
            double avaliacao = ind.getAvaliacao();
            somaContribuicoes += calcularContribuicao(avaliacao, ehMinimizacao);
        }

        if (somaContribuicoes <= 0) {
             System.err.println("Aviso: Soma das contribuições na roleta é <= 0. Selecionando aleatoriamente.");
             return populacao.get(random.nextInt(populacao.size()));
        }

        double numeroAleatorio = random.nextDouble() * somaContribuicoes;

        double somaAcumulada = 0.0;
        for (Individuo ind : populacao) {
            double contribuicao = calcularContribuicao(ind.getAvaliacao(), ehMinimizacao);
            somaAcumulada += contribuicao;

            if (somaAcumulada >= numeroAleatorio) {
                return ind;
            }
        }

        return populacao.get(populacao.size() - 1);
    }

    private static double calcularContribuicao(double avaliacao, boolean ehMinimizacao) {
        if (ehMinimizacao) {
            if (avaliacao < 0) {
                 System.err.println("Aviso: Avaliação negativa em minimização: " + avaliacao + ". Tratando como 0.");
                 avaliacao = 0;
             }
            return 1.0 / (1.0 + avaliacao);
        } else {
            if (avaliacao < 0) {
                 System.err.println("Aviso: Avaliação negativa em maximização: " + avaliacao + ". Tratando como 0.");
                 return 0.0;
            }
            return avaliacao;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Exemplo Minimização (N-Rainhas) ---");
        List<Individuo> populacaoMin = new ArrayList<>();
        populacaoMin.add(createIndividuoTeste(4.0)); 
        populacaoMin.add(createIndividuoTeste(8.0)); 
        populacaoMin.add(createIndividuoTeste(6.0));
        populacaoMin.add(createIndividuoTeste(2.0)); 
        populacaoMin.add(createIndividuoTeste(0.0));

        System.out.println("Avaliações | Contribuições (1/(1+A))");
        for(Individuo ind : populacaoMin) { System.out.printf("%.1f        | %.4f%n", ind.getAvaliacao(), calcularContribuicao(ind.getAvaliacao(), true)); }
        System.out.println("Selecionando 10 indivíduos por roleta (Minimização):");
        for (int i = 0; i < 10; i++) { Individuo selecionado = selecionarPorRoleta(populacaoMin, true); System.out.println("Seleção " + (i + 1) + ": Avaliação=" + selecionado.getAvaliacao()); }

        System.out.println("\n--- Exemplo Maximização ---");
        List<Individuo> populacaoMax = new ArrayList<>();
        populacaoMax.add(createIndividuoTeste(4.0));
        populacaoMax.add(createIndividuoTeste(8.0));
        populacaoMax.add(createIndividuoTeste(6.0));
        populacaoMax.add(createIndividuoTeste(2.0));

        System.out.println("Avaliações | Contribuições (A)");
         for(Individuo ind : populacaoMax) { System.out.printf("%.1f        | %.4f%n", ind.getAvaliacao(), calcularContribuicao(ind.getAvaliacao(), false)); }
        System.out.println("Selecionando 10 indivíduos por roleta (Maximização):");
        for (int i = 0; i < 10; i++) { Individuo selecionado = selecionarPorRoleta(populacaoMax, false); System.out.println("Seleção " + (i + 1) + ": Avaliação=" + selecionado.getAvaliacao()); }
    }
    private static Individuo createIndividuoTeste(double avaliacao) { return new Individuo() { private final double aval = avaliacao; @Override public List<Individuo> recombinar(Individuo i) { return null; } @Override public Individuo mutar() { return null; } @Override public double getAvaliacao() { return aval; } @Override public String toString() { return "Teste(Aval=" + aval + ")"; }

    @Override
    public int[] getGenes() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGenes'");
    } }; }

}