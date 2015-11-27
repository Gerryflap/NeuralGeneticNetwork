package neuralNet;

/**
 * Created by gerben on 27-11-15.
 */
public abstract class Agent {

    protected EvolvingNeuralNet neuralNet;
    public static double MUTATION_CHANCE = 0.5;
    public static int NEURAL_INPUTS = 1;
    public static int NEURAL_OUTPUTS = 1;
    public static int NEURAL_LAYERS = 3;
    public static int NEURONS_PER_LAYER = 5;
    public static int OUTCOME_MULTIPLIER = 1;

    public Agent() throws EvolvingNeuralNet.NotEnoughLayersException {
        this(null, null);
    }

    public Agent(Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        if (parent1 == null || parent2 == null) {
            neuralNet = new EvolvingNeuralNet(NEURAL_INPUTS, NEURONS_PER_LAYER, NEURAL_LAYERS, NEURAL_OUTPUTS, null, null);
        } else {
            neuralNet = new EvolvingNeuralNet(NEURAL_INPUTS, NEURONS_PER_LAYER, NEURAL_LAYERS, NEURAL_OUTPUTS
                    , parent1.getNeuralNet(), parent2.getNeuralNet());

        }
        neuralNet.mutate(MUTATION_CHANCE);
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
}