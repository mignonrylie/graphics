import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Hoop {
    private static int panelWidth=800;
    private static int panelHeight=800;
    //points button: toggles between displaying control points (bouncing balls)
    //hoop button: toggles display of dancing hoop
    //speed slider: controls animation speed
    //make all
    int speed = 10;
    boolean displayHoop = true;
    boolean displayPoints = true;

    //AnimCurve2 curve = new AnimCurve2();

    public Hoop() {
        BouncingBall b = new BouncingBall();
        JFrame frame = new JFrame("Bouncing Ball");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(panelWidth, panelHeight);
        //frame.setContentPane(new BouncingBall());
        frame.setContentPane(b);

        JPanel test = new JPanel();

        JButton points = new JButton("Display points");
        points.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayHoop = !displayHoop;
            }
        });
        JButton hoop = new JButton("Display hoop");
        JSlider slider = new JSlider(5, 30);
        slider.setInverted(true);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider slide = (JSlider) e.getSource();
                speed = slide.getValue();
                b.setDelay(speed);
            }
        });

        JLabel sliderLabel = new JLabel("Animation speed");
        test.add(button);
        test.add(sliderLabel);
        test.add(slider);
        frame.add(test);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Hoop();
    }
}
