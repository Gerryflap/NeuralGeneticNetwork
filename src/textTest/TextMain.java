package textTest;

import neuralNet.EvolvingNeuralNet;
import visual.EvolutionInfoView;

/**
 * Created by gerben on 14-12-15.
 */
public class TextMain {

    public static void main(String[] args) throws EvolvingNeuralNet.NotEnoughLayersException {
        TextEvolver textEvolver = new TextEvolver(1000);
        EvolutionInfoView view = new EvolutionInfoView(textEvolver);
        view.setVisible(true);
        while (true) {
            textEvolver.iterate();
            System.out.println(textEvolver.getFittestAgent().getFitness());
            view.update();
        }
    }
}
