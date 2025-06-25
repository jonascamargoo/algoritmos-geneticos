public class AGRunner {

    public static void main(String[] args) {
        final int N_GER = 2000;
        final int N_IND = 20;
        final int N_ELITE = 4;

        // --- ESCOLHA A FUNÇÃO E O CROSSOVER AQUI ---

        // Exemplo 1: Otimizando a sua função, GRIEWANK (d=10), com Crossover BLX-ALPHA
        // System.out.println("--- OTIMIZANDO FUNÇÃO GRIEWANK (BLX-ALPHA) ---");
        // IndividuoFactory griewankFactoryBlx = new GriewankIndFactory(10, IndividuoReal.TipoCrossover.BLX_ALPHA);
        // executarAG(griewankFactoryBlx, N_GER, N_IND, N_ELITE);



        // System.out.println("\n\n--- OTIMIZANDO FUNÇÃO GRIEWANK (ARITMÉTICO) ---");
        // IndividuoFactory griewankFactoryArit = new GriewankIndFactory(10, IndividuoReal.TipoCrossover.ARITMETICO);
        // executarAG(griewankFactoryArit, N_GER, N_IND, N_ELITE);








        // Exemplo 2: Otimizando DIXON-PRICE (d=10), com Crossover ARITMÉTICO
        // System.out.println("\n\n--- OTIMIZANDO FUNÇÃO DIXON-PRICE (ARITMÉTICO) ---");
        // IndividuoFactory dixonPriceFactory = new DixonPriceIndFactory(10, IndividuoReal.TipoCrossover.ARITMETICO);
        // executarAG(dixonPriceFactory, N_GER, N_IND, N_ELITE);


        // Exemplo 3: Otimizando LANGERMANN (d=2), com Crossover BLX-ALPHA
        System.out.println("\n\n--- OTIMIZANDO FUNÇÃO LANGERMANN (BLX-ALPHA) ---");
        IndividuoFactory langermannFactory = new LangermannIndFactory(IndividuoReal.TipoCrossover.BLX_ALPHA);
        executarAG(langermannFactory, N_GER, N_IND, N_ELITE);
    }

    private static void executarAG(IndividuoFactory factory, int nGer, int nInd, int nElite) {
        AG ag = new AG();
        ag.executar(factory, nGer, nInd, nElite);
    }
}