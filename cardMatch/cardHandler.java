import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class cardHandler extends JFrame implements ActionListener {
    /* what should happen whenever there is an event? a button or a timer.
    * when a button is clicked (a card)
    *   flip the card
    *   increment the flip counter
    *   if the cards match, they should stay flipped
    *   if there's a timer waiting, stop it
    *       if the card is a match with an existing card, leave them flipped
    *       if the card is not a match, set a timer to let them display for a moment before flipping them back over
    *
    * when the timer runs out
    *   flip the card back over
    *
    * */

    /*

    public void actionPerformed(ActionEvent e) {
        System.out.println("event handled");
        Object source = e.getSource();

        //i HATE this
        //why does this work but not javax.swing.JButton
        JButton ref = new JButton();

        System.out.println(source);
        //System.out.println(source.getClientProperty());
        if(source.getClass() == ref.getClass()) {
            System.out.println("button event");
            JButton button = (JButton) source;

            //I think this just returns the panel that the button is attached to, not the object it's a member of
            System.out.println(button.getParent());

            System.out.println(button.getClientProperty());

            //goal is to get source that triggered this action listener
            //that source being a JButton
            //but I need the class that the JButton is a member of, my card class
            //so then i can call functions pertaining to the card object that contains the JButton
            //i'll need to do something similar for cardMatch class

            //button.flipCard();
        }


    }

     */
}
