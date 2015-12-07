package neuralNet;

import java.util.Random;

/**
 * Created by gerben on 26-11-15.
 */
public class Util {

    public static Random random = new Random();

    public static double[] multiply(double f, double[] array) {
        double[] out = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            out[i] = array[i] * f;
        }
        return out;
    }

    public static double[] add(double[] array, double[] array2) throws DimensionMismatchException {
        if (array.length != array2.length ){
            throw new DimensionMismatchException("array size mismatch!");
        }
        double[] out = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            out[i] = array[i] + array2[i];
        }
        return out;
    }

    public static boolean flipCoin() {
        return random.nextBoolean();
    }

    public static double[] sig(double[] array) {
        double[] out = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            out[i] = Util.sig(array[i]);
        }
        return out;
    }

    public static double sig(double x) {
        return 1.0/(1.0+Math.exp(-x));
    }

    public static double arctanh(double x) {
        return Math.log((1/(x+1)) / (1/(x-1)))/2;
    }

    public static double getExponentialMultiplier() {
        /**
        double value = random.nextDouble();
        if (value == 0) {
            return 0;
        }
        value = 1/value-1;
        return random.nextBoolean()?value:-value;
         */
        return random.nextGaussian();
    }

    public static class DimensionMismatchException extends Exception {
        public DimensionMismatchException() {
        }

        public DimensionMismatchException(String s) {
            super(s);
        }
    }


}
