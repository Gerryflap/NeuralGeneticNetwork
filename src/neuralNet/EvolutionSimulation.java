package neuralNet;

import cars.CarAgent;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by gerben on 27-11-15.
 */
public abstract class EvolutionSimulation implements Serializable {
    protected int population;
    public List<Agent> agents;

    public static EvolutionSimulation loadSimulation(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        EvolutionSimulation out = (EvolutionSimulation) objectInputStream.readObject();
        objectInputStream.close();
        return out;
    }

    public boolean save(File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public abstract List<Agent> generateAgents() throws EvolvingNeuralNet.NotEnoughLayersException;

    public abstract Agent generateAgent(Agent p1, Agent p2) throws EvolvingNeuralNet.NotEnoughLayersException;
    
    public EvolutionSimulation(int population) throws EvolvingNeuralNet.NotEnoughLayersException {
        this.population = population;
        agents = generateAgents();
    }

    public abstract Agent generateAgent() throws EvolvingNeuralNet.NotEnoughLayersException;
    
   public void iterate() throws EvolvingNeuralNet.NotEnoughLayersException {
       //Select the 2 fittest
       List<Agent> selected = selectFittest();

       //Add more agents based on chance and their fitness
       hunt(selected);
       
       //Generate a new generation
       agents = breed(selected);

   }


    public double getAverageFitness() {
        int avg = 0;
        for (Agent agent: agents) {
            avg += agent.getFitness();
        }
        return (double) avg/agents.size();
    }

    /**
     * Select 2 fittest agents to have a base for the next generation.
     * @return the 2 fittest agents.
     */
    private List<Agent> selectFittest(){
        List<Agent> selected = new ArrayList<Agent>();
        for (int i = 0; i < agents.size(); i++) {
            if(selected.size() < 2){
                if (selected.size() == 1) {
                    if (agents.get(i).getFitness() > selected.get(0).getFitness()) {
                        selected.add(0, agents.get(i));
                    } else {
                        selected.add(agents.get(i));
                    }
                } else {
                    selected.add(agents.get(i));
                }
            } else {
                if (selected.get(1).getFitness() < agents.get(i).getFitness()) {
                    if (selected.get(0).getFitness() < agents.get(i).getFitness()) {
                        selected.remove(1);
                        selected.add(0, agents.get(i));
                    } else {
                        selected.remove(1);
                        selected.add(agents.get(i));
                    }
                }
            }
        }
        return selected;
    }
    
    private void hunt(List<Agent> survivorList) {
        double maxChance = Integer.MIN_VALUE;
        double minChance = Integer.MAX_VALUE;
        
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).getFitness() > maxChance) {
                maxChance = agents.get(i).getFitness();
            } else if (agents.get(i).getFitness() < minChance) {
                minChance = agents.get(i).getFitness();
            }
        }

        double a = 1.1/(maxChance - minChance);

        for (int i = 0; i < agents.size(); i++) {
            if (!survivorList.contains(agents.get(i))) {
                //System.out.println(a * (agents.get(i).getFitness() - minChance) + 0.1);
                if (Util.random.nextDouble() <
                        (a * (agents.get(i).getFitness() - minChance)) - 0.1) {
                    survivorList.add(agents.get(i));
                }
            }

        }
        //System.out.println(survivorList.size());
    }

    private List<Agent> breed(List<Agent> survivorList) throws EvolvingNeuralNet.NotEnoughLayersException {
        List<Agent> newAgents = new ArrayList<Agent>();
        try {
            survivorList.sort(new AgentComparator());
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            System.out.println(survivorList);
        }
        Agent fittest = getFittestAgent();
        for (int i = 0; i < population -1; i++) {
            Agent p1 = getAgentByChances(survivorList);
            Agent p2 = getAgentByChances(survivorList);
            Agent newAgent = generateAgent(p1, p2);
            newAgents.add(newAgent);
            newAgent.mutate(2*Util.sig(fittest.getFitness() - 0.5*(p1.getFitness() + p2.getFitness())));
        }
        fittest.resetFitness();
        newAgents.add(fittest);
        //newAgents.add(generateAgent());
        //newAgents.add(generateAgent());
        return newAgents;
    }

    public Agent getFittestAgent() {
        Agent bestAgent = agents.get(0);
        for(Agent agent : agents) {
            if (bestAgent.getFitness() < agent.getFitness()){
                bestAgent = agent;
            }
        }
        return bestAgent;
    }

    public double getMedianFitness(){
        List<Agent> sorted = new ArrayList<>(agents);
        sorted.sort(new AgentComparator());
        if (sorted.size() % 2 == 0) {
            return 0.5 * (sorted.get(sorted.size()/2).getFitness() + sorted.get(sorted.size()/2 + 1).getFitness());
        } else {
            return sorted.get(sorted.size()/2).getFitness();
        }
    }

    public Agent getAgentByChances(List<Agent> sortedSurvivors) {
        for (int i = 0; i < sortedSurvivors.size(); i++) {
            if (Util.random.nextDouble() > ((double) i)/sortedSurvivors.size()) {
                return sortedSurvivors.get(i);
            }
        }
        return sortedSurvivors.get(sortedSurvivors.size() - 1);
    }

    public List<Agent> getAgents() {
        return agents;
    }

    class AgentComparator implements Comparator<Agent> {

        @Override
        public int compare(Agent o1, Agent o2) {
            return o2.compareTo(o1);
        }
    }
}


