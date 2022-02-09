import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//TODO: make reset functional

public class cardMatch extends JFrame implements ActionListener {
    //private JFrame frame;
    //private JPanel main;
    //private JPanel menu;
    private JPanel row1;
    private JPanel row2;
    private JPanel row3;
    private JPanel row4;
    private JButton reset;
    //private int delay = 3000;
    public static boolean cardFlipped = false;
    private static int delay = 3000; //timer length in ms
    public static Timer timer;
    //private static int CARD_COUNT = 52;

    public int guessesMade = 0; //increments with each card flip
    public int matchesMade = 0; //increments with each match

    private static card cardCalled;

    private static String[] filenames = {"cardIcons/ace_of_clubs_icon.png", "cardIcons/2_of_clubs_icon.png", "cardIcons/3_of_clubs_icon.png", "cardIcons/4_of_clubs_icon.png", "cardIcons/5_of_clubs_icon.png", "cardIcons/6_of_clubs_icon.png", "cardIcons/7_of_clubs_icon.png", "cardIcons/8_of_clubs_icon.png", "cardIcons/9_of_clubs_icon.png", "cardIcons/10_of_clubs_icon.png", "cardIcons/jack_of_clubs_icon.png", "cardIcons/queen_of_clubs_icon.png", "cardIcons/king_of_clubs_icon.png",
            "cardIcons/ace_of_diamonds_icon.png", "cardIcons/2_of_diamonds_icon.png", "cardIcons/3_of_diamonds_icon.png", "cardIcons/4_of_diamonds_icon.png", "cardIcons/5_of_diamonds_icon.png", "cardIcons/6_of_diamonds_icon.png", "cardIcons/7_of_diamonds_icon.png", "cardIcons/8_of_diamonds_icon.png", "cardIcons/9_of_diamonds_icon.png", "cardIcons/10_of_diamonds_icon.png", "cardIcons/jack_of_diamonds_icon.png", "cardIcons/queen_of_diamonds_icon.png", "cardIcons/king_of_diamonds_icon.png",
            "cardIcons/ace_of_hearts_icon.png", "cardIcons/2_of_hearts_icon.png", "cardIcons/3_of_hearts_icon.png", "cardIcons/4_of_hearts_icon.png", "cardIcons/5_of_hearts_icon.png", "cardIcons/6_of_hearts_icon.png", "cardIcons/7_of_hearts_icon.png", "cardIcons/8_of_hearts_icon.png", "cardIcons/9_of_hearts_icon.png", "cardIcons/10_of_hearts_icon.png", "cardIcons/jack_of_hearts_icon.png", "cardIcons/queen_of_hearts_icon.png", "cardIcons/king_of_hearts_icon.png",
            "cardIcons/ace_of_spades_icon.png", "cardIcons/2_of_spades_icon.png", "cardIcons/3_of_spades_icon.png", "cardIcons/4_of_spades_icon.png", "cardIcons/5_of_spades_icon.png", "cardIcons/6_of_spades_icon.png", "cardIcons/7_of_spades_icon.png", "cardIcons/8_of_spades_icon.png", "cardIcons/9_of_spades_icon.png", "cardIcons/10_of_spades_icon.png", "cardIcons/jack_of_spades_icon.png", "cardIcons/queen_of_spades_icon.png", "cardIcons/king_of_spades_icon.png"};

    private card[] cards = new card[52];

    public cardMatch() {
        JFrame frame = new JFrame("card match");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

        JPanel menu = new JPanel();
        reset = new JButton("reset");
        menu.add(reset);

        row1 = new JPanel();
        row2 = new JPanel();
        row3 = new JPanel();
        row4 = new JPanel();

        timer = new Timer(delay, this);
        timer.setRepeats(false);

        //TODO: change the way cards are added
        //they should all be initialized first THEN randomized
        //this is so i can add them in order and therefore initialize their ranks (and suits even tho it doesnt matter)
        //TODO: card init function
        //TODO: card randomize
        //randomizeCards();

        main.add(row1);
        main.add(row2);
        main.add(row3);
        main.add(row4);
        main.add(menu);

        frame.add(main);

        frame.pack();
        frame.setVisible(true);
    }

