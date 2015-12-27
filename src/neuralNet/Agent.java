package neuralNet;

import java.io.*;

/**
 * Created by gerben on 27-11-15.
 */
public abstract class Agent implements Comparable, Serializable{

    public static double BASE_MULTIPLIER = 0;
    protected EvolvingNeuralNet neuralNet;
    public static double MUTATION_CHANCE = 0.5;
    public static int NEURAL_INPUTS = 1;
    public static int NEURAL_OUTPUTS = 1;
    public static int NEURAL_LAYERS = 3;
    public static int NEURONS_PER_LAYER = 5;
    public static int OUTCOME_MULTIPLIER = 1;
    public static int MEMORY_NEURONS = 0;
    public static boolean staticSet = false;

    public abstract void setStaticVars();

    public static Agent loadAgent(File file) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
        Agent out = (Agent) objectInputStream.readObject();
        objectInputStream.close();
        return out;

    }

    public Agent() throws EvolvingNeuralNet.NotEnoughLayersException {
        this(null, null);
    }

    public Agent(Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        if (!staticSet) {
            setStaticVars();
            staticSet = true;
        }
        if (parent1 == null || parent2 == null) {
            neuralNet = new MemorizingNeuralNet(NEURAL_INPUTS, NEURONS_PER_LAYER, NEURAL_LAYERS, NEURAL_OUTPUTS,
                    MEMORY_NEURONS, null, null);
        } else {
            neuralNet = new MemorizingNeuralNet(NEURAL_INPUTS, NEURONS_PER_LAYER, NEURAL_LAYERS, NEURAL_OUTPUTS,
                    MEMORY_NEURONS, parent1.getNeuralNet(), parent2.getNeuralNet());
        }
    }

    public void mutate(double chanceMultiplier) {
        neuralNet.mutate(MUTATION_CHANCE, chanceMultiplier + BASE_MULTIPLIER);
    }
    /**
     * Calculate the fitness.
     * @return fitness of this agent
     */
    public abstract double getFitness();

    public EvolvingNeuralNet getNeuralNet() {
        return neuralNet;
    }

    public double[] process(double... input) throws Util.DimensionMismatchException {
        return process(input, OUTCOME_MULTIPLIER);
    }

    public double[] process(double[] input, double outcomeMultiplier) throws Util.DimensionMismatchException {
        return Util.multiply(outcomeMultiplier ,neuralNet.process(input));
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Agent) {
            Agent agent = (Agent) o;
            return  (int) Math.signum(getFitness() - agent.getFitness());
        }
        return 0;
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

    public abstract void resetFitness();
}