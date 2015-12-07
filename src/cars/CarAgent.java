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
    private boolean hasCollided = false;
    private double[] ranges;
    private boolean hasSignaled;

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
        NEURAL_INPUTS = 12;

        NEURAL_LAYERS = 5;
        NEURONS_PER_LAYER = 25;
        //speed and delta angle
        NEURAL_OUTPUTS = 3;

        MUTATION_CHANCE = 0.9;

        MEMORY_NEURONS = 1;
    }

    @Override
    public double getFitness() {
        return -1 * damage;
    }

    @Override
    public void resetFitness() {
        damage = 0;
    }

    public void drive(double... ranges) {
        try {
            double[] output = process(ranges);
            this.hasSignaled = output[2] > 0.5 ;
            this.ranges = ranges;
            double oldSpeed = speed;
            speed += (2*output[0]-1)*0.01;
            speed = Math.min(speed, 2);
            speed = Math.max(speed, -0.3);
            double oldRotation = rotation;
            rotation = (rotation + (2*output[1] -1) * 0.0003 /(speed<0.1?0.1:speed) + 2* Math.PI)%(2*Math.PI);
            speed*= 0.99;


            x += Math.cos(rotation) * speed;
            y += Math.sin(rotation) * speed;
            damage -= 0.7 * speed;
            damage += Math.abs(oldRotation - rotation) < 0.1?1:0;
        } catch (Util.DimensionMismatchException e) {
            e.printStackTrace();
        }
    }

    public void collide() {
        hasCollided = true;

        damage += 1;

    }

    public void outBounds() {
        hasCollided = true;

        damage += 20;
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

    public boolean hasCollided(){
        return hasCollided;
    }

    public void resestHasCollided() {
        hasCollided = false;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double[] getRanges() {
        return ranges;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public boolean hasSignaled() {
        return hasSignaled;
    }
}