    private void initCards() {
        for(int i=0; i<cards.length; i++) {
            //cards[i] = null;
            ImageIcon img = new ImageIcon(filenames[i]);
            cards[i] = new card(img);

            //Component[] components = row1.getComponents();
            cards[i].rank = (i%13)+1;
            //i=0 should result in rank=1
            //i=12 should result in rank=13
            //i=13 should result in rank=1
            /*
            if(i/13 == 0) {
                row1.add(cards[i].button);
            }
            if(i/13 == 1) {
                row2.add(cards[i].button);
            }
            if(i/13 == 2) {
                row3.add(cards[i].button);
            }
            if(i/13 == 3) { //redundant but clean
                row4.add(cards[i].button);
            }*/

            randomizeCards();
        }
    }

    private void randomizeCards() {
        //TODO: change this function to work with preexisting cards

        //cards already exist
        //shuffle the cards
        //clear the rows and re-add

        int index = 0;
        for(int i=cards.length; i>=0; i--) {
            //pick random card
            index = (int)Math.floor(Math.random()*i);

            //move the random card to the end of the array
            for(int j=index; j<cards.length-1; j++) {
                card temp = cards[j+1];
                cards[j+1] = cards[j];
                cards[j] = temp;
            }
        }

        //System.out.println(cards.length);

        clearPanel(row1);
        clearPanel(row2);
        clearPanel(row3);
        clearPanel(row4);

        for(int i=0; i<cards.length; i++) {
            //cards[i] = null;
            ImageIcon img = new ImageIcon(randomNames[i]);
            cards[i] = new card(img);

            //Component[] components = row1.getComponents();

            if(i/13 == 0) {
                row1.add(cards[i].button);
            }
            if(i/13 == 1) {
                row2.add(cards[i].button);
            }
            if(i/13 == 2) {
                row3.add(cards[i].button);
            }
            if(i/13 == 3) { //redundant but clean
                row4.add(cards[i].button);
            }
        }
        //repaint();
    }

    /*
    private void randomizeCards() {
        //TODO: change this function to work with preexisting cards

        //cards already exist
        //shuffle the cards
        //clear the rows and re-add

        String[] randomNames = filenames.clone();

        int index = 0;
        for(int i=filenames.length; i>=0; i--) {
            //pick random card
            index = (int)Math.floor(Math.random()*i);

            //move the random card to the end of the array
            for(int j=index; j<filenames.length-1; j++) {
                String temp = randomNames[j+1];
                randomNames[j+1] = randomNames[j];
                randomNames[j] = temp;
            }
        }

        //System.out.println(cards.length);

        clearPanel(row1);
        clearPanel(row2);
        clearPanel(row3);
        clearPanel(row4);

        for(int i=0; i<cards.length; i++) {
            //cards[i] = null;
            ImageIcon img = new ImageIcon(randomNames[i]);
            cards[i] = new card(img);

            //Component[] components = row1.getComponents();

            if(i/13 == 0) {
                row1.add(cards[i].button);
            }
            if(i/13 == 1) {
                row2.add(cards[i].button);
            }
            if(i/13 == 2) {
                row3.add(cards[i].button);
            }
            if(i/13 == 3) { //redundant but clean
                row4.add(cards[i].button);
            }
        }
        //repaint();
    }

     */

    private void clearPanel(JPanel panel) {
        if(panel.getComponentCount() != 1) {
            panel.removeAll(); //.getContentPane maybe
        }
    }

    //this function is called whenever a button is clicked (ie a card is flipped)
    public static void cardFlipped(card callingCard) {
        //if no cards have been clicked no restrictions
        System.out.println("you called cardMatch!");

        timer.start();
        cardCalled = callingCard;

        //if a card has been clicked, you gotta check:
        if(cardCalled != null) {
            if cardCalled.rank
        }

        //if they're the same rank, DO NOT flip them back over.
        //TODO: implement lock preventing flip
        //set cardCalled null

        //if they're not the same rank, leave them on screen for a bit then flip them back over
        //TODO: disable inputs during this time
        //set cardCalled null
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO: check whether source is timer or one of the menu buttons

        System.out.println("timer went off!");
        //System.out.println(cardFlipped);

        cardCalled.flipCard();


        //if the timer went off, call the
    }

    public static void main(String[] args) {
        cardMatch c = new cardMatch();
    }
}
