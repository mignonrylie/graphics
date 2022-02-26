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

    private int row;
    private int col;

    private int scale = 50;


    //button.setMargin(new Insets(0,0,0,0))




    //action handler
    //left click vs right click

    public Cell(int row, int col, int scale) {
        this.scale = scale;
        this.row = row;
        this.col = col;
        bomb = false;
        flag = false;
        open = false;
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
        bomb = !bomb;
    }

    public boolean getBomb() {
        return bomb;
    }

    public void setNumBombs(int numBombs) {
        this.numBombs = numBombs;
    }

    public int getNumBombs() {
        return numBombs;
    }

    public void incBombs() {
        numBombs++;
    }

    public JButton getButton() {
        return button;
    }

    public boolean getFlag() {
        return flag;
    }

    public boolean getOpen() {
        return open;
    }

    public void reveal() {
        if(bomb) {
            img = new ImageIcon(files[2]);
        }
        else {
            img = new ImageIcon(files[numBombs+3]);
        }
        open = true;
        Image scaleimg = img.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        img = new ImageIcon(scaleimg);
        button.setIcon(img);
    }

    private void flag() {
        if(Minesweeper.lock) {
            return;
        }
        if(flag) {
            flag = false;
            img = new ImageIcon(files[0]);
            Image scaleimg = img.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
            img = new ImageIcon(scaleimg);
            button.setIcon(img);
            if(bomb) {
                Minesweeper.numFlagged(-1, -1);
            }
            else {
                Minesweeper.numFlagged(-1, 0);
            }
        }
        else {
            flag = true;
            img = new ImageIcon(files[1]);
            Image scaleimg = img.getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
            img = new ImageIcon(scaleimg);
            button.setIcon(img);
            if(bomb) {
                Minesweeper.numFlagged(1, 1);
            }
            else {
                Minesweeper.numFlagged(1, 0);
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent m) {
        if(m.getButton() == MouseEvent.BUTTON1) {
            Minesweeper.openTiles(row, col);
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
