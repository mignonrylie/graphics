import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class HandlePanel2 extends JPanel{
    final static int HANDLE_SIZE = 10;
    final static int FULL_PANEL_SIZE = 600;
    final static int PANEL_SIZE = 500;

    static int gridSize = 5;
    static int numHandles = (int)Math.pow(gridSize, 2);

    int drag = -1;
    int selected = -1;
    //int panelNumber = -1;

    //JPanel panel1, panel2;

    static Rectangle[] handles;
    //5x5 (generalize) grid of control points (for each image) - correspondance via numbers?
    //nx2 array (25x2) for start and end?
    //25 parametric equations
    //set of points on the border to allow triangles on outside?

    //this function will take two HandlePanels
    //public static void doMorph(HandlePanel h1, HandlePanel h2) {
        //return;
    //}

    public HandlePanel2() {
        //panelNumber = panel;
        //this.panel1 = panel1;
        //this.panel2 = panel2;

        handles = new Rectangle[numHandles];

        //initialize all handles but only place one image's handles
        for(int i = 0; i < numHandles; i++) {
                handles[i] = new Rectangle();
                //windowsize/gridSize * i - HANDLE_SIZE/2


            handles[i].setRect(PANEL_SIZE/gridSize * (i/gridSize + 1) - HANDLE_SIZE/2,
                    PANEL_SIZE/gridSize * (i%gridSize + 1) - HANDLE_SIZE/2,
                    HANDLE_SIZE, HANDLE_SIZE);
            //handles[i][1].setRect(PANEL_SIZE/gridSize * (i/gridSize + 1) - HANDLE_SIZE/2,
            //        PANEL_SIZE/gridSize * (i%gridSize + 1) - HANDLE_SIZE/2,
            //        HANDLE_SIZE, HANDLE_SIZE);
        }

        System.out.println(this.getParent());

        repaint();
    }

    public Component uhhh2() {
        return this.getParent();
    }

    public void paintComponent2(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        for(int i = 0; i < numHandles; i++) {
            if(i == selected){
                g2D.setColor(Color.MAGENTA);
            }
            else {
                g2D.setColor(Color.BLACK);
            }

            g2D.fill(handles[i]);
        }
    }

    public void selectHandle2(int i) {
        selected = i;
        repaint();
    }

    public Dimension getPreferredSize2() {
        return new Dimension(FULL_PANEL_SIZE, FULL_PANEL_SIZE);
    }

    public Dimension getMinimumSize2() {
        return getPreferredSize();
    }

    //mouseclick:
    //find which if any point is being clicked
    //highlight the relevant points
    public void clickHandle2(int x, int y) {
        System.out.println(this.getParent());
        for (int i = 0; i < numHandles; i++) {
            if(handles[i].contains(x, y)) {
                //highlight the relevant points
                //set them as the ones being moved
                System.out.println("handle grabbed");
                drag = i;
                //selected = i;
                HandlePanelHandler.selectHandle(i);
                repaint();
                return;
            }
        }
    }

    //mousedrag:
    //move point around and re-render stuff
    //setRect() can be used to change the location of the rectangle
    public void moveHandle2(int x, int y) {
        System.out.println(drag);
        if(drag > -1) {
            //if(this.getParent() == panel1)
                handles[drag].setRect(x-HANDLE_SIZE/2, y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
            //else if(this.getParent() == panel2)
                //handles[drag][1].setRect(x-HANDLE_SIZE/2, y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
            //else
                //System.out.println("this should not be possible");
            repaint();
        }
    }

    //mouseend:
    //do nothing? idk
    public void mouseDone2() {
        System.out.println("done");
        drag = -1;
    }
}