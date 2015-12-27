package imageGen;

import neuralNet.Util;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * Created by gerben on 27-12-15.
 */
public class GeneratedImage extends BufferedImage {

    public GeneratedImage(JFrame rootFrame, ImageGenAgent agent, int width, int height) {
        super(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < getWidth(); x++) {


            if (rootFrame != null) {
                rootFrame.setTitle("Genetic NN Image generator: Rendering x " + x);
            }


            for (int y = 0; y < getHeight(); y++) {
                try {
                    double inX = 500.0 * (double) x/ getWidth();
                    double inY = 500.0 * (double) y / getHeight();
                    setRGB(x,y,getRGBfromArray(agent.process(inX,
                            inY,
                            Math.sqrt(inX*inX + inY*inY),
                            156)));
                } catch (Util.DimensionMismatchException e) {
                    e.printStackTrace();
                }
            }
        }
        if (rootFrame != null) {
            rootFrame.setTitle("Genetic NN Image generator");
        }

    }

    public static int getRGBfromArray(double[] rgb) {
        return (int) (Math.pow(255, 3) * rgb[0]
                        + Math.pow(255,2) * rgb[1]
                        + Math.pow(255,1) * rgb[2]);

    }
}
