import java.util.List;
import java.util.ArrayList;
import java.util.Arrays; // Para Arrays.toString()
import java.util.Random;

public class MotorAG {

    private int nRainhas;
    private int tamanhoPopulacao;
    private int numeroMaxGeracoes;
    private double taxaDeCrossover;
    private IndividuoFactory factory;
    private static final Random random = new Random();

    public MotorAG(int nRainhas, int tamanhoPopulacao, int numeroMaxGeracoes, double taxaDeCrossover) {
        this.nRainhas = nRainhas;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroMaxGeracoes = numeroMaxGeracoes;
        this.taxaDeCrossover = taxaDeCrossover;
        this.factory = new NRainhasIndividuos.NRainhasFactory(nRainhas);
    }

    private Individuo encontrarMelhorIndividuo(List<Individuo> populacao) {
        if (populacao == null || populacao.isEmpty()) {
            return null;
        }

        Individuo melhorIndividuo = populacao.get(0);
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


    public void executar() {
        List<Individuo> populacao = new ArrayList<>();
        for (int i = 0; i < tamanhoPopulacao; i++) {
            populacao.add(factory.getInstance());
        }

        Individuo melhorGlobal = null;
        boolean solucaoEncontrada = false;

        for (int geracao = 0; geracao < numeroMaxGeracoes; geracao++) {
            for (Individuo ind : populacao) {
                ind.getAvaliacao();
            }

            Individuo melhorDaGeracao = encontrarMelhorIndividuo(populacao);
            imprimirStatusDaGeracao(melhorDaGeracao, geracao);

            if (melhorGlobal == null || (melhorDaGeracao != null && melhorDaGeracao.getAvaliacao() < melhorGlobal.getAvaliacao())) {
                melhorGlobal = melhorDaGeracao;
            }

            if (melhorDaGeracao != null && melhorDaGeracao.getAvaliacao() == 0) {
                System.out.println("\nSOLUÇÃO PERFEITA ENCONTRADA NA GERAÇÃO " + (geracao + 1) + "!");
                solucaoEncontrada = true;
                break;
            }

            List<Individuo> novaPopulacao = new ArrayList<>();

            if (melhorDaGeracao != null) {
                novaPopulacao.add(melhorDaGeracao);
            }


            while (novaPopulacao.size() < tamanhoPopulacao) {
                Individuo pai1 = Selecao.selecionarPorRoleta(populacao, true);
                Individuo pai2 = Selecao.selecionarPorRoleta(populacao, true);

                Individuo filho1, filho2;

                if (pai1 != null && pai2 != null && random.nextDouble() < this.taxaDeCrossover) {
                    List<Individuo> filhos = pai1.recombinar(pai2);
                    filho1 = filhos.get(0);
                    filho2 = (filhos.size() > 1) ? filhos.get(1) : filhos.get(0).mutar();
                } else {
                    filho1 = (pai1 != null) ? pai1 : factory.getInstance();
                    filho2 = (pai2 != null) ? pai2 : factory.getInstance();
                }

                if (filho1 != null) {
                    novaPopulacao.add(filho1.mutar());
                }
                if (novaPopulacao.size() < tamanhoPopulacao && filho2 != null) {
                    novaPopulacao.add(filho2.mutar());
                }
                if (pai1 == null && pai2 == null && novaPopulacao.size() < tamanhoPopulacao) {
                    novaPopulacao.add(factory.getInstance().mutar());
                }
            }
            while (novaPopulacao.size() > tamanhoPopulacao) {
                novaPopulacao.remove(novaPopulacao.size() - 1);
            }
            populacao = novaPopulacao;
        }

        System.out.println("\n--- Fim da Execução do AG ---");

        if (!solucaoEncontrada) {
            System.out.println("Não convergiu para uma solução perfeita após " + numeroMaxGeracoes + " gerações.");
            if (melhorGlobal != null) {
                System.out.println("Melhor solução encontrada durante a execução:");
                imprimirStatusDaGeracao(melhorGlobal, numeroMaxGeracoes -1);
            } else {
                 System.out.println("Nenhuma solução válida foi processada.");
            }
        } else if (melhorGlobal != null) {
             System.out.println("Melhor Indivíduo Global Encontrado (Solução Perfeita):");
             imprimirStatusDaGeracao(melhorGlobal, numeroMaxGeracoes-1);
        }
    }


    public static void main(String[] args) {
        int nRainhas = 8;
        int tamanhoPopulacao = 20;
        int numeroMaxGeracoes = 2000;
        double taxaDeCrossover = 0.85;

        System.out.println("Iniciando AG para o Problema das " + nRainhas + "-Rainhas...");
        System.out.println("Tamanho da População: " + tamanhoPopulacao);
        System.out.println("Número Máximo de Gerações: " + numeroMaxGeracoes);
        System.out.println("Taxa de Crossover: " + taxaDeCrossover);
        System.out.println("Taxa de Mutação (por gene, na classe NRainhasIndividuos): " + 0.20);
        System.out.println("----------------------------------------------");

        MotorAG ag = new MotorAG(nRainhas, tamanhoPopulacao, numeroMaxGeracoes, taxaDeCrossover);
        ag.executar();
    }
}