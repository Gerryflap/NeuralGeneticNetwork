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
        NEURAL_INPUTS = 18;
        NEURAL_LAYERS = 6;
        NEURONS_PER_LAYER = 18;
        OUTCOME_MULTIPLIER = 1;
        NEURAL_OUTPUTS = 1;
        MUTATION_CHANCE = 0.2;
        BASE_MULTIPLIER = 1.0;
    }

    @Override
    public double getFitness() {
        return winLossSum ;
    }

    @Override
    public void resetFitness() {
        winLossSum = 0;
        totalErrors = 0;
    }

    public void hasWon() {
        winLossSum += 1;
    }

    public void hasWonPerfect(){winLossSum += 50;}

    public void hasLost() {
        winLossSum -= 1;
    }

    public void madeError() {
        totalErrors += 1;
    }

    public void lostFromPlayer() {winLossSum -= 10;}

    public int makeMove(Board boardInfo){
        try {

            double[] out= null;
            double max = 0;
            int maxI = 0;
            for (int i = 0; i < 9; i++) {
                if (boardInfo.getPlayerAt(i) == 0) {
                    out = process(boardInfo.getBoardForAI(i));
                    if (out[0] > max) {
                        max = out[0];
                        maxI = i;
                    }
                }
            }
            return maxI;


            //return (int) (process(boardInfo)[0]);

        } catch (Util.DimensionMismatchException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
