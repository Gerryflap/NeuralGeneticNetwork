package tictactoe;

import neuralNet.Agent;
import neuralNet.EvolutionSimulation;
import neuralNet.EvolvingNeuralNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerben on 2-12-15.
 */
public class TicTacToeEvolutionSimulator extends EvolutionSimulation {
    public TicTacToeEvolutionSimulator(int population) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(population);
    }

    @Override
    public List<Agent> generateAgents() throws EvolvingNeuralNet.NotEnoughLayersException {
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            agents.add(new TicTacToeAgent());
        }
        return agents;
    }

    @Override
    public Agent generateAgent(Agent p1, Agent p2) throws EvolvingNeuralNet.NotEnoughLayersException {
        return new TicTacToeAgent(p1, p2);
    }

    @Override
    public Agent generateAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
        return new TicTacToeAgent();
    }
}
