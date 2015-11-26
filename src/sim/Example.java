package sim;

import neuralNet.EvolvingNeuralNet;
import neuralNet.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gerben on 26-11-15.
 */
public class Example {
    private int population;
    private List<Guesser> world;


    public static void main(String[] args) {
        Example example = new Example(4);
        for (int i = 0; i < 1000000; i++) {
            example.generate();
        }

        System.out.println(example.getWorld().get(0).getChanceOfSurvival());
        System.out.println(example.test(2));
        System.out.println(example.test(3));
        System.out.println(example.test(-1));
        System.out.println(example.test(10));
    }

    public Example(int population) {
        this.population = population;
        world = new ArrayList<>();
        for (int i = 0; i < population; i++) {
            world.add(new Guesser());
        }

    }

    public void generate() {
        List<Guesser> selected = new ArrayList<>();
        List<Guesser> nextWorld = new ArrayList<>();

        for (int i = 0; i < world.size(); i++) {
            if(selected.size() < 2){
                if (selected.size() == 1) {
                    if (world.get(i).getChanceOfSurvival() > selected.get(0).getChanceOfSurvival()) {
                        selected.add(0, world.get(i));
                    } else {
                        selected.add(world.get(i));
                    }
                } else {
                    selected.add(world.get(i));
                }
            } else {
                if (selected.get(1).getChanceOfSurvival() < world.get(i).getChanceOfSurvival()) {
                    if (selected.get(0).getChanceOfSurvival() < world.get(i).getChanceOfSurvival()) {
                        selected.remove(1);
                        selected.add(0, world.get(i));
                    } else {
                        selected.remove(1);
                        selected.add(world.get(i));
                    }
                }
            }
        }

        for (int i = 0; i < world.size(); i++) {
            if (!selected.contains(world.get(i))) {
                if (Guesser.random.nextDouble() < world.get(i).getChanceOfSurvival()) {
                    selected.add(world.get(i));
                }
            }

        }

        for (int i = 0; i < population; i++) {
            Guesser p1 = selected.get(0);
            Guesser p2 = selected.get(1);
            nextWorld.add(new Guesser(p1, p2));
        }
        //System.out.println(world);
        world = nextWorld;
    }

    public static double f(double x) {
        return x*x;
    }

    public String test(double x) {
        try {
            double out = world.get(0).getNN().process(new double[] {x})[0];
            return "Expected outcome: " + f(x) +", our outcome: " + out;
        } catch (Util.DimensionMismatchException e) {
            e.printStackTrace();
        }
        return "";
    }

    public List<Guesser> getWorld() {
        return world;
    }
}
