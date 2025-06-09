package novo;

import java.util.List;

public class IndividuoNRainhas extends Individuo{
    private int nRainhas;
    private int[] genes;

    public IndividuoNRainhas(int nRainhas) {
        this.nRainhas = nRainhas;
    // TODO Inicializar o tamanho do vetor de genes e seus valores de forma aleatória. Os valores aleatórios devem obedecer a ideia da permutação (nao podem se repetir).
    }

    @Override
    public List<Individuo> recombinar(Individuo p2) {
        // TODO recombinar os indivíduos self e p2 gerando 2 filhos utilizando a ideia de permutação
        return null;
    }
    @Override
    public Individuo mutar() {
        // Gerar um novo indivíduo mutante com os genes do indivíduo self mutados em uma taxa de mutação de 10% ou 5%
        throw new UnsupportedOperationException("Unimplemented method 'mutar'");
    }
    @Override
    public double avaliar() {
        // TODO realizar a contagem de colisões que ocorre entre as rainhas
        throw new UnsupportedOperationException("Unimplemented method 'avaliar'");
    }



}
