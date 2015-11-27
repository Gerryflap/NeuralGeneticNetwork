package cars;

import neuralNet.Agent;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gerben on 27-11-15.
 */
public class CarViewer extends JFrame {
    CarPanel carPanel;

    public CarViewer(CarEvolutionSimulator simulator) {
        super();
        carPanel = new CarPanel(simulator);
        this.setContentPane(carPanel);
        // be nice to testers..
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        this.setSize(500,500);
        this.invalidate();
    }

    public void setSizes(CarEvolutionSimulator simulator) {
        simulator.setW(getWidth());
        simulator.setH(getHeight());
    }

    public void update(){
        carPanel.update();
    }
    public class CarPanel extends JPanel {
        CarEvolutionSimulator simulator;

        public CarPanel(CarEvolutionSimulator simulator) {
            this.simulator = simulator;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(255,255,255));
            g.fillRect(0, 0, getWidth(), getHeight());
            for (Agent agent : simulator.getAgents()){
                CarAgent carAgent = (CarAgent) agent;
                g.setColor(new Color(255,0,0));
                g.fillPolygon(carAgent.getXs(), carAgent.getYs(), 4);
            }


        }

        public void update(){
            setVisible(false);
            setVisible(true);
        }
    }
}
