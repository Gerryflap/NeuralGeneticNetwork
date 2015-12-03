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
        List<double[]> allRanges = new ArrayList<double[]>();
        if (i > 3000) {
            i = 0;
            System.out.printf("Average fitness: %f, Highest fitness: %f\n", getAverageFitness(), getFittestAgent().getFitness());
            iterate();
            placeAgentsInGrid();
        }
        for (Agent agent: agents) {
            CarAgent carAgent = (CarAgent) agent;
            carAgent.resestHasCollided();
            double[] ranges = new double[11];
            double xOffset = Math.cos(carAgent.getRotation()) * 30;
            double yOffset = Math.sin(carAgent.getRotation()) * 30;
            ranges[9] = getDistanceToCollision(carAgent.getRotation() - 2, carAgent.getX() + xOffset, carAgent.getY() + yOffset);
            ranges[0] = getDistanceToCollision(carAgent.getRotation() - 1, carAgent.getX() + xOffset, carAgent.getY() + yOffset);
            ranges[7] = getDistanceToCollision(carAgent.getRotation() - 0.5, carAgent.getX() + xOffset, carAgent.getY() + yOffset);
            ranges[1] = getDistanceToCollision(carAgent.getRotation(), carAgent.getX() + xOffset, carAgent.getY() + yOffset);
            ranges[8] = getDistanceToCollision(carAgent.getRotation() + 0.5, carAgent.getX() + xOffset, carAgent.getY() + yOffset);
            ranges[2] = getDistanceToCollision(carAgent.getRotation() + 1, carAgent.getX()+ xOffset, carAgent.getY()+ yOffset);
            ranges[3] = getDistanceToCollision(carAgent.getRotation() + Math.PI, carAgent.getX() - xOffset, carAgent.getY() - yOffset);
            ranges[10] = getDistanceToCollision(carAgent.getRotation() + 2, carAgent.getX() + xOffset, carAgent.getY() + yOffset);
            for (Agent agent1: agents) {
                CarAgent carAgent1 = (CarAgent) agent1;
                if (agent!=agent1) {
                    double sqrDist = Math.pow(carAgent.getX() - carAgent1.getX(), 2) + Math.pow(carAgent.getY() - carAgent1.getY(), 2);
                    if (sqrDist < 200 * 200) {
                        ranges[5] += 1.0;
                        if (sqrDist < 60 * 60) {
                            carAgent.collide();
                            carAgent1.collide();
                            /**
                             if (carAgent.getSpeed() > carAgent1.getSpeed()) {
                             carAgent.collide();
                             } else {
                             carAgent1.collide();
                             } **/
                        }
                    }
                }
            }
            /**if (Math.abs(carAgent.getSpeed()) < 0.01) {
                carAgent.collide();
            }*/
            if (w<carAgent.getX() || carAgent.getX()<0) {
                carAgent.outBounds();
            } else if (h<carAgent.getY() || carAgent.getY()<0) {
                carAgent.outBounds();
            }
            ranges[4] = carAgent.getSpeed();

            allRanges.add(ranges);
        }

        for (int j = 0; j < agents.size(); j++) {
            ((CarAgent) agents.get(j)).drive(allRanges.get(j));
        }
    }

    @Override
    public Agent generateAgent() throws EvolvingNeuralNet.NotEnoughLayersException {
        return new CarAgent(Util.random.nextDouble() * w,
                Util.random.nextDouble() * h);
    }

    @Override
    public List<Agent> generateAgents() throws EvolvingNeuralNet.NotEnoughLayersException {
        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            CarAgent carAgent = (CarAgent) generateAgent();
            agents.add(carAgent);
        }
        return agents;
    }

    @Override
    public Agent generateAgent(Agent p1, Agent p2) throws EvolvingNeuralNet.NotEnoughLayersException {
        return new CarAgent(Util.random.nextDouble() * w,
                Util.random.nextDouble() * h,
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

    public double getDistanceToCollision(double rotation, double x, double y) {

        double sqrDist = 0;
        double minSqrDist = (30000 * 30000);


        if (x >= w || x <= 0 || y >= h || y <= 0) {
            return 0;
        }

        for(Agent agent : agents){
            CarAgent carAgent = (CarAgent) agent;
            sqrDist = Math.pow(x - carAgent.getX(),2) + Math.pow(y - carAgent.getY(),2) - 30*30;
            if (sqrDist < minSqrDist && sqrDist != 0) {
                double angle = Math.atan2(-(y - carAgent.getY()),-(x - carAgent.getX()));
                //System.out.printf("Delta rotation: %f, Allowed delta: %f\n",Math.abs(rotation - angle) , Math.atan(60.0*0.5/Math.pow(sqrDist, 0.5)));
                if (Math.abs(rotation - angle)%(2*Math.PI) < Math.atan(60.0*0.5/Math.pow(sqrDist, 0.5))) {
                    minSqrDist = sqrDist;
                }
            }
        }


        double distX = 0;
        if (Math.cos(rotation) > 0) {
            distX = (w - x);
        } else {
            distX = x;
        }

        double distY = 0;

        if (Math.sin(rotation) > 0) {
            distY = (h - y);
        } else {
            distY = y;
        }


        double distWalls;
        if (distX/Math.abs(Math.cos(rotation)) > distY/Math.abs(Math.sin(rotation))) {
            distWalls = distY/Math.abs(Math.sin(rotation));
        } else  {
            distWalls = distX/Math.abs(Math.cos(rotation));
        }


        //System.out.println("X: "+ x);
        //System.out.println("DistX: "+ distX + "distY: " + distY + "DistWalls: " + distWalls);

        //System.out.println("dw: " +distWalls);
        //System.out.println("do: "+ Math.sqrt(sqrDist));
        //System.out.println("dchosen: " + Math.min(distWalls, Math.pow(minSqrDist, 0.5)));

        if (distWalls > Math.sqrt(minSqrDist)) {
            //System.out.println("Car detected! " + Math.sqrt(minSqrDist));
        } else {
            //System.out.println("Car too far away! " + Math.sqrt(minSqrDist));

        }
        return Math.min(distWalls
                , Math.pow(minSqrDist, 0.5))/100.0;

    }

    public void placeAgentsInGrid() {
        for (int i = 0; i < agents.size(); i++) {

            CarAgent carAgent = (CarAgent) agents.get(i);
            carAgent.setRotation(Util.random.nextDouble()* Math.PI * 2);
            carAgent.setX(i * 200 % w);
            carAgent.setY((i * 200 / w) * 200);
        }
    }

}
