package sim;

import neuralNet.Agent;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

/**
 * Created by gerben on 27-11-15.
 */
public class NewGuesser extends Agent {
    public static final double RANGE = 1;
    public static final double STEP_SIZE = 0.01;
    public static final double OUTCOME_MULTIPLIER = 3000.0;
    private double fitness = Double.MIN_VALUE;

    public void setStaticVars() {
        NEURAL_LAYERS = 4;
        NEURONS_PER_LAYER = 5;
        MUTATION_CHANCE = 0.2;
    }

    public NewGuesser() throws EvolvingNeuralNet.NotEnoughLayersException {
        super();
    }

    public NewGuesser(Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(parent1, parent2);
    }

    @Override
    public double getFitness() {
        if (fitness == Double.MIN_VALUE) {
            double meanError = 0;

            for (double x = 0; x <= RANGE; x += STEP_SIZE) {
                double x_ = x + (2 * Util.random.nextDouble() -1 ) * STEP_SIZE;
                try {
                    meanError += Math.sqrt(Math.abs(process(x_)[0] - NewExample.fStat(x_)));
                } catch (Util.DimensionMismatchException e) {
                    e.printStackTrace();
                }
            }
            meanError /= RANGE;

            fitness = 1.0 /meanError;
            return 1.0 / meanError;
        } else {
            return fitness;
        }
    }

    @Override
    public double[] process(double... input) throws Util.DimensionMismatchException {
        return super.process(input , OUTCOME_MULTIPLIER);
    }

    @Override
    public void resetFitness() {
        fitness = Double.MIN_VALUE;
    }
}
