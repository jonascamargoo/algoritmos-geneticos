package human;

import java.util.List;

public class AGRunner {
    public static void main(String[] args) {
        IndividuoFactory factory = new IndividuoNRainhasFactory(8);

        AG ag = new AG();
        // List<Individuo> indList = ag.executar(factory, 2000, 40, 8);
        ag.executar(factory, 2000, 40, 8);
        
    }

    
}

