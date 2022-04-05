import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class HandlePanel extends JPanel{
    final static int HANDLE_SIZE = 10;
    final static int FULL_PANEL_SIZE = 600;
    final static int PANEL_SIZE = 500;

    static int gridSize = 5;
    static int numHandles = (int)Math.pow(gridSize, 2);

    int drag = -1;
    int selected = -1;
    int panelNumber = -1;

    JPanel panel1, panel2;

    static Rectangle[][] handles;
    //5x5 (generalize) grid of control points (for each image) - correspondance via numbers?
    //nx2 array (25x2) for start and end?
    //25 parametric equations
    //set of points on the border to allow triangles on outside?

    //this function will take two HandlePanels
    public static void doMorph(HandlePanel h1, HandlePanel h2) {
        return;
    }

    public HandlePanel(JPanel panel1, JPanel panel2) {
        //panelNumber = panel;
        this.panel1 = panel1;
        this.panel2 = panel2;

        handles = new Rectangle[numHandles][2];

        //initialize all handles but only place one image's handles
        for(int i = 0; i < numHandles; i++) {
            for(int j = 0; j < 2; j++) {
                handles[i][j] = new Rectangle();
                //windowsize/gridSize * i - HANDLE_SIZE/2

            }
            handles[i][0].setRect(PANEL_SIZE/gridSize * (i/gridSize + 1) - HANDLE_SIZE/2,
                    PANEL_SIZE/gridSize * (i%gridSize + 1) - HANDLE_SIZE/2,
                    HANDLE_SIZE, HANDLE_SIZE);
            handles[i][1].setRect(PANEL_SIZE/gridSize * (i/gridSize + 1) - HANDLE_SIZE/2,
                    PANEL_SIZE/gridSize * (i%gridSize + 1) - HANDLE_SIZE/2,
                    HANDLE_SIZE, HANDLE_SIZE);
        }

        System.out.println(this.getParent());

        repaint();
    }

    public Component uhhh() {
        return this.getParent();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        for(int i = 0; i < numHandles; i++) {
            if(i == selected){
                g2D.setColor(Color.MAGENTA);
            }
            else {
                g2D.setColor(Color.BLACK);
            }
            if(this.getParent() == panel1)
                g2D.fill(handles[i][0]);
            //}
            else if (this.getParent() == panel2)
                g2D.fill(handles[i][1]);
            //}
            else
                System.out.println("this should not be possible");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(FULL_PANEL_SIZE, FULL_PANEL_SIZE);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    //mouseclick:
    //find which if any point is being clicked
    //highlight the relevant points
    public void clickHandle(int x, int y) {
        for (int i = 0; i < numHandles; i++) {
            if(handles[i][0].contains(x, y) || handles[i][1].contains(x, y)) {
                //highlight the relevant points
                //set them as the ones being moved
                System.out.println("handle grabbed");
                drag = i;
                selected = i;
                repaint();
                return;
            }
        }
    }

    //mousedrag:
    //move point around and re-render stuff
    //setRect() can be used to change the location of the rectangle
    public void moveHandle(int x, int y) {
        if(drag > -1) {
            if(this.getParent() == panel1)
                handles[drag][0].setRect(x-HANDLE_SIZE/2, y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
            else if(this.getParent() == panel2)
                handles[drag][1].setRect(x-HANDLE_SIZE/2, y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
            else
                System.out.println("this should not be possible");
            repaint();
        }
    }

    //mouseend:
    //do nothing? idk
    public void mouseDone() {
        System.out.println("done");
        //TODO: remove this
    }
}