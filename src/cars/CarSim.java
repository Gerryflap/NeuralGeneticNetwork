package cars;

import neuralNet.EvolvingNeuralNet;

/**
 * Created by gerben on 27-11-15.
 */
public class CarSim {

    public static void main(String[] args) {
        try {
            CarEvolutionSimulator carEvolutionSimulator = new CarEvolutionSimulator(500, 500, 20);
            CarViewer carViewer = new CarViewer(carEvolutionSimulator);
            carViewer.setVisible(true);

            while (true) {
                carEvolutionSimulator.step();
                if (carViewer.shouldSleep()) {
                    Thread.sleep(1);
                    carViewer.update();
                    carViewer.setSizes(carEvolutionSimulator);
                }
            }
        } catch (EvolvingNeuralNet.NotEnoughLayersException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
