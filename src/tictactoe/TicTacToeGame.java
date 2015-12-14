package tictactoe;

import neuralNet.Agent;
import neuralNet.EvolutionSimulation;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;
import visual.EvolutionInfoView;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by gerben on 2-12-15.
 */
public class TicTacToeGame {
    static MoveEvaluator evaluator = new MoveEvaluator(10);
    public static long seed = 0;


    public static void main(String[] args) {
        try {
            TicTacToeEvolutionSimulator simulator = new TicTacToeEvolutionSimulator(20);
            StdInListener listener = new StdInListener();
            listener.start();
            boolean visual = args.length > 0;
            EvolutionInfoView view = null;
            if (visual) {
                view = new EvolutionInfoView(simulator);
            }
            int best = holdTournament(simulator);
            double avg = -200;
            double avgavg = -2000;
            int i = 0;
            while (true) {
                seed = System.currentTimeMillis();
                evaluator.waitForJobsToFinish();
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
                    } else if (line.equals("save")) {
                        File file = new File("./TTTsimulation.sim");
                        simulator.save(file);
                        System.out.printf("Saved a simulation with a fittest agent with fitness %f\n", simulator.getFittestAgent().getFitness());

                    } else if (line.equals("load")) {
                        File file = new File("./TTTsimulation.sim");
                        try {
                            TicTacToeEvolutionSimulator out = (TicTacToeEvolutionSimulator) TicTacToeEvolutionSimulator.loadSimulation(file);
                            System.out.printf("Loaded a simulation wit a fittest agent with fitness %f\n", out.getFittestAgent().getFitness());
                            simulator = out;
                            if (visual) {
                                view.setSimulation(simulator);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if(line.equals("visual")) {

                        view = new EvolutionInfoView(simulator);
                        visual = true;

                    }
                }

                if (visual) {
                    view.update();
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

            //**
            for (int j = 0; j < 5; j++) {
                TicTacToeAgent agent2 = (TicTacToeAgent) agents.get(Util.random.nextInt(agents.size()));
                int winner = playGame(agent1, agent2);
                if (winner == 1){
                    agent1.hasWon();
                    agent2.hasLost();
                } else if (winner == 2) {
                    agent2.hasWon();
                    agent1.hasLost();
                }
            }
             //*/
            /**
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
            //*/
            /**
            for (int j = 0; j < 50; j++) {
                int winner = testAgainstAI(agent1);
                if (winner == 1){
                    agent1.hasWon();
                } else if (winner == 2) {
                    agent1.hasLost();
                }
            }
            */
            //evaluator.testAndRateAgent(agent1, true);

            //**
            for (int j = 0; j < 20; j++) {
                evaluator.testAndRateAgent(agent1, false, i);
            }
            //*/

            int a = agent1.makeMove(new Board(new int[]{2,1,0,0,1,0,0,0,0}, 2));
            if (a == 7) {
                agent1.hasWon();
            } else {
                agent1.lostFromPlayer();
            }

        }
        return (int) simulator.getFittestAgent().getFitness();
    }

    public static int testAgainstAI(TicTacToeAgent agent, boolean perfect, int i){
        return testAgainstAI(agent, perfect, Util.random.nextBoolean(), i);
    }
    public static int testAgainstAI(TicTacToeAgent agent, boolean perfect, boolean aiStarts, int iSeed) {
        Board board = new Board();
        int aiPlayerNum = aiStarts?0:1;
        Random random = new Random(seed + iSeed);
        int winner = -1;
        int player = 0;
        while (winner == -1){
            if (player == aiPlayerNum) {
                boolean moveSuccessful = board.doMove(agent.makeMove(board));
                if (!moveSuccessful) {
                    board.doMove(board.getFirstValidMove());
                    agent.madeError();
                }
            } else {
                int pos = 0;
                if (perfect) {
                    pos = board.getBestMove();
                } else {
                    ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
                    for (int i = 0; i < 9; i++) {
                        possibleMoves.add(i);
                    }
                    while (board.getPlayerAt(pos) != 0) {
                        possibleMoves.remove(new Integer(pos));
                        pos = possibleMoves.get(random.nextInt(possibleMoves.size()));
                    }
                }
                board.doMove(pos);
            }
            winner = board.getWinner();

            player = (player + 1)%2;

        }

        return aiPlayerNum == 0?winner:((winner)%2 + 1);

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

                int move = agent.makeMove(board);
                //int move = board.getBestMove();
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
            agent.hasWon();
            System.out.println("AI won!");
        } else if (winner == -2) {
            System.out.println("Someone made the wrong move!");
        } else {
            System.out.println("You've won!");
            agent.lostFromPlayer();
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

            boolean moveSuccessful = board.doMove(agents[player].makeMove(board));
            if (!moveSuccessful) {
                agents[player].madeError();
                board.doMove(board.getFirstValidMove());
            }
            winner = board.getWinner();
            player = (player + 1)%2;

        }
        if (winner == 0) {
            //a draw is like a loss for the starting agent
            return agent1Starting?2:1;
        }
        return agent1Starting?winner:((winner)%2 + 1);
    }
}
