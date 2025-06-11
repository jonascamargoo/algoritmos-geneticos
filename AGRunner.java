public class AGRunner {
    public static void main(String[] args) {
        IndividuoFactory factory = new IndividuoNRainhasFactory(8);

        AG ag = new AG();
        ag.executar(factory, 2000, 40, 8);
        
    }

    
}

