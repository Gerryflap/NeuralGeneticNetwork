package visual;

import neuralNet.EvolutionSimulation;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

/**
 * Created by gerben on 4-12-15.
 */
public class EvolutionInfoView extends JFrame {
    EvolGraphPanel graphPanel;
    EvolutionSimulation simulation;
    double maxY = Double.MIN_VALUE;
    double minY = Double.MAX_VALUE;


    public EvolutionInfoView(EvolutionSimulation simulation){
        super();
        this.simulation = simulation;
        graphPanel = new EvolGraphPanel();
        this.setContentPane(graphPanel);
        graphPanel.setDoubleBuffered(true);
        // be nice to testers..
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        this.setSize(500,500);
        this.invalidate();
        this.setVisible(true);

    }

    public void update() {
        graphPanel.addData(simulation);
        graphPanel.setVisible(false);
        graphPanel.setVisible(true);
    }

    public void setSimulation(EvolutionSimulation simulation) {
        this.simulation = simulation;
    }

    public class EvolGraphPanel extends JPanel {
        private List<Double> averages = new ArrayList<>();
        private List<Double> medians = new ArrayList<>();
        private List<Double> best = new ArrayList<>();

        public void addData(EvolutionSimulation simulation) {
            addData(averages, simulation.getAverageFitness());
            addData(medians,simulation.getMedianFitness());
            addData(best,simulation.getFittestAgent().getFitness());

        }

        private void addData(List<Double> list, double y) {
            list.add(y);
            if (list.size() > 200) {
                list.remove(0);

            }
            List<Double> sorted = new ArrayList<>(averages);
            sorted.addAll(medians);
            sorted.addAll(best);
            sorted.sort(new DoubleComparator());
            maxY = sorted.get(0);
            minY = sorted.get(sorted.size()-1);

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = getWidth();
            int h = getHeight();
            g.setColor(Color.WHITE);
            g.drawRect(0,0,w,h);
            List<Double>[] lists = new List[]{averages, medians, best};
            int[] xs = new int[averages.size()];
            int[] ys = new int[averages.size()];
            if (minY - maxY != 0){
                g.setColor(Color.RED);
                double a = 1/(maxY - minY);
                double b = -minY;
                for (int i1 = 0; i1 < lists.length; i1++) {
                    List<Double> list = lists[i1];
                    if (i1 == 1) {
                        g.setColor(Color.BLUE);

                    } else if(i1 == 2) {
                        g.setColor(Color.GREEN);

                    }
                    for (int i = 0; i < list.size(); i++) {
                        xs[i] = (int) (((double) i / list.size()) * (double) w);
                        ys[i] = h - (int) ((double) h * a * (list.get(i) + b));
                    }
                    g.drawPolyline(xs, ys, xs.length);
                }
            }

        }
    }

    class DoubleComparator implements Comparator<Double> {

        @Override
        public int compare(Double o1, Double o2) {
            return o2.equals(o1)?0:o2>o1?1:-1;
        }
    }

    }


