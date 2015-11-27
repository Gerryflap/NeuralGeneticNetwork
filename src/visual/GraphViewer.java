package visual;

import javax.swing.*;

/**
 * Created by gerben on 27-11-15.
 */
public class GraphViewer extends JFrame {
    private GraphPanel graphPanel;


    public GraphViewer(){
        super();
         graphPanel = new GraphPanel();
        this.setContentPane(graphPanel);
        // be nice to testers..
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        this.setSize(500,500);
        this.invalidate();
    }

    public GraphPanel getGraphPanel(){
        return graphPanel;
    }

}
