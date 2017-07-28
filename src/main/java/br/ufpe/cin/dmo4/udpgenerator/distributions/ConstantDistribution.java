package br.ufpe.cin.dmo4.udpgenerator.distributions;

/**
 *
 * @author Danilo Oliveira
 */
public final class ConstantDistribution implements RandomDistribution {

    private final double value;


    public ConstantDistribution(double value) {
        this.value = value;
    }        
    
    @Override
    public double random() {
        return value;
    }
    
}
