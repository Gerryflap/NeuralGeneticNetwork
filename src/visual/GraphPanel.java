package visual;

import neuralNet.Agent;
import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;
import sim.Example;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gerben on 27-11-15.
 */
public class GraphPanel extends JPanel {
    Agent agent;
    EvolvingNeuralNet neuralNet;
    FormulaContainer f;

    public void setFormula(FormulaContainer f){
        this.f = f;
    }
    public void setAgent(Agent agent) {
        this.agent = agent;
        this.invalidate();
        this.setVisible(false);
        this.setVisible(true);
    }

    public void setNeuralNet(EvolvingNeuralNet net) {
        this.neuralNet = net;
        this.invalidate();
        this.setVisible(false);
        this.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if((agent != null || neuralNet != null) && f != null) {
            Agent agent_ = agent;
            g.setColor(new Color(255,255,255));
            int w = getWidth();
            int h = getHeight();
            g.fillRect(0, 0, w, h);
            int[] xs = new int[w];
            int[] ys = new int[w];
            int[] ygs = new int[w];
            for (int x = 0; x < w; x++) {
                xs[x] = x;
                ygs[x] = (int) f.f(scaleTranform(x, w/4, 0));
                try {
                    if (agent_ != null) {
                        ys[x] = (int) (agent_.process(scaleTranform(x, w/4, 0))[0]);
                    } else {
                        ys[x] = (int) (1000.0 * neuralNet.process(new double[]{scaleTranform(x, w/4, 0)})[0]);
                    }
                } catch (Util.DimensionMismatchException e) {
                    e.printStackTrace();
                }
            }
            g.setColor(new Color(255,0,0));
            g.drawPolyline(xs, ys, xs.length);
            g.setColor(new Color(0,255,0));
            g.drawPolyline(xs, ygs, xs.length);
        }
    }

    public double scaleTranform(double x, double zoom, double offset) {
        return ((x - offset)/zoom);
    }
}
