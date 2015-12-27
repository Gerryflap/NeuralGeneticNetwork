package imageGen;

import neuralNet.Agent;
import neuralNet.EvolutionSimulation;
import neuralNet.EvolvingNeuralNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerben on 27-12-15.
 */
public class ImageGenEvoSim extends EvolutionSimulation{
    public ImageGenEvoSim(int population) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(population);
    }

    @Override
    public List<Agent> generateAgents() throws EvolvingNeuralNet.NotEnoughLayersException {
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            agents.add(new ImageGenAgent());
        }
        return agents;
    }

    @Override
    public Agent generateAgent(Agent p1, Agent p2) throws EvolvingNeuralNet.NotEnoughLayersException {
        return new ImageGenAgent(p1, p2);
    }

    @Override
    public Agent generateAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
        return new ImageGenAgent();
    }
}
