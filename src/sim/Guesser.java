package sim;

import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

import java.util.Random;

/**
 * Created by gerben on 26-11-15.
 */
public class Guesser {

    public static Random random = new Random();
    private EvolvingNeuralNet neuralNet;
    private double chanceOfSurvival = -1;

    public Guesser(Guesser p1, Guesser p2) {
        try {
            if (p1 == null || p2 == null) {
                neuralNet = new EvolvingNeuralNet(1, 5, 5, 1,null,null);
            } else {
                neuralNet = new EvolvingNeuralNet(1, 5, 5, 1, p1.getNN(), p2.getNN());
            }
        } catch (EvolvingNeuralNet.NotEnoughLayersException e) {
            e.printStackTrace();
        }
        neuralNet.mutate(0.00001);
    }

    public Guesser() {
        this(null, null);
    }

    public double test() {
        try {
            double errorMean = 0;
            for (int i = 0; i < 100; i++) {
                double x = (2*random.nextDouble()-1) * 10;
                double ourAnswer = Util.arctanh(neuralNet.process(new double[]{x})[0]);
                double correctAnswer = Example.f(x);
                errorMean += Math.pow(ourAnswer - correctAnswer, 2);
            }
            errorMean/=10;
            return 1 - (Util.tanh(errorMean/10000.0));
        } catch (Util.DimensionMismatchException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getChanceOfSurvival() {
        if (chanceOfSurvival == -1){
            chanceOfSurvival = test();
        }
        return chanceOfSurvival;
    }

    public EvolvingNeuralNet getNN() {
        return neuralNet;
    }

    public String toString() {
        return String.format("Guesser: %f", chanceOfSurvival);
    }
}
