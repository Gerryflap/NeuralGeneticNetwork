package imageGen;

import neuralNet.Agent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by gerben on 27-12-15.
 */
public class GeneratedImageView extends JButton implements MouseListener{
    BufferedImage image;
    boolean selected = false;
    ImageGenAgent agent;
    JFrame rootFrame;

    public GeneratedImageView(ImageGenAgent agent, JFrame frame) {
        super();
        rootFrame = frame;
        setBackground(new Color(200,200, 200));
        this.agent = agent;
        if (agent != null) {
            image = new GeneratedImage(frame , agent, 300, 200);
        } else {
            image = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        }
        setIcon(new ImageIcon(image));
        addMouseListener(this);
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            selected = !selected;
            setBackground(new Color(200, 200, selected ? 100 : 200));
        } else {
            File file = new File("./savedImg.bmp");
            GeneratedImage image = new GeneratedImage(rootFrame, agent, 1920 * 2, 1080 * 2);
            try {
                ImageIO.write(image, "bmp", file);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void gradeAgent() {
        if (agent != null) {
            if(selected) {
                agent.select();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }



    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
