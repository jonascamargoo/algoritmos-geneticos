package novo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AG {

    // método para executar o ag propriamente dito
    public List<Individuo> executar(IndividuoFactory factory, int nGer, int nInd, int nElite) {

        // Fazer um for para preencher uma lista com N individuos
        List<Individuo> populacaoInicial = new ArrayList<>(nInd);

        for (int ger = 0; ger < nGer; ger++) {

            // passei por parametro o conjunto de pais
            List<Individuo> filhos = getFilhos(populacaoInicial);
            List<Individuo> mutantes = getMutantes(populacaoInicial);
            List<Individuo> popAux = new ArrayList<Individuo>(nInd*3);
            popAux.addAll(populacaoInicial);
            popAux.addAll(filhos);
            popAux.addAll(mutantes);

            // para fazer pressão seletiva
            populacaoInicial = selecao(popAux, nInd, nElite);

            
            imprimirMelhor(populacaoInicial, nGer);

        }

        return null;

    }

    private void imprimirMelhor(List<Individuo> populacaoInicial, int nGer) {
        Individuo ind0 = populacaoInicial.get(0);
        boolean minimizacao = ind0.isMinimizacao();
        // Imprimir o melhor indivíduo, numero da geracao desse individuo e a avaliação dele
        // Levar em consideração se o problema é de min ou max

    }

    private List<Individuo> selecao(List<Individuo> popAux, int nInd, int nElite) {        
        List<Individuo> newPop = new ArrayList<Individuo>();

        // verificar se o problema é de minimização
        Individuo ind0 = popAux.get(0);
        boolean minimizacao = ind0.isMinimizacao();

        // -----------------------
        // Elitismo
        // -----------------------
        // se for de min, ordenar popAux de menor para maior avaliacao, caso contrário, ordenar da maior para a menor avaliacao  
        // Selecionar os nElite individuos no início do popAux (os melhores)
        // colocar os nElite individuos selecionaods na newPop.


        // -----------------------
        // Roleta viciada
        // -----------------------
        // maximização: 1 - obter a soma (soma1) das avaliações de todos os indivíduos que irão participar da roleta. 2 - Gerar um número aleatório (r) que vai de 0 ao somatório. 3 - Para selecionar um indivíduo da minha lista, vou percorrendo a lista somando (soma2) novamente a avaliação até obter um valor de soma maior que o valor aleatório (soma2 > r). 4 - retirar o indivíduo selecionado para newPop e voltar para o passo 1 (a soma deve ser refeita toda novamente)
        // minimização: 0 - inverter a avaliação de todos os indivíduos na forma (1/avaliacao) e utilizar estes novos valores para os passos 1, 2 e 3. 1 - obter a soma (soma1) das avaliações de todos os indivíduos que irão participar da roleta. 2 - Gerar um número aleatório (r) que vai de 0 ao somatório. 3 - Para selecionar um indivíduo da minha lista, vou percorrendo a lista somando (soma2) novamente a avaliação até obter um valor de soma maior que o valor aleatório (soma2 > r). 4 - retirar o indivíduo selecionado para newPop e voltar para o passo 1 (a soma deve ser refeita toda novamente)

        // OBS: realizar os passos 1, 2, 3 e 4 n vezes até completar newPop.size() = nInd

        return newPop;

    }

    private List<Individuo> getMutantes(List<Individuo> populacaoInicial) {
        // TODO percorrer a lista de pais e a cada pai obter o seu mutante, retornando uma lista de mutantes
        List<Individuo> mutantesList = new ArrayList<Individuo>();
        return mutantesList;
    }

    private List<Individuo> getFilhos(List<Individuo> populacaoInicial) {
        List<Individuo> filhosList = new ArrayList<Individuo>();

        List<Individuo> paisList = new ArrayList<Individuo>();
        paisList.addAll(populacaoInicial);
        Random rd = new Random();

        for (int i = 0; i < populacaoInicial.size()/2; i++) {
            int r1 = rd.nextInt(paisList.size());
            Individuo p1 = paisList.get(r1);
            paisList.remove(r1);

            int r2 = rd.nextInt(paisList.size());
            Individuo p2 = paisList.get(r2);
            List<Individuo> filhos = p1.recombinar(p2);
            filhosList.addAll(filhos);

        }

        return filhosList;
    }
}


// Descrição do algoritmo
// inicialmente temos 20 individuos, consequentemente 20 pais. Na primeira execução gera 20 filhos e 20 mutantes (3 listas até agora: pai, filhos e mutantes). Agora juntaremos essas três listas em uma lista auxiliar (popAux) com 60 indivíduos. Aplicaremos uma seleção que resultara em uma nova população (newPop) de 20 indivíduos (com uma pressão seletiva para gerar sempre os melhores). Por fim, esse newPop será os pais da nova geração.