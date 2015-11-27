package neuralNet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerben on 27-11-15.
 */
public abstract class EvolutionSimulation {
    protected int population;
    public List<Agent> agents;

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
        double maxChance = 0;
        double minChance = 1;
        
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).getFitness() > maxChance) {
                maxChance = agents.get(i).getFitness();
            } else if (agents.get(i).getFitness() < minChance) {
                minChance = agents.get(i).getFitness();
            }
        }

        double a = 0.8/(maxChance - minChance);

        for (int i = 0; i < agents.size(); i++) {
            if (!survivorList.contains(agents.get(i))) {
                if (Util.random.nextDouble() <  (a * agents.get(i).getFitness() + 0.1)) {
                    survivorList.add(agents.get(i));
                }
            }

        }
    }

    private List<Agent> breed(List<Agent> survivorList) throws EvolvingNeuralNet.NotEnoughLayersException {
        List<Agent> newAgents = new ArrayList<Agent>();
        for (int i = 0; i < population - 1; i++) {
            Agent p1 = survivorList.get(Util.random.nextInt(survivorList.size()));
            Agent p2 = survivorList.get(Util.random.nextInt(survivorList.size()));
            newAgents.add(generateAgent(p1, p2));
        }
        newAgents.add(generateAgent());
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
}
