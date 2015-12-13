package tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by gerben on 7-12-15.
 */
public class MoveEvaluator {
    Thread[] threads;
    int nThreads;
    final List<AgentTesterThread> availableThreads = new ArrayList<>();
    Semaphore threadsAvailable;

    public MoveEvaluator(int nThreads) {
        this.nThreads = nThreads;
        this.threads = new Thread[nThreads];
        for (int i = 0; i < threads.length; i++) {
            AgentTesterThread thread = new AgentTesterThread(this);
            threads[i] = thread;
            availableThreads.add(thread);
            thread.start();

        }
        threadsAvailable = new Semaphore(nThreads);
    }

    public void testAndRateAgent(TicTacToeAgent agent, boolean perfect) {
        try {
            threadsAvailable.acquire();
            synchronized (availableThreads) {
                availableThreads.get(0).testAgent(agent, perfect);
                availableThreads.remove(0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setThreadAvailable(AgentTesterThread thread) {
        synchronized (availableThreads) {
            availableThreads.add(thread);
        }
        threadsAvailable.release();
    }

    public void waitForJobsToFinish() {
        try {
            for (int i = 0; i < threads.length; i++) {
                threadsAvailable.acquire();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < threads.length; i++) {
            threadsAvailable.release();
        }
    }



    public class AgentTesterThread extends Thread{
        TicTacToeAgent agent = null;
        MoveEvaluator master;
        Semaphore job = new Semaphore(0);
        boolean perfect = false;

        public AgentTesterThread(MoveEvaluator master) {
            this.master = master;
        }

        public void run() {
            while(true) {
                try {
                    job.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                if (agent != null) {
                    if (perfect) {
                        for (boolean nnStarts: new boolean[]{true, false}){
                            int winner = TicTacToeGame.testAgainstAI(agent, perfect, nnStarts);
                            reward(winner);
                        }
                    }
                    int winner = TicTacToeGame.testAgainstAI(agent, perfect);
                    reward(winner);

                }
                master.setThreadAvailable(this);
            }

        }

        public void reward(int winner){
            if (winner == 1) {
                if(perfect) {
                    agent.hasWonPerfect();
                } else {
                    agent.hasWon();
                }
            } else if (winner == 2) {
                agent.hasLost();
            }

        }

        public void testAgent(TicTacToeAgent agent) {
        public void testAgent(TicTacToeAgent agent, boolean perfect) {
            job.release();
            this.agent = agent;
            this.perfect = perfect;
            job.release();
        }
    }

}
