import java.awt.*;
import javax.swing.*;

//TODO: make each ball a set speed?

public class BouncingBall extends JPanel {
    int numBalls = 4;
    private Ball[] b = new Ball[numBalls];
    private int delay=10;
    private double speed=10.0;
    private static int panelWidth=800;
    private static int panelHeight=800;

    Bspline curve = new Bspline();

    //(float)(speed*Math.random()) random speed

    public BouncingBall() {
        for(int i = 0; i < numBalls; i++) {
            b[i] = new Ball(panelWidth, panelHeight, //panelWidth+, panelHeight+
                    (float)(speed*Math.random()),
                    (float)(speed*Math.random()),
                    new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));

            //curve.addPoint(b[i].getX(), b[i].getY());
        }

        // Set up the bouncing ball with random speeds and colors
    /*b = new Ball(panelWidth,panelHeight,
                (float)(speed*Math.random()), 
                (float)(speed*Math.random()), 
        new Color((float)Math.random(), (float)Math.random(), (float)Math.random()));

     */

        // Create a thread to update the animation and repaint
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    curve.resetCurve();
                    // Ask the ball to move itself and then repaint
                    for(int i = 0; i < numBalls; i++) {
                        b[i].moveBall();

                        curve.addPoint(b[i].getX(), b[i].getY());
                    }




                    //b.moveBall();
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
        // Draw the ball
        for(int i = 0; i < numBalls; i++) {
            b[i].paintBall(g);
        }
        curve.paintCurve(g);
        //curve.drawPolyline(g);
        repaint();
        //b.paintBall(g);
    }
  /*
  public static void main(String[] args) {
    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("Bouncing Ball");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(panelWidth, panelHeight);
    frame.setContentPane(new BouncingBall());
    frame.setVisible(true);
  }

   */
}
