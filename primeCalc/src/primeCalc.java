import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class primeCalc extends JFrame implements ActionListener {

    static JButton button;
    static JTextField text;
    static JLabel label;

    primeCalc() {
        JFrame frame = new JFrame("Prime Number Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        label = new JLabel("Enter a number between 0 and 999,999 to see if it's prime.");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        text = new JTextField(20);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);

        button = new JButton("Calculate");
        button.addActionListener(this);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(text);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(10, 10)));


        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!text.getText().matches("\\d+")) {
            label.setText("Please input a positive integer!");
        }
        else {
            int num = Integer.parseInt(text.getText());
            if(num >= 1000000) {
                label.setText("That number is too large!");
            }
            else {
                if(sieveHistogram.isPrime(num)) {
                    label.setText(num + " is prime.");
                }
                else {
                    label.setText(num + " is not prime.");
                }
            }
        }
    }

    public static void main(String[] args) {
        new primeCalc();
    }
}