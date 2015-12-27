package imageGen;


import neuralNet.EvolvingNeuralNet;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by gerben on 27-12-15.
 */
public class EvolutionView extends JFrame {
    JScrollPane scrollPane;
    JPanel outerPanel;
    JPanel panel = new JPanel();
    ImageGenEvoSim evoSim;

    public EvolutionView() {
        super();

        outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.PAGE_AXIS));
        setTitle("Genetic NN Image generator");

        try {
            evoSim = new ImageGenEvoSim(4);
        } catch (EvolvingNeuralNet.NotEnoughLayersException e) {
            e.printStackTrace();
        }
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        scrollPane = new JScrollPane(panel);
        /**
        for (int i = 0; i < 10; i++) {
            GeneratedImageView added = new GeneratedImageView(null);
            //added.setIcon(new ImageIcon(new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB)));
            panel.add(added);
            //added.setVisible(true);
        }
         */

        for (int i = 0; i < evoSim.getAgents().size(); i++) {
            ImageGenAgent agent = (ImageGenAgent) evoSim.getAgents().get(i);
            panel.add(new GeneratedImageView(agent, this));
        }
        outerPanel.add(new IterateButton(this));
        scrollPane.setVisible(true);
        outerPanel.add(scrollPane);
        this.setContentPane(outerPanel);
        // be nice to testers..
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        this.setSize(1000, 700);
        this.invalidate();
        this.setVisible(true);
    }

    public void iterate() {
        for (int i = 0; i < panel.getComponentCount(); i++) {
            Component component = panel.getComponent(i);
            if (component instanceof GeneratedImageView) {
                GeneratedImageView imageView = (GeneratedImageView) component;
                imageView.gradeAgent();
            }
        }
        panel.removeAll();

        try {
            evoSim.iterate();
        } catch (EvolvingNeuralNet.NotEnoughLayersException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < evoSim.getAgents().size(); i++) {
            ImageGenAgent agent = (ImageGenAgent) evoSim.getAgents().get(i);
            panel.add(new GeneratedImageView(agent, this));
        }
        panel.setVisible(false);
        panel.setVisible(true);
    }


    public static void main(String[] args) {
        EvolutionView evolutionView = new EvolutionView();
        evolutionView.setVisible(true);
    }
}
