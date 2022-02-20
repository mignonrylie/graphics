import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Cell extends JFrame implements ActionListener {
    /*
    private enum state { //may not use
        ZERO,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT
        DEFAULT, //unopened
        FLAGGED, //flagged
    };
     */

    private String[] files = {"assets/default.png", "assets/flagged.png", "assets/bomb.png",
            "assets/0.png", "assets/1.png", "assets/2.png", "assets/3.png", "assets/4.png",
            "assets/5.png", "assets/6.png", "assets/7.png", "assets/8.png"};

    private boolean bomb;
    private boolean flag;
    private boolean open;
    private JButton button;
    private int numBombs; //number of bombs in neighboring cells
    private ImageIcon img;

    private int scale = 40;


    //button.setMargin(new Insets(0,0,0,0))




    //action handler
    //left click vs right click

    public Cell() {
        button = new JButton();
        button.setMargin(new Insets(0, 0, 0, 0));
        img = new ImageIcon(files[0]);
        img = img.getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        button.setIcon(img);
        button.addActionListener(this);
    }

    public void setNumBombs(int numBombs) {
        this.numBombs = numBombs;
    }

    public JButton getButton() {
        return button;
    }

    private void reveal() {
        img = new ImageIcon(files[3]);
        img = img.getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        button.setIcon(img);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        reveal();
        System.out.println("button event");
    }
}
