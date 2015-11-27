package visual;

import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;
import sim.Example;

import javax.swing.*;
import java.awt.*;

/**
 * Created by gerben on 27-11-15.
 */
public class GraphPanel extends JPanel {
    EvolvingNeuralNet net;

    public void setNet(EvolvingNeuralNet net) {
        this.net = net;
        this.invalidate();
        this.setVisible(false);
        this.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(net != null) {
            EvolvingNeuralNet net_ = net;
            g.setColor(new Color(255,255,255));
            int w = getWidth();
            int h = getHeight();
            g.fillRect(0, 0, w, h);
            int[] xs = new int[w];
            int[] ys = new int[w];
            int[] ygs = new int[w];
            for (int x = 0; x < w; x++) {
                xs[x] = x;
                ygs[x] = (int) Example.f(scaleTranform(x, 20, w/2));
                try {
                    ys[x] = (int) (100.0 * net_.process(new double[]{scaleTranform(x, 20, w/2)})[0]);
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
