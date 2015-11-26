package neuralNet;

import java.util.Random;

/**
 * Created by gerben on 26-11-15.
 */
public class EvolvingNeuralNet {
    double[][][] neuralNetwork;
    public static Random random = new Random();
    private int nInputs;
    private int neuronsPerLayer;
    private int nLayers;
    private int nOutputs;

    public EvolvingNeuralNet(int nInputs, int neuronsPerLayer, int nLayers, int nOutputs, EvolvingNeuralNet parent1, EvolvingNeuralNet parent2) throws NotEnoughLayersException {
        if (nLayers < 2) {
            throw new NotEnoughLayersException();
        }
        this.nInputs = nInputs;
        this.neuronsPerLayer = neuronsPerLayer;
        //convert to virtual layers
        nLayers = nLayers -1;
        this.nLayers = nLayers;
        this.nOutputs = nOutputs;

        //Initialize the NN layers
        neuralNetwork = new double[nLayers][][];

        //Initialize the Dots
        for (int i = 0; i < nLayers; i++) {
            if (i == 0) {
                neuralNetwork[i] = new double[nInputs][];
            } else {
                neuralNetwork[i] = new double[neuronsPerLayer][];
            }
        }

        //Initialize the Connections
        for (int layer = 0; layer < nLayers; layer++) {
            for (int dot = 0; dot < neuralNetwork[layer].length; dot++) {
                if (layer < neuralNetwork.length - 2) {
                    neuralNetwork[layer][dot] = new double[neuronsPerLayer];
                } else {
                    neuralNetwork[layer][dot] = new double[neuronsPerLayer];
                }

                for (int connection = 0; connection < neuralNetwork[layer][dot].length; connection++) {
                    if(parent1 ==null ||parent2 == null) {
                        neuralNetwork[layer][dot][connection] = Util.getExponentialMultiplier();
                    } else {
                        if (Util.flipCoin()) {
                            neuralNetwork[layer][dot][connection] = parent1.getMultiplier(layer,dot,connection);
                        } else {
                            neuralNetwork[layer][dot][connection] = parent2.getMultiplier(layer,dot,connection);
                        }
                    }
                }
            }
        }

    }

    public void mutate(double chance) {

        //I too like to live dangerously...
        while(random.nextDouble() < chance) {
            int layer = random.nextInt(nLayers);
            int dot = random.nextInt(neuralNetwork[layer].length);
            int connection = random.nextInt(neuralNetwork[layer][dot].length);
            neuralNetwork[layer][dot][connection] = Util.getExponentialMultiplier();
        }
    }

    public double[] process(double[] in) throws Util.DimensionMismatchException {
        double[] next = null;
        for (int layer = 0; layer < nLayers; layer++) {
            next = new double[neuralNetwork[layer][0].length];
            for (int dot = 0; dot < neuralNetwork[layer].length; dot++) {
                next = Util.add(next, Util.multiply(in[dot], neuralNetwork[layer][dot]));
            }
            in = Util.tanh(next);

        }
        return next;
    }

    public double getMultiplier(int layer, int dot, int connection) {
        return neuralNetwork[layer][dot][connection];
    }


    public class NotEnoughLayersException extends Exception {}

}