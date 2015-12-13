package neuralNet;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by gerben on 26-11-15.
 */
public class EvolvingNeuralNet implements Serializable {
    double[][][] neuralNetwork;
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
                neuralNetwork[i] = new double[nInputs + 1][];
            } else if (i == nLayers -1) {
                neuralNetwork[i] = new double[nOutputs][];
            } else {
                neuralNetwork[i] = new double[neuronsPerLayer + 1][];
            }
        }

        //Initialize the Connections
        boolean crossedOver = false;
        for (int layer = 0; layer < nLayers; layer++) {
            for (int dot = 0; dot < neuralNetwork[layer].length; dot++) {
                if (layer < neuralNetwork.length - 2) {
                    neuralNetwork[layer][dot] = new double[neuronsPerLayer];
                } else {
                    neuralNetwork[layer][dot] = new double[nOutputs];
                }

                for (int connection = 0; connection < neuralNetwork[layer][dot].length; connection++) {
                    if(parent1 ==null ||parent2 == null) {
                        neuralNetwork[layer][dot][connection] = Util.getExponentialMultiplier();
                    } else {
                        if (!crossedOver) {
                            neuralNetwork[layer][dot][connection] = parent1.getMultiplier(layer,dot,connection);
                            crossedOver = Util.random.nextDouble() > 0.94;
                        } else {
                            neuralNetwork[layer][dot][connection] = parent2.getMultiplier(layer,dot,connection);
                            crossedOver = Util.random.nextDouble() > 0.01;
                        }
                    }
                }
            }
        }

    }

    public void mutate(double chance){
        mutate(chance, 1.0);
    }

    public void mutate(double chance, double mutationMultiplier) {

        //I too  like to live dangerously...
        /**
        while(Util.random.nextDouble() < chance) {
            int layer = Util.random.nextInt(nLayers);
            int dot = Util.random.nextInt(neuralNetwork[layer].length);
            int connection = Util.random.nextInt(neuralNetwork[layer][dot].length);
            neuralNetwork[layer][dot][connection] += Util.getExponentialMultiplier();
        }

         */
        for (int layer = 0; layer < nLayers; layer++) {
            for (int dot = 0; dot < neuralNetwork[layer].length; dot++) {
                for (int connection = 0; connection < neuralNetwork[layer][dot].length; connection++) {
                    if (Util.random.nextDouble() < chance) {
                        neuralNetwork[layer][dot][connection] += Util.getExponentialMultiplier()*mutationMultiplier;
                    }
                }
            }
        }
    }

    public double[] process(double[] in) throws Util.DimensionMismatchException {
        double[] next = null;
        for (int layer = 0; layer < nLayers; layer++) {
            double[] in_ = new double[in.length + 1];
            System.arraycopy(in, 0, in_, 0, in.length);
            in_[in.length] = 1.0;
            in = in_;
            next = new double[neuralNetwork[layer][0].length];
            for (int dot = 0; dot < neuralNetwork[layer].length; dot++) {
                next = Util.add(next, Util.multiply(in[dot], neuralNetwork[layer][dot]));
            }
            in = Util.sig(next);

        }
        return in;
    }

    public double getMultiplier(int layer, int dot, int connection) {
        return neuralNetwork[layer][dot][connection];
    }


    public class NotEnoughLayersException extends Exception {}

}
