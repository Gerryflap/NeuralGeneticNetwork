package sim;

import neuralNet.Agent;
import neuralNet.EvolutionSimulation;
import neuralNet.EvolvingNeuralNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerben on 27-11-15.
 */
public class NewEvolutionSimulator extends EvolutionSimulation{
    public NewEvolutionSimulator(int population) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(population);
    }

    @Override
    public Agent generateAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
        return new NewGuesser();
    }

    @Override
    public List<Agent> generateAgents() throws EvolvingNeuralNet.NotEnoughLayersException {
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            agents.add(new NewGuesser(null, null));
        }
        return agents;
    }

    @Override
    public Agent generateAgent(Agent p1, Agent p2) throws EvolvingNeuralNet.NotEnoughLayersException {
        return new NewGuesser(p1, p2);
    }
}
