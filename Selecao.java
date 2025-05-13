import java.util.List;
import java.util.ArrayList; // Usada no main de exemplo
import java.util.Random;

public class Selecao {

    private static final Random random = new Random(); // Gerador de números aleatórios

    /**
     * Seleciona UM indivíduo de uma população usando o método da Roleta Viciada.
     * A probabilidade de seleção é proporcional à avaliação (fitness) do indivíduo,
     * ajustada para problemas de minimização ou maximização.
     *
     * @param populacao Lista de objetos que implementam a interface Individuo.
     * @param ehMinimizacao true se o objetivo for minimizar o valor de getAvaliacao()
     * (como no N-Rainhas onde avaliacao = nº ataques),
     * false se for maximizar.
     * @return O Individuo selecionado da população.
     * @throws IllegalArgumentException Se a população for nula ou vazia.
     */
    public static Individuo selecionarPorRoleta(List<Individuo> populacao, boolean ehMinimizacao) {

        if (populacao == null || populacao.isEmpty()) {
            throw new IllegalArgumentException("A população não pode ser nula ou vazia.");
        }

        double somaContribuicoes = 0.0;

        // 1. Calcula a soma das contribuições. A função getAvaliacao() da interface Individuo é chamada aqui.
        for (Individuo ind : populacao) {
            // Obtém a avaliação através da interface Individuo
            double avaliacao = ind.getAvaliacao();
            somaContribuicoes += calcularContribuicao(avaliacao, ehMinimizacao);
        }

        // Validação da soma
        if (somaContribuicoes <= 0) {
             System.err.println("Aviso: Soma das contribuições na roleta é <= 0. Selecionando aleatoriamente.");
             return populacao.get(random.nextInt(populacao.size()));
        }

        // 4. Gera número aleatório na faixa da soma total
        double numeroAleatorio = random.nextDouble() * somaContribuicoes;

        // 5. Gira a roleta virtual
        double somaAcumulada = 0.0;
        for (Individuo ind : populacao) {
             // Chama getAvaliacao() novamente para calcular a contribuição individual
             // (Poderia ser otimizado armazenando as contribuições, mas assim fica mais simples)
            double contribuicao = calcularContribuicao(ind.getAvaliacao(), ehMinimizacao);
            somaAcumulada += contribuicao;

            if (somaAcumulada >= numeroAleatorio) {
                // Encontrou o indivíduo cuja "fatia" contém o número aleatório
                return ind;
            }
        }

        // Fallback (raro, mas seguro)
        return populacao.get(populacao.size() - 1);
    }

    /**
     * Calcula a contribuição para a roleta (privado).
     */
    private static double calcularContribuicao(double avaliacao, boolean ehMinimizacao) {
        if (ehMinimizacao) {
            // Para NRainhas (minimizar ataques), usamos o inverso de (1 + ataques)
            if (avaliacao < 0) {
                 System.err.println("Aviso: Avaliação negativa em minimização: " + avaliacao + ". Tratando como 0.");
                 avaliacao = 0;
             }
            return 1.0 / (1.0 + avaliacao);
        } else {
            // Para maximização, usamos a própria avaliação (garantindo não-negatividade)
            if (avaliacao < 0) {
                 System.err.println("Aviso: Avaliação negativa em maximização: " + avaliacao + ". Tratando como 0.");
                 return 0.0;
            }
            return avaliacao;
        }
    }

    // O método main de exemplo da resposta anterior pode ser mantido aqui para testar a classe Selecao isoladamente.
    // Lembre-se que ele usa uma classe anônima para simular Indivíduos.
    public static void main(String[] args) {
        // (Mesmo código do main da resposta anterior para demonstração)
        System.out.println("--- Exemplo Minimização (N-Rainhas) ---");
        List<Individuo> populacaoMin = new ArrayList<>();
        populacaoMin.add(createIndividuoTeste(4.0)); // i1 - 4 ataques
        populacaoMin.add(createIndividuoTeste(8.0)); // i2 - 8 ataques
        populacaoMin.add(createIndividuoTeste(6.0)); // i3 - 6 ataques
        populacaoMin.add(createIndividuoTeste(2.0)); // i4 - 2 ataques
        populacaoMin.add(createIndividuoTeste(0.0)); // i5 - 0 ataques (melhor)

        System.out.println("Avaliações | Contribuições (1/(1+A))");
        for(Individuo ind : populacaoMin) { System.out.printf("%.1f        | %.4f%n", ind.getAvaliacao(), calcularContribuicao(ind.getAvaliacao(), true)); }
        System.out.println("Selecionando 10 indivíduos por roleta (Minimização):");
        for (int i = 0; i < 10; i++) { Individuo selecionado = selecionarPorRoleta(populacaoMin, true); System.out.println("Seleção " + (i + 1) + ": Avaliação=" + selecionado.getAvaliacao()); }

        System.out.println("\n--- Exemplo Maximização ---");
        List<Individuo> populacaoMax = new ArrayList<>();
        populacaoMax.add(createIndividuoTeste(4.0)); // i1
        populacaoMax.add(createIndividuoTeste(8.0)); // i2 (melhor)
        populacaoMax.add(createIndividuoTeste(6.0)); // i3
        populacaoMax.add(createIndividuoTeste(2.0)); // i4

        System.out.println("Avaliações | Contribuições (A)");
         for(Individuo ind : populacaoMax) { System.out.printf("%.1f        | %.4f%n", ind.getAvaliacao(), calcularContribuicao(ind.getAvaliacao(), false)); }
        System.out.println("Selecionando 10 indivíduos por roleta (Maximização):");
        for (int i = 0; i < 10; i++) { Individuo selecionado = selecionarPorRoleta(populacaoMax, false); System.out.println("Seleção " + (i + 1) + ": Avaliação=" + selecionado.getAvaliacao()); }
    }
    // Método auxiliar do main
    private static Individuo createIndividuoTeste(double avaliacao) { return new Individuo() { private final double aval = avaliacao; @Override public List<Individuo> recombinar(Individuo i) { return null; } @Override public Individuo mutar() { return null; } @Override public double getAvaliacao() { return aval; } @Override public String toString() { return "Teste(Aval=" + aval + ")"; } }; }

}