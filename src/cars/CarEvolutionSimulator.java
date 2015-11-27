package cars;

import neuralNet.Agent;
import neuralNet.EvolutionSimulation;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerben on 27-11-15.
 */
public class CarEvolutionSimulator extends EvolutionSimulation{
    private double w;
    private double h;
    private int i;

    public CarEvolutionSimulator(double w, double h, int population) throws EvolvingNeuralNet.NotEnoughLayersException {
        super(population);
        this.w = w;
        this.h = h;
    }

    public void step() throws EvolvingNeuralNet.NotEnoughLayersException {
        i += 1;
        if (i > 3000) {
            i = 0;
            iterate();
        }
        for (Agent agent: agents) {
            CarAgent carAgent = (CarAgent) agent;
            double[] ranges = new double[4];
            ranges[0] = getDistanceToWall(carAgent.getRotation() - 1, carAgent.getX(), carAgent.getY());
            ranges[1] = getDistanceToWall(carAgent.getRotation(), carAgent.getX(), carAgent.getY());
            ranges[2] = getDistanceToWall(carAgent.getRotation() + 1, carAgent.getX(), carAgent.getY());
            ranges[3] = getDistanceToWall(carAgent.getRotation() + Math.PI, carAgent.getX(), carAgent.getY());

            if (Math.abs(carAgent.getSpeed()) < 0.2) {
                carAgent.collide();
            }
            if (w<carAgent.getX() || carAgent.getX()<0) {
                carAgent.collide();
            } else if (h<carAgent.getY() || carAgent.getY()<0) {
                carAgent.collide();
            }
            carAgent.drive(ranges);
        }
    }

    @Override
    public Agent generateAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
        return new CarAgent( w/2,
                h/2);
    }

    @Override
    public List<Agent> generateAgents() throws EvolvingNeuralNet.NotEnoughLayersException {
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            agents.add(generateAgent());
        }
        return agents;
    }

    @Override
    public Agent generateAgent(Agent p1, Agent p2) throws EvolvingNeuralNet.NotEnoughLayersException {
        return new CarAgent(w/2,
                h/2,
                p1, p2);
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getDistanceToWall(double rotation, double x, double y) {
        double xStep;
        double yStep;
        double distLeft = -1;
        double distRight = -1;
        double distTop = -1;
        double distBot = -1;

        if (x > w || x < 0 || y > h || y < 0) {
            return 0;
        }

        if ((xStep = Math.cos(rotation)) != 0 ) {
            distLeft = x/xStep;
            distRight = (w - x)/xStep;
        }

        if ((yStep = Math.sin(rotation)) != 0 ) {
            distTop = y/yStep;
            distBot = (h - y)/yStep;
        }

        double distX = 0;
        if (distLeft == -1) {
            distX = distRight;
        } else if (distRight == -1) {
            distX = distLeft;
        } else {
            distX = distLeft>distRight?distRight:distLeft;
        }

        double distY = 0;
        if (distTop < 0) {
            distY = distBot;
        } else if (distBot < 0) {
            distY = distTop;
        } else {
            distY = distTop>distBot?distBot:distTop;
        }
        return Math.pow(Math.pow(distX, 2) + Math.pow(distY, 2), 0.5);

    }

}
