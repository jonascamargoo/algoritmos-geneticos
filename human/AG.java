package human;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AG {

    // método para executar o ag propriamente dito
    public List<Individuo> executar(IndividuoFactory factory, int nGer, int nInd, int nElite) {

        // Preenche a lista com N individuos iniciais
        List<Individuo> populacao = new ArrayList<>(nInd);
        for (int i = 0; i < nInd; i++) {
            populacao.add(factory.getIndividuo());
        }

        for (int ger = 0; ger < nGer; ger++) {

            // passei por parametro o conjunto de pais
            List<Individuo> filhos = getFilhos(populacao);
            List<Individuo> mutantes = getMutantes(populacao);
            List<Individuo> popAux = new ArrayList<Individuo>(populacao.size() + filhos.size() + mutantes.size());
            popAux.addAll(populacao);
            popAux.addAll(filhos);
            popAux.addAll(mutantes);

            // para fazer pressão seletiva
            populacao = selecao(popAux, nInd, nElite);
            
            imprimirMelhor(populacao, ger);

            // Critério de parada: se encontrou a solução (0 colisões)
            if (populacao.get(0).getAvaliacao() == 0) {
                System.out.println("\nSOLUÇÃO ÓTIMA ENCONTRADA!");
                break;
            }
        }

        return populacao;
    }

    private void imprimirMelhor(List<Individuo> populacao, int ger) {
        Individuo melhor = populacao.get(0);
        // Imprimir o melhor indivíduo, numero da geracao desse individuo e a avaliação dele
        System.out.println("Geração: " + ger + " | Melhor Avaliação (Colisões): " + melhor.getAvaliacao() + " | " + melhor.toString());
    }

    private List<Individuo> selecao(List<Individuo> popAux, int nInd, int nElite) {
        List<Individuo> newPop = new ArrayList<Individuo>();

        // verificar se o problema é de minimização
        Individuo ind0 = popAux.get(0);
        boolean minimizacao = ind0.isMinimizacao();

        // Ordena a população auxiliar.
        // Se for minimização, ordena do menor para o maior.
        // Se for maximização, ordena do maior para o menor.
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

        // -----------------------
        // Elitismo
        // -----------------------
        for (int i = 0; i < nElite; i++) {
            newPop.add(popAux.get(i));
        }
        
        // Remove os indivíduos de elite da população auxiliar para não participarem da roleta
        List<Individuo> roletaList = new ArrayList<>(popAux.subList(nElite, popAux.size()));

        // -----------------------
        // Roleta viciada
        // -----------------------
        Random rd = new Random();
        int nSelecionar = nInd - nElite;

        for (int i = 0; i < nSelecionar; i++) {
            double somaAvaliacoes = 0;
            // Para minimização, inverte-se o valor da avaliação para que os menores tenham maior "fatia"
            if (minimizacao) {
                for (Individuo ind : roletaList) {
                    // Adiciona 1 para evitar divisão por zero se a avaliação for 0
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
            // Fallback para caso de imprecisão de ponto flutuante
            if(selecionado == null){
                selecionado = roletaList.get(roletaList.size() - 1);
            }
            
            newPop.add(selecionado);
            roletaList.remove(selecionado); // Remove para não ser selecionado novamente
        }

        return newPop;
    }

    private List<Individuo> getMutantes(List<Individuo> populacao) {
        List<Individuo> mutantesList = new ArrayList<Individuo>();
        for(Individuo pai : populacao) {
            mutantesList.add(pai.mutar());
        }
        return mutantesList;
    }

    private List<Individuo> getFilhos(List<Individuo> populacao) {
        List<Individuo> filhosList = new ArrayList<Individuo>();

        List<Individuo> paisList = new ArrayList<Individuo>();
        paisList.addAll(populacao);
        Collections.shuffle(paisList); // Embaralha para tornar a seleção de pares aleatória
        Random rd = new Random();

        for (int i = 0; i < populacao.size() / 2; i++) {
            Individuo p1 = paisList.get(2 * i);
            Individuo p2 = paisList.get(2 * i + 1);
            
            List<Individuo> filhos = p1.recombinar(p2);
            filhosList.addAll(filhos);
        }

        return filhosList;
    }
}