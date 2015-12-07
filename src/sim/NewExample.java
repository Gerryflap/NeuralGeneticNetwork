package sim;

import neuralNet.EvolvingNeuralNet;
import visual.EvolutionInfoView;
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
            simulator = new NewEvolutionSimulator(200);
            GraphViewer viewer = new GraphViewer();
            EvolutionInfoView evolutionInfoView = new EvolutionInfoView(simulator);
            viewer.getGraphPanel().setFormula(new NewExample());
            viewer.setVisible(true);

            while (true) {
                simulator.iterate();
                viewer.getGraphPanel().setAgent(simulator.getFittestAgent());
                evolutionInfoView.update();
            }

        } catch (EvolvingNeuralNet.NotEnoughLayersException e) {
            e.printStackTrace();
        }
    }


    public static double fStat(double x) {
        return 50.0 * Math.sin(x*20.0) + 120;
    }

    public double f(double x) {
        return fStat(x);
    }
}
