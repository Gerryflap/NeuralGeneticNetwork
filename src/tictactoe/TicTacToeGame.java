package tictactoe;

import neuralNet.Agent;
import neuralNet.EvolutionSimulation;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

import java.util.List;
import java.util.Scanner;

/**
 * Created by gerben on 2-12-15.
 */
public class TicTacToeGame {


    public static void main(String[] args) {
        try {
            TicTacToeEvolutionSimulator simulator = new TicTacToeEvolutionSimulator(150);
            StdInListener listener = new StdInListener();
            listener.start();
            int best = holdTournament(simulator);
            double avg = -200;
            double avgavg = -200;
            int i = 0;
            while (true) {
                simulator.iterate();
                best = holdTournament(simulator);
                avg = simulator.getAverageFitness();
                avgavg = 0.9 * avgavg + 0.1 * avg;
                String line = listener.popLine();
                if(line != null) {
                    if (line.equals("stats")) {
                        System.out.println(best + ", \t\t" + simulator.getAverageFitness() + ", \t\t " + avgavg);
                    } else if (line.equals("play")) {
                        playAgainstAI((TicTacToeAgent) simulator.getFittestAgent(), listener);
                    }
                }

            i += 1;

            }

        } catch (EvolvingNeuralNet.NotEnoughLayersException e) {
            e.printStackTrace();
        }

    }

    public static int holdTournament(TicTacToeEvolutionSimulator simulator) {
        List<Agent> agents = simulator.getAgents();
        for (int i = 0; i < agents.size(); i++) {
            TicTacToeAgent  agent1 = (TicTacToeAgent) agents.get(i);

            for (int j = (i + 1); j < agents.size(); j++) {
                TicTacToeAgent agent2 = (TicTacToeAgent) agents.get(j);
                int winner = playGame(agent1, agent2);
                if (winner == 1){
                    agent1.hasWon();
                    agent2.hasLost();
                } else if (winner == 2) {
                    agent2.hasWon();
                    agent1.hasLost();
                }
            }

        }
        return (int) simulator.getFittestAgent().getFitness();
    }

    public static void playAgainstAI(TicTacToeAgent agent, StdInListener listener) {

        int aiPlayerNum = Util.random.nextInt(2);
        Board board = new Board();

        int winner = -1;
        int player = 0;
        while (winner == -1) {
            boolean moveSuccessful = false;
            System.out.println(board);
            if (player == aiPlayerNum) {

                int move = agent.makeMove(board.getBoardForAI());
                System.out.println("AI placed at " + move);
                moveSuccessful = board.doMove(move);


            } else {

                int move = Integer.valueOf(listener.getNextLine());
                moveSuccessful = board.doMove(move);
            }

            if (!moveSuccessful) {
                winner = -2;
            } else {
                winner = board.getWinner();
            }
            player = (player + 1)%2;
        }
        if (winner == 0) {
            System.out.println("It's a draw!");
        } else if (winner == aiPlayerNum + 1) {
            System.out.println("AI won!");
        } else if (winner == -2) {
            System.out.println("Someone made the wrong move!");
        } else {
            System.out.println("You've won!");
        }

    }

    public static int playGame(TicTacToeAgent agent1, TicTacToeAgent agent2) {
        Board board = new Board();
        boolean agent1Starting = Util.random.nextBoolean();
        TicTacToeAgent[] agents = new TicTacToeAgent[2];
        if (agent1Starting) {
            agents[0] = agent1;
            agents[1] = agent2;
        } else {
            agents[0] = agent2;
            agents[1] = agent1;
        }
        int winner = -1;
        int player = 0;
        while (winner == -1){

            boolean moveSuccessful = board.doMove(agents[player].makeMove(board.getBoardForAI()));
            if (!moveSuccessful) {
                board.doMove(board.getFirstValidMove());
                agents[player].madeError();
                winner = board.getWinner();
            } else {
                winner = board.getWinner();
            }
            player = (player + 1)%2;

        }
        return winner;
    }
}
