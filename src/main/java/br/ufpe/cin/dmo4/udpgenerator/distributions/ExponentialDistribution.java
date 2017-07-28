package br.ufpe.cin.dmo4.udpgenerator.distributions;

import java.util.Random;

/**
 *
 * @author Danilo Oliveira
 */
public class ExponentialDistribution implements RandomDistribution{
    private final double rate;
    private final Random random;

    public ExponentialDistribution(double rate, Random random) {
        this.rate = rate;
        this.random = random;
    }

    @Override
    public double random() {
        return -(Math.log(1 - random.nextDouble()) / rate);
    }
    
    public static void main(String[] args) {
        RandomDistribution rd = RandomDistribution.createRandomDistribution(0.1);
        System.out.println("c(");
        for(int i = 0; i < 100; i++){
            System.out.println(rd.random() + ", ");
        }
        System.out.println(")");
    }
    
    
}
