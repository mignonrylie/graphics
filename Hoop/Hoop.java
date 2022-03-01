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

    //AnimCurve2 curve = new AnimCurve2();

    public Hoop() {
        JFrame frame = new JFrame("Bouncing Ball");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(panelWidth, panelHeight);
        frame.setContentPane(new BouncingBall());

        JPanel test = new JPanel();
        JButton button = new JButton("click me");
        test.add(button);
        frame.add(test);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Hoop();
    }
}
