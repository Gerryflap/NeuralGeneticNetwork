package textTest;

import neuralNet.Agent;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

/**
 * Created by gerben on 7-12-15.
 */
public class TextAgent extends Agent {
    public int fitness = Integer.MIN_VALUE;
    public static String TEST_STRING = "";

    public TextAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
    }

    public TextAgent(Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(parent1, parent2);
    }

    @Override
    public void setStaticVars() {
        MEMORY_NEURONS = 12;

        NEURAL_LAYERS = 4;
        NEURAL_OUTPUTS = 3;
        NEURAL_INPUTS = 1;
        NEURONS_PER_LAYER = 15;
        MUTATION_CHANCE = 0.1;

    }

    @Override
    public double getFitness() {

        if (fitness == Integer.MIN_VALUE) {
            String s = "";
            String out = "";
            for (int i = 0; i < NEURAL_OUTPUTS; i++) {
                //char c = (char) (Util.random.nextDouble()*26 + 65);
                char c = TEST_STRING.charAt(i);
                s+=c;
                try {

                    if(i == NEURAL_OUTPUTS-1) {
                        double[] outp = process((c - 65.0)/26.0);
                        for (int i1 = 0; i1 < NEURAL_OUTPUTS; i1++) {
                            double d = outp[i1];
                            out += (char) (d * 26 + 65);
                        }
                    } else {
                        process((c - 65.0)/26.0);
                    }
                } catch (Util.DimensionMismatchException e) {
                    e.printStackTrace();
                }
            }
            fitness = 300;
                //
            for (int i = 0; i < s.length(); i++) {
                fitness -= Math.pow(s.charAt(i) - out.charAt(i), 2);
            }
            if (fitness > 280) {
                System.out.printf("Trained: %s, Result: %s\n", s, out);
            }
        }

        return fitness;
    }

    @Override
    public void resetFitness() {
        fitness = Integer.MIN_VALUE;
    }
}
