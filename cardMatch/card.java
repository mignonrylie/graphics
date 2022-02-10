import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//TODO: make members private and implement setters & getters
//TODO: set suits

public class card implements ActionListener {
    public enum suit {
        HEART,
        DIAMOND,
        CLUB,
        SPADE
    };

    public enum state {
        FACE,
        BACK
    };

    private int rank;
    private int suit; //not necessary?
    private state state;
    public JButton button;
    private ImageIcon face;
    private ImageIcon back;
    public boolean matched = false;

    private String defaultFace = "cardIcons/black_joker_icon.png";
    private String defaultBack = "cardIcons/back.png";

    //default constructor, in case no image is passed in
    public card() {
        face = new ImageIcon(defaultFace);
        back = new ImageIcon(defaultBack);

        button = new JButton();
        button.setIcon(face);
        button.addActionListener(this);
        state = state.FACE;

        rank = 0;
    }

    //overloaded constructor, meant to pass in the image
    public card(ImageIcon image) {
        face = image;
        back = new ImageIcon(defaultBack);

        button = new JButton();
        button.setIcon(back);
        button.addActionListener(this);
        state = state.BACK;

        rank = 0;
    }

    //overloaded constructor, meant to pass in the image and rank
    public card(ImageIcon image, int rank) {
        face = image;
        back = new ImageIcon(defaultBack);

        button = new JButton();
        button.setIcon(back);
        button.addActionListener(this);
        state = state.BACK;

        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getSuit() {
        return suit;
    }

    public void setImage(ImageIcon image) {
        button.setIcon(image);
    }

    public void flipCard() {
        if(!matched) {
            if (state == state.BACK) {
                button.setIcon(face);
                state = state.FACE;
            } else if (state == state.FACE) { //this if is redundant
                button.setIcon(back);
                state = state.BACK;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //calls handler function in primary file
        cardMatch.cardFlipped(this);
    }
}
