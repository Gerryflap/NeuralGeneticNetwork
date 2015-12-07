package textTest;

import neuralNet.Agent;
import neuralNet.EvolvingNeuralNet;

/**
 * Created by gerben on 7-12-15.
 */
public class TextAgent extends Agent {
    public int goodClassifications = 0;

    public TextAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
    }

    public TextAgent(Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(parent1, parent2);
    }

    @Override
    public void setStaticVars() {
        MEMORY_NEURONS = 5;

        NEURAL_LAYERS = 3;
        NEURAL_OUTPUTS = 1;
        NEURAL_INPUTS = 1;
        NEURONS_PER_LAYER = 5;

    }

    @Override
    public double getFitness() {
        return goodClassifications;
    }

    @Override
    public void resetFitness() {
        goodClassifications = 0;
    }
}
