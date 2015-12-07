package cars;

import neuralNet.Agent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by gerben on 27-11-15.
 */
public class CarViewer extends JFrame implements ItemListener {
    CarPanel carPanel;
    JToggleButton radioButton;
    JPanel jPanel;
    private boolean shouldSleep = false;

    public CarViewer(CarEvolutionSimulator simulator) {
        super();
        jPanel = new JPanel();
        carPanel = new CarPanel(simulator);
        radioButton = new JToggleButton("Superspeed?");

        this.setContentPane(carPanel);
        //carPanel.add(carPanel);
        radioButton.addItemListener(this);
        carPanel.add(radioButton);
        carPanel.setSize(500,500);
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

    public boolean shouldSleep() {
        return shouldSleep;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            shouldSleep = true;
        } else if (e.getStateChange() == ItemEvent.DESELECTED) {
            shouldSleep = false;
        }

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
                int s = carAgent.hasSignaled()?255:0;
                if (carAgent.hasCollided()) {
                    g.setColor(new Color(255, s, 0));
                } else {
                    g.setColor(new Color(0, s, 255));

                }
                g.fillPolygon(carAgent.getXs(), carAgent.getYs(), 4);
                g.setColor(Color.BLACK);

                double x = carAgent.getX() + Math.cos(carAgent.getRotation()) * 30;
                double y = carAgent.getY() + Math.sin(carAgent.getRotation()) * 30;
                for (int i = 0; i < 3; i++) {
                    g.drawLine((int) x,
                            (int) y,
                            (int) (x
                                    + Math.cos(carAgent.getRotation() + i - 1)
                                    * carAgent.getRanges()[i]*100),
                            (int) (y
                                    + Math.sin(carAgent.getRotation() + i - 1)
                                    * carAgent.getRanges()[i]*100));

                }

            }


        }

        public void update(){
            setVisible(false);
            setVisible(true);
        }
    }
}
