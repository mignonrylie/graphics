import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Hoop {
    private static int panelWidth=800;
    private static int panelHeight=800;
    int speed = 10;
    BouncingBall b;

    public Hoop() {
        b = new BouncingBall();

        //create window and add animation to it
        JFrame frame = new JFrame("Bouncing Ball");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(panelWidth, panelHeight);
        frame.setContentPane(b);

        //create panel to hold menu objects
        JPanel test = new JPanel();

        //button to toggle displaying points
        JButton points = new JButton("Display points");
        points.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //TODO: make private variable w setter method?
                b.showPoints = !b.showPoints;
            }
        });

        //button to toggle displaying hoop
        JButton hoop = new JButton("Display hoop");
        hoop.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               b.showHoop = !b.showHoop;
           }
        });

        //slider to control animation speed
        JSlider slider = new JSlider(5, 30);
        slider.setInverted(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider slide = (JSlider) e.getSource();
                speed = slide.getValue();
                b.setDelay(speed);
            }
        });

        //label for slider
        JLabel sliderLabel = new JLabel("Animation speed");

        test.add(hoop);
        test.add(points);
        test.add(sliderLabel);
        test.add(slider);

        frame.add(test);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Hoop();
    }
}
