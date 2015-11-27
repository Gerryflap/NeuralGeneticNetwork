package cars;

import neuralNet.Agent;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

/**
 * Created by gerben on 27-11-15.
 */
public class CarAgent extends Agent {

    private int damage = 0;
    private double x;
    private double y;
    private double rotation;
    private double speed = 0;

    public CarAgent(double x, double y) throws EvolvingNeuralNet.NotEnoughLayersException {
        this(x, y, null, null);
    }

    public CarAgent(double x, double y, Agent parent1, Agent parent2) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(parent1, parent2);
        this.x = x;
        this.y = y;
    }

    @Override
    public void setStaticVars() {
        //3 collision lines
        NEURAL_INPUTS = 4;

        NEURAL_LAYERS = 3;
        NEURONS_PER_LAYER = 5;
        //speed and delta angle
        NEURAL_OUTPUTS = 2;

        MUTATION_CHANCE = 0.5;
    }

    @Override
    public double getFitness() {
        return -damage;
    }

    public void drive(double... ranges) {
        try {
            double[] output = process(ranges);
            speed += (2*output[0]-1)*0.01;
            rotation = (rotation + (2*output[1] -1) * 0.003);
            x += Math.cos(rotation) * speed;
            y += Math.sin(rotation) * speed;


        } catch (Util.DimensionMismatchException e) {
            e.printStackTrace();
        }
    }

    public void collide() {
        damage += 1;
    }

    public double getX() {
        return x;
    }

    public double getRotation() {
        return rotation;
    }

    public double getY() {
        return y;
    }

    public int[] getXs() {
        int[] xs = new int[4];
        for (int i = 0; i < 4; i++) {
            xs[i] = (int) (Math.cos(i * 0.5* Math.PI + rotation + 0.25*Math.PI) * 30 + x);
        }
        return xs;
    }

    public int[] getYs() {
        int[] ys = new int[4];
        for (int i = 0; i < 4; i++) {
            ys[i] = (int) (Math.sin(i * 0.5* Math.PI + rotation + 0.25*Math.PI) * 30 + y);
        }
        return ys;
    }

    public double getSpeed() {
        return speed;
    }
}
