package textTest;

import neuralNet.Agent;
import neuralNet.EvolutionSimulation;
import neuralNet.EvolvingNeuralNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerben on 7-12-15.
 */
public class TextEvolver extends EvolutionSimulation {
    public TextEvolver(int population) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(population);
    }

    @Override
    public List<Agent> generateAgents() throws EvolvingNeuralNet.NotEnoughLayersException {
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            agents.add(new TextAgent());
        }
        return agents;
    }

    @Override
    public Agent generateAgent(Agent p1, Agent p2) throws EvolvingNeuralNet.NotEnoughLayersException {
        return new TextAgent(p1, p2);
    }

    @Override
    public Agent generateAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
        return new TextAgent();
    }
}
