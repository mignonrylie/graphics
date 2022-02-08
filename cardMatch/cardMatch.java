import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class cardMatch extends JFrame implements ActionListener {
    //private JFrame frame;
    //private JPanel main;
    //private JPanel menu;
    //private JPanel row1;
    //private JPanel row2;
    //private JPanel row3;
    //private JPanel row4;
    //private int delay = 3000;
    public static boolean cardFlipped = false;

    public cardMatch() {
        JFrame frame = new JFrame("card match");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel main = new JPanel();

        //Timer timer = new Timer(delay, this);
        //timer.start();

        ActionListener listener = new cardHandler();

        ImageIcon image = new ImageIcon("cardIcons/red_joker_icon.png");
        card test = new card();
        card test2 = new card(image);
        main.add(test.button);
        main.add(test2.button);
        frame.add(main);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("cardMatch event");
        System.out.println(cardFlipped);
    }

    public static void main(String[] args) {
        cardMatch c = new cardMatch();
    }
}
