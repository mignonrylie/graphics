import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class cardMatch extends JFrame implements ActionListener {
    private JPanel row1;
    private JPanel row2;
    private JPanel row3;
    private JPanel row4;
    private JButton reset;

    private static Timer timer;

    private int LONG_DELAY = 3000; //timer length in ms
    private static int SHORT_DELAY = 1500;
    private int CARD_COUNT = 52;

    private static int guessesMade = 0; //increments with each card flip
    private static int matchesMade = 0; //increments with each match
    private static JLabel guesses;
    private static JLabel matches;

    private static card click1 = null;
    private static card click2 = null;

    private static String[] filenames = {"cardIcons/ace_of_clubs_icon.png", "cardIcons/2_of_clubs_icon.png", "cardIcons/3_of_clubs_icon.png", "cardIcons/4_of_clubs_icon.png", "cardIcons/5_of_clubs_icon.png", "cardIcons/6_of_clubs_icon.png", "cardIcons/7_of_clubs_icon.png", "cardIcons/8_of_clubs_icon.png", "cardIcons/9_of_clubs_icon.png", "cardIcons/10_of_clubs_icon.png", "cardIcons/jack_of_clubs_icon.png", "cardIcons/queen_of_clubs_icon.png", "cardIcons/king_of_clubs_icon.png",
            "cardIcons/ace_of_diamonds_icon.png", "cardIcons/2_of_diamonds_icon.png", "cardIcons/3_of_diamonds_icon.png", "cardIcons/4_of_diamonds_icon.png", "cardIcons/5_of_diamonds_icon.png", "cardIcons/6_of_diamonds_icon.png", "cardIcons/7_of_diamonds_icon.png", "cardIcons/8_of_diamonds_icon.png", "cardIcons/9_of_diamonds_icon.png", "cardIcons/10_of_diamonds_icon.png", "cardIcons/jack_of_diamonds_icon.png", "cardIcons/queen_of_diamonds_icon.png", "cardIcons/king_of_diamonds_icon.png",
            "cardIcons/ace_of_hearts_icon.png", "cardIcons/2_of_hearts_icon.png", "cardIcons/3_of_hearts_icon.png", "cardIcons/4_of_hearts_icon.png", "cardIcons/5_of_hearts_icon.png", "cardIcons/6_of_hearts_icon.png", "cardIcons/7_of_hearts_icon.png", "cardIcons/8_of_hearts_icon.png", "cardIcons/9_of_hearts_icon.png", "cardIcons/10_of_hearts_icon.png", "cardIcons/jack_of_hearts_icon.png", "cardIcons/queen_of_hearts_icon.png", "cardIcons/king_of_hearts_icon.png",
            "cardIcons/ace_of_spades_2_icon.png", "cardIcons/2_of_spades_icon.png", "cardIcons/3_of_spades_icon.png", "cardIcons/4_of_spades_icon.png", "cardIcons/5_of_spades_icon.png", "cardIcons/6_of_spades_icon.png", "cardIcons/7_of_spades_icon.png", "cardIcons/8_of_spades_icon.png", "cardIcons/9_of_spades_icon.png", "cardIcons/10_of_spades_icon.png", "cardIcons/jack_of_spades_icon.png", "cardIcons/queen_of_spades_icon.png", "cardIcons/king_of_spades_icon.png"};

    private card[] orderedCards = new card[CARD_COUNT];
    private card[] cards = new card[CARD_COUNT];

    public cardMatch() {
        JFrame frame = new JFrame("card match");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

        JPanel menu = new JPanel();
        reset = new JButton("reset");
        reset.addActionListener(this);

        guesses = new JLabel(String.valueOf(guessesMade)+" guesses");
        matches = new JLabel(String.valueOf(matchesMade)+" matches");

        menu.add(guesses);
        menu.add(reset);
        menu.add(matches);

        row1 = new JPanel();
        row2 = new JPanel();
        row3 = new JPanel();
        row4 = new JPanel();

        timer = new Timer(LONG_DELAY, this);
        timer.setRepeats(false);

        initCards();
        randomizeCards();
        addToPanel();

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
        for(int i = 0; i < cards.length; i++) {
            ImageIcon img = new ImageIcon(filenames[i]);
            //TODO: add rank in constructor call
            orderedCards[i] = new card(img);
            orderedCards[i].setRank((i % 13) + 1);
        }
    }

    private void randomizeCards() {
        cards = orderedCards.clone();

        int index = 0;
        for (int i = cards.length; i >= 0; i--) {
            //pick random card
            index = (int) Math.floor(Math.random() * i);

            //move the random card to the end of the array
            for (int j = index; j < cards.length - 1; j++) {
                card temp = cards[j + 1];
                cards[j + 1] = cards[j];
                cards[j] = temp;
            }
        }
    }

    private void addToPanel() {
        clearPanel(row1);
        clearPanel(row2);
        clearPanel(row3);
        clearPanel(row4);

        for (int i = 0; i < cards.length; i++) {
            if (i / 13 == 0) {
                row1.add(cards[i].button);
            }
            if (i / 13 == 1) {
                row2.add(cards[i].button);
            }
            if (i / 13 == 2) {
                row3.add(cards[i].button);
            }
            if (i / 13 == 3) {
                row4.add(cards[i].button);
            }
        }
    }

    private void clearPanel(JPanel panel) {
        if(panel.getComponentCount() != 1) {
            panel.removeAll();
        }
        panel.revalidate();
    }

    private void resetBoard() {
        if(click1 != null) {
            click1.flipCard();
            click1 = null;
        }
        if(click2 != null) {
            click2.flipCard();
            click2 = null;
        }

        matchesMade = 0;
        guessesMade = 0;
        matches.setText(String.valueOf(matchesMade)+" matches");
        guesses.setText(String.valueOf(guessesMade)+" guesses");

        initCards();
        randomizeCards();

        clearPanel(row1);
        clearPanel(row2);
        clearPanel(row3);
        clearPanel(row4);

        addToPanel();
    }

    //this function is called whenever a button is clicked (ie a card is flipped)
    public static void cardFlipped(card callingCard) {
        //only process cards that haven't already been matched
        if(!callingCard.matched) {
            //no cards have been clicked yet
            if (click1 == null) {
                click1 = callingCard;
                click1.flipCard();
                guessesMade++;
                if(guessesMade == 1) {
                    guesses.setText(String.valueOf(guessesMade) + " guess");
                }
                else {
                    guesses.setText(String.valueOf(guessesMade) + " guesses");
                }
            }
            //one card has already been clicked
            else if (click2 == null) {
                timer.stop();
                click2 = callingCard;
                click2.flipCard();
                //TODO: (guessesmade++)?
                guessesMade++;
                if(guessesMade == 1) {
                    guesses.setText(String.valueOf(guessesMade) + " guess");
                }
                else {
                    guesses.setText(String.valueOf(guessesMade) + " guesses");
                }

                //check if the cards match
                if (click1.getRank() == click2.getRank()) { //they match
                    matchesMade++;
                    if(matchesMade == 1) {
                        matches.setText(String.valueOf(matchesMade) + " match");
                    }
                    else {
                        matches.setText(String.valueOf(matchesMade) + " matches");
                    }

                    click1.matched = true;
                    click2.matched = true;
                    click1 = null;
                    click2 = null;
                } else { //they don't match
                    //flip the cards back over after a short time
                    timer.setInitialDelay(SHORT_DELAY);
                    timer.start();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == reset) {
            resetBoard();
        }
        else if(e.getSource() == timer){
            timer.setInitialDelay(LONG_DELAY);

            if(click1 != null) {//possibly redundant
                click1.flipCard();
                click1 = null;
            }
            if(click2 != null) {
                click2.flipCard();
                click2 = null;
            }
        }
    }

    public static void main(String[] args) {
        cardMatch c = new cardMatch();
    }
}
