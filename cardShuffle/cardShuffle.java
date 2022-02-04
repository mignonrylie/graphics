import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class cardShuffle extends JFrame implements ActionListener {
    //declare menu buttons
    private static JButton shuffle;
    private static JButton reset;
    private static JButton quit;

    //initialize card image file names
    private static String[] filenames = {"cardIcons/ace_of_clubs_icon.png", "cardIcons/2_of_clubs_icon.png", "cardIcons/3_of_clubs_icon.png", "cardIcons/4_of_clubs_icon.png", "cardIcons/5_of_clubs_icon.png", "cardIcons/6_of_clubs_icon.png", "cardIcons/7_of_clubs_icon.png", "cardIcons/8_of_clubs_icon.png", "cardIcons/9_of_clubs_icon.png", "cardIcons/10_of_clubs_icon.png", "cardIcons/jack_of_clubs_icon.png", "cardIcons/queen_of_clubs_icon.png", "cardIcons/king_of_clubs_icon.png",
            "cardIcons/ace_of_diamonds_icon.png", "cardIcons/2_of_diamonds_icon.png", "cardIcons/3_of_diamonds_icon.png", "cardIcons/4_of_diamonds_icon.png", "cardIcons/5_of_diamonds_icon.png", "cardIcons/6_of_diamonds_icon.png", "cardIcons/7_of_diamonds_icon.png", "cardIcons/8_of_diamonds_icon.png", "cardIcons/9_of_diamonds_icon.png", "cardIcons/10_of_diamonds_icon.png", "cardIcons/jack_of_diamonds_icon.png", "cardIcons/queen_of_diamonds_icon.png", "cardIcons/king_of_diamonds_icon.png",
            "cardIcons/ace_of_hearts_icon.png", "cardIcons/2_of_hearts_icon.png", "cardIcons/3_of_hearts_icon.png", "cardIcons/4_of_hearts_icon.png", "cardIcons/5_of_hearts_icon.png", "cardIcons/6_of_hearts_icon.png", "cardIcons/7_of_hearts_icon.png", "cardIcons/8_of_hearts_icon.png", "cardIcons/9_of_hearts_icon.png", "cardIcons/10_of_hearts_icon.png", "cardIcons/jack_of_hearts_icon.png", "cardIcons/queen_of_hearts_icon.png", "cardIcons/king_of_hearts_icon.png",
            "cardIcons/ace_of_spades_icon.png", "cardIcons/2_of_spades_icon.png", "cardIcons/3_of_spades_icon.png", "cardIcons/4_of_spades_icon.png", "cardIcons/5_of_spades_icon.png", "cardIcons/6_of_spades_icon.png", "cardIcons/7_of_spades_icon.png", "cardIcons/8_of_spades_icon.png", "cardIcons/9_of_spades_icon.png", "cardIcons/10_of_spades_icon.png", "cardIcons/jack_of_spades_icon.png", "cardIcons/queen_of_spades_icon.png", "cardIcons/king_of_spades_icon.png"};
    //declare images
    private static ImageIcon[] pics;
    //declare buttons to hold card images
    private static JButton[] cards;

    //randomizes the images on the card buttons
    public void shuffle() {
        //clone the original images array into a new one to be set to a random order
        ImageIcon[] randomPics = pics.clone();

        int index = 0;
        for(int i=filenames.length; i>=0; i--) {
            //pick random card
            index = (int)Math.floor(Math.random()*i);

            //move the random card to the end of the array
            for(int j=index; j<filenames.length-1; j++) {
                ImageIcon temp = randomPics[j+1];
                randomPics[j+1] = randomPics[j];
                randomPics[j] = temp;
            }
        }

        //add the images to the card buttons
        for(int i=0; i<cards.length; i++) {
            cards[i].setIcon(randomPics[i]);
        }

        //redraw to display the new images
        repaint();
    }

    //resets the images to their original layout
    public void reset() {
        //uses the original set of images
        for(int i=0; i<cards.length; i++) {
            cards[i].setIcon(pics[i]);
        }
        //display the changes
        repaint();
    }

    //constructor; the majority of the operations are done here
    public cardShuffle() {
        //initialize frame and set exit program on window close
        JFrame frame = new JFrame("cards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //declare main panel to hold sub-panels
        JPanel main = new JPanel();
        //declare sub-panels, one for the menu buttons and one for each suit
        JPanel menu = new JPanel();
        JPanel clubs = new JPanel();
        JPanel diamonds = new JPanel();
        JPanel hearts = new JPanel();
        JPanel spades = new JPanel();

        //set layout of the main panel so that the sub-panels are laid out vertically
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

        //declare image and card button arrays
        pics = new ImageIcon[filenames.length];
        cards = new JButton[filenames.length];

        for(int i=0; i<filenames.length; i++) {
            //initialize images and set size
            pics[i] = new ImageIcon(filenames[i]);
            setSize(pics[i].getIconWidth()+50, pics[i].getIconHeight()+50);

            //initialize card buttons and add images
            cards[i] = new JButton(pics[i]);
            cards[i].setIcon(pics[i]);

            //add the cards to their respective panels
            //the first 13 to clubs, the next 13 to diamonds, etc.
            if(i/13 == 0) {
                clubs.add(cards[i]);
            }
            else if(i/13 == 1) {
                diamonds.add(cards[i]);
            }
            else if(i/13 == 2) {
                hearts.add(cards[i]);
            }
            else if(i/13 == 3) { //shouldnt need if
                spades.add(cards[i]);
            }
        }

        //initialize, set handlers, and add buttons to menu panel
        shuffle = new JButton("shuffle");
        shuffle.addActionListener(this);
        menu.add(shuffle);

        reset = new JButton("reset");
        reset.addActionListener(this);
        menu.add(reset);

        quit = new JButton("quit");
        quit.addActionListener(this);
        menu.add(quit);

        //adding all of the sub-panels to the main panel
        main.add(clubs);
        main.add(diamonds);
        main.add(hearts);
        main.add(spades);
        main.add(menu);

        //adding the main panel to the frame, setting its size, then displaying it
        frame.add(main);
        frame.pack();
        frame.setVisible(true);
    }

    //button event handler
    @Override
    public void actionPerformed(ActionEvent e) {
        //figure out which button was clicked and respond accordingly
        Object source = e.getSource();
        if(source == shuffle) {
            shuffle();
        }
        else if(source == reset) {
            reset();
        }
        else if(source == quit) {
            System.exit(0);
        }
        else {
            System.err.println("This shouldn't have happened.");
        }
    }

    //creates instance of class
    public static void main(String[] args) {
        cardShuffle c = new cardShuffle();
    }
}
