import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random; // Se precisar de aleatoriedade no motor


public class MotorAG {

    private int nRainhas;
    private int tamanhoPopulacao;
    private int numeroMaxGeracoes;
    private double taxaDeCrossover; // Probabilidade de realizar crossover
    private IndividuoFactory factory;
    private static final Random random = new Random(); // Para decisões no motor, como taxa de crossover

    public MotorAG(int nRainhas, int tamanhoPopulacao, int numeroMaxGeracoes, double taxaDeCrossover) {
        this.nRainhas = nRainhas;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroMaxGeracoes = numeroMaxGeracoes;
        this.taxaDeCrossover = taxaDeCrossover;
        this.factory = new NRainhasIndividuos.NRainhasFactory(nRainhas);
    }

    /**
     * Encontra o melhor indivíduo (menor avaliação) em uma população.
     * @param populacao A lista de indivíduos.
     * @return O melhor indivíduo encontrado.
     */
    private Individuo encontrarMelhorIndividuo(List<Individuo> populacao) {
        if (populacao == null || populacao.isEmpty()) {
            return null;
        }

        Individuo melhorIndividuo = populacao.get(0);
        // Garante que a avaliação inicial seja calculada
        double melhorAvaliacao = melhorIndividuo.getAvaliacao();

        for (int i = 1; i < populacao.size(); i++) {
            Individuo candidato = populacao.get(i);
            double avaliacaoCandidato = candidato.getAvaliacao();
            if (avaliacaoCandidato < melhorAvaliacao) {
                melhorAvaliacao = avaliacaoCandidato;
                melhorIndividuo = candidato;
            }
        }
        return melhorIndividuo;
    }

    /**
     * Imprime as informações do melhor indivíduo da geração.
     * @param melhor O melhor indivíduo.
     * @param geracao O número da geração atual (começando em 0).
     */
    private void imprimirStatusDaGeracao(Individuo melhor, int geracao) {
        if (melhor != null) {
            System.out.println(
                "Geração: " + (geracao + 1) + // Adiciona 1 para exibição (1-indexed)
                " | Melhor Indivíduo - Genes: " + Arrays.toString(melhor.getGenes()) +
                " | Colisões (Avaliação): " + melhor.getAvaliacao()
            );
        } else {
            System.out.println("Geração: " + (geracao + 1) + " | População vazia.");
        }
    }


    /**
     * Executa o Algoritmo Genético.
     */
    public void executar() {
        // 1. Inicialização da População
        List<Individuo> populacao = new ArrayList<>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            populacao.add(factory.getInstance());
        }

        Individuo melhorGlobal = null; // Para rastrear o melhor de todas as gerações

