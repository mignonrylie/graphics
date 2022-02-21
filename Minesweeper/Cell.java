import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//add question mark flag?

public class Cell extends JFrame implements MouseListener {
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

    private int scale = 50;


    //button.setMargin(new Insets(0,0,0,0))




    //action handler
    //left click vs right click

    public Cell() {
        bomb = false;
        flag = false;
        button = new JButton();
        button.setMargin(new Insets(0, 0, 0, 0));
        img = new ImageIcon(files[0]);
        Image scaleimg = img.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        img = new ImageIcon(scaleimg);
        button.setIcon(img);
        //button.addActionListener(this);
        button.addMouseListener(this);
    }

    public void setBomb() {
        bomb = true;
    }

    public void setNumBombs(int numBombs) {
        this.numBombs = numBombs;
    }

    public void incBombs() {
        numBombs++;
    }

    public JButton getButton() {
        return button;
    }

    private void reveal() {
        if(bomb) {
            //show bomb
            //game over
            img = new ImageIcon(files[2]);
        }
        else {
            img = new ImageIcon(files[numBombs+3]);
        }



        Image scaleimg = img.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        img = new ImageIcon(scaleimg);
        button.setIcon(img);
    }

    private void flag() {
        System.out.println("Flag");
        if(flag) {
            flag = false;
            img = new ImageIcon(files[0]);
            Image scaleimg = img.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
            img = new ImageIcon(scaleimg);
            button.setIcon(img);
        }
        else {
            flag = true;
            img = new ImageIcon(files[1]);
            Image scaleimg = img.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
            img = new ImageIcon(scaleimg);
            button.setIcon(img);
        }

    }

    @Override
    public void mouseClicked(MouseEvent m) {
        if(m.getButton() == MouseEvent.BUTTON1) {
            reveal();
        }
        else if(m.getButton() == MouseEvent.BUTTON3) {
            flag();
        }
    }


    public void mouseEntered(MouseEvent m) {
    }

    public void mouseExited(MouseEvent m) {
    }

    public void mousePressed(MouseEvent m) {
    }

    public void mouseReleased(MouseEvent m) {
    }



    /*
    @Override
    public void actionPerformed(ActionEvent e) {
        reveal();
        System.out.println("button event");
    }
     */
}
