package imageGen;

import neuralNet.EvolutionSimulation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by gerben on 27-12-15.
 */
public class IterateButton extends JButton implements ActionListener {
    EvolutionView evolutionView;

    public IterateButton(EvolutionView evolutionView) {
        super();
        setText("Iterate");
        this.evolutionView = evolutionView;
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        evolutionView.iterate();
    }
}