        // Loop das Gerações
        for (int geracao = 0; geracao < numeroMaxGeracoes; geracao++) {
            // Avaliar todos (garantir que a avaliação está calculada e cacheada)
            for (Individuo ind : populacao) {
                ind.getAvaliacao();
            }

            // Encontrar e imprimir o melhor da geração ATUAL
            Individuo melhorDaGeracao = encontrarMelhorIndividuo(populacao);
            imprimirStatusDaGeracao(melhorDaGeracao, geracao);

            // Atualizar melhor global
            if (melhorGlobal == null || (melhorDaGeracao != null && melhorDaGeracao.getAvaliacao() < melhorGlobal.getAvaliacao())) {
                melhorGlobal = melhorDaGeracao; // Aqui guardamos a referência, o que é ok.
                                              // Se quiséssemos um clone: new NRainhasIndividuos(melhorDaGeracao.getGenes());
            }

            // Critério de parada (ex: solução perfeita encontrada)
            if (melhorDaGeracao != null && melhorDaGeracao.getAvaliacao() == 0) {
                System.out.println("\nSOLUÇÃO PERFEITA ENCONTRADA NA GERAÇÃO " + (geracao + 1) + "!");
                break;
            }

            // Criação da Próxima Geração
            List<Individuo> novaPopulacao = new ArrayList<>();

            // Elitismo: Opcional, mas comum. Adicionar o melhor indivíduo diretamente.
            if (melhorDaGeracao != null) {
                // Adicionar um clone para evitar modificações se o mesmo objeto for selecionado para crossover
                // Para NRainhasIndividuos, precisaríamos de um construtor que aceita genes ou um método clone.
                // Supondo que NRainhasIndividuos tem um construtor que clona os genes ou que seja seguro
                // adicionar a mesma instância se não houver modificação direta nos pais selecionados.
                // Como `mutar` e `recombinar` em NRainhasIndividuos criam NOVOS objetos, é seguro adicionar `melhorDaGeracao`.
                novaPopulacao.add(melhorDaGeracao);
            }


            // Preencher o restante da nova população com filhos
            while (novaPopulacao.size() < tamanhoPopulacao) {
                // 3. Seleção dos Pais (usando a Roleta Viciada)
                Individuo pai1 = Selecao.selecionarPorRoleta(populacao, true); // true para minimização
                Individuo pai2 = Selecao.selecionarPorRoleta(populacao, true);

                Individuo filho1, filho2;

                // 4. Crossover
                if (random.nextDouble() < this.taxaDeCrossover && pai1 != null && pai2 != null) {
                    List<Individuo> filhos = pai1.recombinar(pai2);
                    filho1 = filhos.get(0);
                    filho2 = (filhos.size() > 1) ? filhos.get(1) : filhos.get(0).mutar(); // se só um filho, cria o segundo
                } else { // Sem crossover, os pais (ou clones deles) passam adiante
                    filho1 = pai1; // Ou um clone de pai1
                    filho2 = pai2; // Ou um clone de pai2
                }

                // 5. Mutação (aplicada nos filhos resultantes do crossover ou nos pais que passaram direto)
                if (filho1 != null) novaPopulacao.add(filho1.mutar());
                if (novaPopulacao.size() < tamanhoPopulacao && filho2 != null) {
                    novaPopulacao.add(filho2.mutar());
                }
            }
            // Garante que a população não exceda o tamanho máximo devido ao elitismo e loop
             while (novaPopulacao.size() > tamanhoPopulacao) {
                novaPopulacao.remove(novaPopulacao.size() - 1); // Remove excedentes
            }


            populacao = novaPopulacao; // A nova geração se torna a população atual
        }

        System.out.println("\n--- Fim da Execução do AG ---");
        if (melhorGlobal != null) {
             System.out.println("Melhor Indivíduo Global Encontrado:");
             imprimirStatusDaGeracao(melhorGlobal, numeroMaxGeracoes); // Reusa o método de impressão
        } else {
            System.out.println("Nenhum indivíduo válido encontrado durante a execução.");
        }
    }


    // --- Ponto de Entrada ---
    public static void main(String[] args) {
        // Parâmetros do AG
        int nRainhas = 8;          // Tamanho do tabuleiro
        int tamanhoPopulacao = 100;  // Número de indivíduos na população
        int numeroMaxGeracoes = 200; // Critério de parada
        double taxaDeCrossover = 0.85; // Probabilidade de pais cruzarem

        System.out.println("Iniciando AG para o Problema das " + nRainhas + "-Rainhas...");
        System.out.println("Tamanho da População: " + tamanhoPopulacao);
        System.out.println("Número Máximo de Gerações: " + numeroMaxGeracoes);
        System.out.println("Taxa de Crossover: " + taxaDeCrossover);
        System.out.println("Taxa de Mutação (por gene, na classe NRainhasIndividuos): " + 0.20); // Valor fixo em NRainhasIndividuos
        System.out.println("----------------------------------------------");


        MotorAG ag = new MotorAG(nRainhas, tamanhoPopulacao, numeroMaxGeracoes, taxaDeCrossover);
        ag.executar();
    }
}