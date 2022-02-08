import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class card {
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

    private int delay = 3000; //timer length in ms
    private Timer timer;

    private String defaultFace = "cardIcons/black_joker_icon.png";
    private String defaultBack = "cardIcons/back.png";

    //default constructor, in case no image is passed in
    public card() {
        ActionListener listener = new cardHandler();
        button = new JButton();
        face = new ImageIcon(defaultFace);
        button.setIcon(face);
        button.addActionListener(listener);

        state = state.FACE;
        back = new ImageIcon(defaultBack);
        timer = new Timer(delay, listener);
    }

    //overloaded constructor, meant to pass in the image
    public card(ImageIcon image) {
        ActionListener listener = new cardHandler();

        button = new JButton();
        face = image;

        back = new ImageIcon(defaultBack);
        button.setIcon(back);
        button.addActionListener(listener);

        state = state.BACK;
        timer = new Timer(delay, listener);
        timer.setRepeats(false);
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

    /*
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("card event");

        Object source = e.getSource();
        if(source == button) {
            System.out.println("button event");
            timer.start();
            cardMatch.cardFlipped = true;
        }
        else if(source == timer) {
            System.out.println("timer event");
            cardMatch.cardFlipped = false;
        }
        //if state == BACK
        //set timer with callback flip card
        //cancel timer if another card is flipped
        //call function from cardMatch??? fuck what should go in what class
        //if(state == BACK) {
            flipCard();

        //}
    }
     */
}
