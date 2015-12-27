package imageGen;

import neuralNet.Agent;
import neuralNet.EvolvingNeuralNet;

/**
 * Created by gerben on 27-12-15.
 */
public class ImageGenAgent extends Agent {
    int score = 0;

    public ImageGenAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
    }

    public ImageGenAgent(Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(parent1, parent2);
    }

    @Override
    public void setStaticVars() {
        NEURAL_INPUTS = 4;

        NEURAL_LAYERS = 15;
        NEURONS_PER_LAYER = 15;
        //speed and delta angle
        NEURAL_OUTPUTS = 3;

        MUTATION_CHANCE = 0.04;
        BASE_MULTIPLIER = 1.0;

        MEMORY_NEURONS = 1;

    }

    @Override
    public double getFitness() {
        return score;
    }

    public void select() {
        score = 100;
    }

    @Override
    public void resetFitness() {
        score = 0;

    }
}
