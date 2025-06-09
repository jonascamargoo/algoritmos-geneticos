package novo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class IndividuoNRainhas extends Individuo {
    private int nRainhas;
    private int[] genes;

    public IndividuoNRainhas(int nRainhas) {
        this.nRainhas = nRainhas;
        this.genes = new int[nRainhas];
        // Inicializa o vetor de genes com uma permutação aleatória de 0 a n-1.
        // Isso garante que não há rainhas na mesma linha ou coluna.
        List<Integer> permutacao = new ArrayList<>();
        for (int i = 0; i < nRainhas; i++) {
            permutacao.add(i);
        }
        Collections.shuffle(permutacao);
        for (int i = 0; i < nRainhas; i++) {
            this.genes[i] = permutacao.get(i);
        }
    }

    // Construtor privado para criar filhos e mutantes
    private IndividuoNRainhas(int nRainhas, int[] genes) {
        this.nRainhas = nRainhas;
        this.genes = genes;
    }

    @Override
    public List<Individuo> recombinar(Individuo p2) {
        IndividuoNRainhas pai2 = (IndividuoNRainhas) p2;
        Random rd = new Random();

        // Implementação do Order Crossover (OX1)
        int ponto1 = rd.nextInt(nRainhas);
        int ponto2 = rd.nextInt(nRainhas);

        if (ponto1 > ponto2) {
            int temp = ponto1;
            ponto1 = ponto2;
            ponto2 = temp;
        }

        // Criar filhos
        int[] genesFilho1 = new int[nRainhas];
        int[] genesFilho2 = new int[nRainhas];

        // Lógica para o Filho 1
        Set<Integer> genesP1Segmento = new HashSet<>();
        for (int i = ponto1; i <= ponto2; i++) {
            genesFilho1[i] = this.genes[i];
            genesP1Segmento.add(this.genes[i]);
        }

        int idxP2 = 0;
        for (int i = 0; i < nRainhas; i++) {
            if (i >= ponto1 && i <= ponto2) continue;
            while (genesP1Segmento.contains(pai2.genes[idxP2])) {
                idxP2++;
            }
            genesFilho1[i] = pai2.genes[idxP2];
            idxP2++;
        }
        
        // Lógica para o Filho 2
         Set<Integer> genesP2Segmento = new HashSet<>();
        for (int i = ponto1; i <= ponto2; i++) {
            genesFilho2[i] = pai2.genes[i];
            genesP2Segmento.add(pai2.genes[i]);
        }

        int idxP1 = 0;
        for (int i = 0; i < nRainhas; i++) {
            if (i >= ponto1 && i <= ponto2) continue;
            while (genesP2Segmento.contains(this.genes[idxP1])) {
                idxP1++;
            }
            genesFilho2[i] = this.genes[idxP1];
            idxP1++;
        }


        List<Individuo> filhos = new ArrayList<>();
        filhos.add(new IndividuoNRainhas(nRainhas, genesFilho1));
        filhos.add(new IndividuoNRainhas(nRainhas, genesFilho2));

        return filhos;
    }

    @Override
    public Individuo mutar() {
        Random rd = new Random();
        int[] genesMutante = this.genes.clone();

        // Mutação por troca (Swap Mutation): troca duas posições.
        int pos1 = rd.nextInt(nRainhas);
        int pos2 = rd.nextInt(nRainhas);

        // Garante que as posições sejam diferentes
        while (pos1 == pos2) {
            pos2 = rd.nextInt(nRainhas);
        }

        int temp = genesMutante[pos1];
        genesMutante[pos1] = genesMutante[pos2];
        genesMutante[pos2] = temp;

        return new IndividuoNRainhas(nRainhas, genesMutante);
    }

    @Override
    public double avaliar() {
        // A representação por permutação já elimina colisões de linha e coluna.
        // Precisamos contar apenas as colisões nas diagonais.
        int colisoes = 0;
        for (int i = 0; i < nRainhas; i++) {
            for (int j = i + 1; j < nRainhas; j++) {
                // Se a distância vertical for igual à distância horizontal,
                // elas estão na mesma diagonal.
                if (Math.abs(i - j) == Math.abs(this.genes[i] - this.genes[j])) {
                    colisoes++;
                }
            }
        }
        this.avaliado = true;
        this.avaliacao = colisoes;
        return colisoes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Avaliação: ").append(getAvaliacao()).append(" | Genes: [");
        for (int i = 0; i < nRainhas; i++) {
            sb.append(genes[i]).append(i == nRainhas - 1 ? "" : ", ");
        }
        sb.append("]");
        return sb.toString();
    }
}