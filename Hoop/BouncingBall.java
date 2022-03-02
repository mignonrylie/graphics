import java.awt.*;
import javax.swing.*;

//changes to this file:
//made array of balls instead of single one
//added Bspline object and commands to paint it
//added booleans to decide whether or not to render objects
//added setDelay function

public class BouncingBall extends JPanel {
    int numBalls = 4;
    private Ball[] b = new Ball[numBalls];
    private int delay=10;
    private double speed=10.0;
    private static int panelWidth=800;
    private static int panelHeight=800;

    Bspline curve = new Bspline();
    boolean showHoop = true;
    boolean showPoints = true;

    public BouncingBall() {
        for(int i = 0; i < numBalls; i++) {
            //initialize point with boundaries = screen size, random speed, and color black
            b[i] = new Ball(panelWidth, panelHeight,
                    (float)(speed*Math.random()),
                    (float)(speed*Math.random()),
                    new Color(0, 0, 0));
            //randomly place point on screen
            b[i].setX((int)(panelWidth*Math.random()));
            b[i].setY((int)(panelHeight*Math.random()));
        }

        // Create a thread to update the animation and repaint
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    curve.resetCurve();
                    //move each point then generate the corresponding curve
                    for(int i = 0; i < numBalls; i++) {
                        b[i].moveBall();
                        curve.addPoint(b[i].getX(), b[i].getY());
                    }

                    repaint();
                    getToolkit().sync(); //had to add because of stutter
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ex) { }
                }
            }
        };
        thread.start();
    }

    public void setDelay(int d) {
        this.delay = d;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //only draw the points if toggled
        if(showPoints) {
            for(int i = 0; i < numBalls; i++) {
                b[i].paintBall(g);
            }
        }

        //only paint the curve if toggled
        if(showHoop) {
            curve.paintCurve(g);
        }

        repaint();
    }
}
