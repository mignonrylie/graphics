import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

private class ButtonObject extends JFrame implements ActionListener {
    private JButton button;

    public ButtonObject() {
        super("MF BUTTON");
        this.setSize(300, 500);

        button = new JButton("AYO?!?!?");

        button.addActionListener(this);
        button.setForeground(Color.RED);

        Container c = getContentPane();
        c.add(button);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Button event");
    }


}