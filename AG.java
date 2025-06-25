import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AG {

    public List<Individuo> executar(IndividuoFactory factory, int nGer, int nInd, int nElite) {

        List<Individuo> populacao = new ArrayList<>(nInd);
        for (int i = 0; i < nInd; i++) {
            populacao.add(factory.getIndividuo());
        }

        double melhorAvaliacaoGeral = Double.POSITIVE_INFINITY;
        int geracoesSemMelhoria = 0;
        final int CONVERGENCIA = 200;

        for (int ger = 0; ger < nGer; ger++) {

            List<Individuo> filhos = getFilhos(populacao);
            List<Individuo> mutantes = getMutantes(populacao);
            List<Individuo> popAux = new ArrayList<>(populacao.size() + filhos.size() + mutantes.size());
            popAux.addAll(populacao);
            popAux.addAll(filhos);
            popAux.addAll(mutantes);

            populacao = selecao(popAux, nInd, nElite);
            
            imprimirMelhor(populacao, ger);
            
            Individuo melhorDaGeracao = populacao.get(0);

            // --- Início da lógica de parar na convergência ---
            if (melhorDaGeracao.getAvaliacao() < melhorAvaliacaoGeral) {
                melhorAvaliacaoGeral = melhorDaGeracao.getAvaliacao();
                geracoesSemMelhoria = 0;
            } else {
                geracoesSemMelhoria++;
            }

            if (geracoesSemMelhoria >= CONVERGENCIA) {
                System.out.println("\n--- CONVERGÊNCIA ATINGIDA ---");
                System.out.println("O algoritmo parou na geração " + ger + " por não apresentar melhorias nas últimas " + CONVERGENCIA + " gerações.");
                break;
            }
            // --- Fim da lógica de parada ---

            if (melhorDaGeracao.getAvaliacao() == 0) {
                System.out.println("\nSOLUÇÃO ÓTIMA ENCONTRADA!");
                break;
            }
        }

        return populacao;
    }

    private void imprimirMelhor(List<Individuo> populacao, int ger) {
        Individuo melhor = populacao.get(0);
        System.out.println("Geração: " + ger + " | " + melhor.toString());
    }

    private List<Individuo> selecao(List<Individuo> popAux, int nInd, int nElite) {
        List<Individuo> newPop = new ArrayList<>();

        Individuo ind0 = popAux.get(0);
        boolean minimizacao = ind0.isMinimizacao();

        popAux.sort(new Comparator<Individuo>() {
            @Override
            public int compare(Individuo o1, Individuo o2) {
                if (minimizacao) {
                    return Double.compare(o1.getAvaliacao(), o2.getAvaliacao());
                } else {
                    return Double.compare(o2.getAvaliacao(), o1.getAvaliacao());
                }
            }
        });

        for (int i = 0; i < nElite; i++) {
            newPop.add(popAux.get(i));
        }
        
        List<Individuo> roletaList = new ArrayList<>(popAux.subList(nElite, popAux.size()));
        Random rd = new Random();
        int nSelecionar = nInd - nElite;

        for (int i = 0; i < nSelecionar; i++) {
            if (roletaList.isEmpty()) break;

            double somaAvaliacoes = 0;
            if (minimizacao) {
                for (Individuo ind : roletaList) {
                    somaAvaliacoes += 1.0 / (ind.getAvaliacao() + 1.0);
                }
            } else {
                for (Individuo ind : roletaList) {
                    somaAvaliacoes += ind.getAvaliacao();
                }
            }

            double valorSorteado = rd.nextDouble() * somaAvaliacoes;
            double somaParcial = 0;
            Individuo selecionado = null;

            for (Individuo ind : roletaList) {
                double fitness = minimizacao ? (1.0 / (ind.getAvaliacao() + 1.0)) : ind.getAvaliacao();
                somaParcial += fitness;
                if (somaParcial >= valorSorteado) {
                    selecionado = ind;
                    break;
                }
            }
            if (selecionado == null) {
                selecionado = roletaList.get(roletaList.size() - 1);
            }
            
            newPop.add(selecionado);
            roletaList.remove(selecionado);
        }
        return newPop;
    }

    private List<Individuo> getMutantes(List<Individuo> populacao) {
        List<Individuo> mutantesList = new ArrayList<>();
        for (Individuo pai : populacao) {
            mutantesList.add(pai.mutar());
        }
        return mutantesList;
    }

    private List<Individuo> getFilhos(List<Individuo> populacao) {
        List<Individuo> filhosList = new ArrayList<>();
        List<Individuo> paisList = new ArrayList<>(populacao);
        Collections.shuffle(paisList);

        for (int i = 0; i < populacao.size() / 2; i++) {
            Individuo p1 = paisList.get(2 * i);
            Individuo p2 = paisList.get(2 * i + 1);
            
            List<Individuo> filhos = p1.recombinar(p2);
            filhosList.addAll(filhos);
        }

        return filhosList;
    }
}