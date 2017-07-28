/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpe.cin.dmo4.udpgenerator.distributions;

import java.util.Random;

/**
 *
 * @author Danilo Oliveira
 */
public final class NormalDistribution implements RandomDistribution {

    private final double mean;
    private final double sd;
    private final Random random;

    public NormalDistribution(double mean, double sd, Random random) {
        this.mean = mean;
        this.sd = sd;
        this.random = random;
    }

    @Override
    public double random() {
        double val = random.nextGaussian() * sd + mean;

        // truncating the variable
        if (val < 0) {
            val = 0;
        }

        return val;
    }

}
