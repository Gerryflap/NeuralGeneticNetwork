package tictactoe;

import neuralNet.Agent;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

/**
 * Created by gerben on 2-12-15.
 */
public class TicTacToeAgent extends Agent {
    int winLossSum = 0;
    int totalErrors = 1;

    public TicTacToeAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
        super();
    }

    public TicTacToeAgent(Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(parent1, parent2);
    }

    @Override
    public void setStaticVars() {
        NEURAL_INPUTS = 9;
        NEURAL_LAYERS = 3;
        NEURONS_PER_LAYER = 9;
        OUTCOME_MULTIPLIER = 1;
        NEURAL_OUTPUTS = 9;
        MUTATION_CHANCE = 0.4;
    }

    @Override
    public double getFitness() {
        return (winLossSum - (Math.pow(1.1, totalErrors) + totalErrors) * 20) ;
    }

    @Override
    public void resetFitness() {
        winLossSum = 0;
        totalErrors = 0;
    }

    public void hasWon() {
        winLossSum += 2;
    }

    public void hasWonPerfect(){winLossSum += 50;}

    public void hasLost() {
        winLossSum -= 1;
    }

    public void madeError() {
        totalErrors += 1;
    }

    public int makeMove(double[] boardInfo){
        try {

            double[] out = process(boardInfo);
            double max = 0;
            int maxI = 0;
            for (int i = 0; i < 9; i++) {
                if (boardInfo[i] == 0 && out[i] > max) {
                    max = out[i];
                    maxI = i;
                }
            }
            return maxI;
             /**
            return (int) (process(boardInfo)[0]*9);
              */
        } catch (Util.DimensionMismatchException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
