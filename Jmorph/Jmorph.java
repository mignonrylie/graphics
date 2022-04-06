import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class Jmorph extends JFrame{
    final static int MIN_TWEEN = 1;
    final static int MAX_TWEEN = 60;

    private HandlePanelHandler h;


    //static JFrame frame;
    static JSpinner tweens;





    //user controls
    //image 1
    //image 2

    //number of tweens
    //preview button (new window?)
    private void createLayout() {
        //frame = new JFrame();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openFileStart = new JMenuItem("Open Start Image");
        JMenuItem openFileEnd = new JMenuItem("Open End Image");

        JPanel holder = new JPanel();
        JPanel imagePanel1 = new JPanel();
        JPanel imagePanel2 = new JPanel();
        JPanel controlPanel = new JPanel();


        //holder.setLayout(new BoxLayout(holder, BoxLayout.PAGE_AXIS));

        tweens = new JSpinner(new SpinnerNumberModel(MAX_TWEEN/2, MIN_TWEEN, MAX_TWEEN, 1));
        //JSlider gridSlider = new JSlider(1, 10);
        JTextPane test = new JTextPane();
        test.setText("fuck");

        openFileStart.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //TODO: set open file start
                    }
                }
        );

        openFileEnd.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //TODO: set open file end
                    }
                }
        );

        tweens.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        //TODO: set tween value
                    }
                }
        );



        //add mouse listeners to detect when trying to click point
        //on mouse click
            //function should find which, if any, point is being clicked
            //highlight the relevant points
        //on mouse drag
            //function should move point around and re-render stuff
            //setRect() can be used to change the location of the rectangle
        //on mouse end
            //maybe do nothing idk

        controlPanel.add(tweens);
        controlPanel.add(test);
        //frame.add(controlPanel);
        //add(controlPanel);



        h = new HandlePanelHandler();

        imagePanel1.add(h.getChild(1));
        imagePanel2.add(h.getChild(2));


        holder.add(imagePanel1, BorderLayout.LINE_START);
        holder.add(test, BorderLayout.CENTER);
        holder.add(imagePanel2, BorderLayout.LINE_END);

        add(holder);
        //add(imagePanel1);
        //add(imagePanel2);
        //add(h);
        //frame.pack();
        //frame.setVisible(true);
        pack();
        setVisible(true);

        //frame.repaint();
        repaint();


    }

    public Jmorph() {
        createLayout();


    }

    public static void main(String[] args) {
        Jmorph morph = new Jmorph();
    }
}
