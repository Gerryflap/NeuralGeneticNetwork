package cars;

import neuralNet.EvolvingNeuralNet;

/**
 * Created by gerben on 27-11-15.
 */
public class CarSim {

    public static void main(String[] args) {
        try {
            CarEvolutionSimulator carEvolutionSimulator = new CarEvolutionSimulator(500, 500, 40);
            CarViewer carViewer = new CarViewer(carEvolutionSimulator);
            carViewer.setVisible(true);

            while (true) {
                carEvolutionSimulator.step();
                carViewer.update();
                carViewer.setSizes(carEvolutionSimulator);
                Thread.sleep(1);
            }
        } catch (EvolvingNeuralNet.NotEnoughLayersException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
