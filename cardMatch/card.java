import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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

    private String defaultFace = "cardIcons/black_joker_icon.png";
    private String defaultBack = "cardIcons/back.png";

    //default constructor, in case no image is passed in
    public card() {
        button = new JButton();
        face = new ImageIcon(defaultFace);
        button.setIcon(face);
        button.addActionListener(this);

        state = state.FACE;
        back = new ImageIcon(defaultBack);
    }

    //overloaded constructor, meant to pass in the image
    public card(ImageIcon image) {
        button = new JButton();
        face = image;

        back = new ImageIcon(defaultBack);
        button.setIcon(back);
        button.addActionListener(this);

        state = state.BACK;
    }

    public int getRank() {
        return rank;
    }

    public int getSuit() {
        return suit;
    }

    public void setImage(ImageIcon image) {
        button.setIcon(image);
    }

    private void flipCard() {
        if(state == state.BACK) {
            button.setIcon(face);
            state = state.FACE;
        }
        else if(state == state.FACE) { //this if is redundant
            button.setIcon(back);
            state = state.BACK;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("event");
        //if state == BACK
        //set timer with callback flip card
        //cancel timer if another card is flipped
        //call function from cardMatch??? fuck what should go in what class
        //if(state == BACK) {
            flipCard();
        //}
    }
}
