package tictactoe;

import neuralNet.Agent;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

/**
 * Created by gerben on 2-12-15.
 */
public class TicTacToeAgent extends Agent {
    int winLossSum = 0;
    int totalErrors = 0;

    public TicTacToeAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
        super();
    }

    public TicTacToeAgent(Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(parent1, parent2);
    }

    @Override
    public void setStaticVars() {
        NEURAL_INPUTS = 10;
        NEURAL_LAYERS = 4;
        NEURONS_PER_LAYER = 21;
        OUTCOME_MULTIPLIER = 1;
        NEURAL_OUTPUTS = 9;
        MUTATION_CHANCE = 0.6;
    }

    @Override
    public double getFitness() {
        return winLossSum - totalErrors * 10;
    }

    public void hasWon() {
        winLossSum += 1;
    }

    public void hasLost() {
        winLossSum -= 1;
    }

    public void madeError() {
        totalErrors += 1;
    }

    public int makeMove(double[] boardInfo){
        try {
            double[] out = process(boardInfo);
            for (int i = 0; i < 9; i++) {
                if (out[i] > 0.5) {
                    return i;
                }
            }
        } catch (Util.DimensionMismatchException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
