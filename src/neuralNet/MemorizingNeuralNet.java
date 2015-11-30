package neuralNet;

/**
 * Created by gerben on 30-11-15.
 */
public class MemorizingNeuralNet extends EvolvingNeuralNet {
    double[] memory;


    public MemorizingNeuralNet(int nInputs, int neuronsPerLayer, int nLayers, int nOutputs, int nMemoryNeurons, EvolvingNeuralNet parent1, EvolvingNeuralNet parent2) throws NotEnoughLayersException {
        super(nInputs + nMemoryNeurons, neuronsPerLayer, nLayers, nOutputs + nMemoryNeurons, parent1, parent2);
        memory = new double[nMemoryNeurons];
    }

    @Override
    public double[] process(double[] in) throws Util.DimensionMismatchException {
        double[] actualIn = new double[in.length + memory.length];
        System.arraycopy(in, 0, actualIn, 0, in.length);
        System.arraycopy(memory, 0, actualIn, in.length, memory.length);
        return super.process(actualIn);
    }
}
