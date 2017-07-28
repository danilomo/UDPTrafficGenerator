package br.ufpe.cin.dmo4.udpgenerator.distributions;

import java.util.Map;
import java.util.Random;

/**
 * A interface for a random generator following some probability distribution.
 *
 * @author Danilo Oliveira
 */
public interface RandomDistribution {

    /**
     * Creates a RandomDistribution with a new java.util.Random object according
     * to the specified distribution and parameters.
     *
     * @param type The probability distribution (normal, geometric, etc.)
     * @param parameters A hashmap containing the distribution parameters (e.g.:
     * sd: 1, mean: 10 )
     * @return A RandomDistribution object.
     */
    public static RandomDistribution createRandomDistribution(String type, Map<String, Double> parameters) {
        return createRandomDistribution(type, parameters, new Random());
    }

    /**
     * Creates an exponential RandomDistribution with a new java.util.Random
     * object according to the specified rate.
     *
     * @param rate The rate parameter of an exponential distribution
     * @return
     */
    public static RandomDistribution createRandomDistribution(double rate) {
        return createRandomDistribution(rate, new Random());
    }

    /**
     * Creates a RandomDistribution with a specified java.util.Random object
     * according to the specified distribution and parameters.
     *
     * @param type The probability distribution (normal, geometric, etc.)
     * @param parameters A hashmap containing the distribution parameters (e.g.:
     * sd: 1, mean: 10 )
     * @param random A random number generator
     * @return A RandomDistribution object.
     */
    public static RandomDistribution createRandomDistribution(String type,
            Map<String, Double> parameters, Random random) {
        switch (type) {
            case "constant":
                return new ConstantDistribution(parameters.get("value"));
            case "normal":
                return new NormalDistribution(parameters.get("mean"), parameters.get("sd"), random);
            default:
                return null;
        }
    }

    /**
     * Creates an exponential RandomDistribution with a specified
     * java.util.Random object according to the specified rate.
     *
     * @param rate The rate parameter of an exponential distribution
     * @param random A random number generator
     * @return
     */
    public static RandomDistribution createRandomDistribution(double rate,
            Random random) {
        return new ExponentialDistribution(rate, random);
    }

    public double random();

}
