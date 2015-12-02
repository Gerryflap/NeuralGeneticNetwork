package sim;

import neuralNet.EvolvingNeuralNet;
import visual.FormulaContainer;
import visual.GraphPanel;
import visual.GraphViewer;

/**
 * Created by gerben on 27-11-15.
 */
public class NewExample implements FormulaContainer {

    public static void main(String[] args) {
        NewEvolutionSimulator simulator = null;
        try {
            simulator = new NewEvolutionSimulator(20);
            GraphViewer viewer = new GraphViewer();
            viewer.getGraphPanel().setFormula(new NewExample());
            viewer.setVisible(true);

            while (true) {
                simulator.iterate();
                viewer.getGraphPanel().setAgent(simulator.getFittestAgent());
            }

        } catch (EvolvingNeuralNet.NotEnoughLayersException e) {
            e.printStackTrace();
        }
    }


    public static double fStat(double x) {
        return 100*Math.pow(x, 2);
    }

    public double f(double x) {
        return fStat(x);
    }
}
