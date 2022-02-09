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



    public void actionPerformed(ActionEvent e) {
        System.out.println("event handled");
        Object source = e.getSource();

        JButton ref = new JButton(); //why does this work but not javax.swing.JButton

        //source.getClass() will tell you what kind of object it is?
        if(source.getClass() == ref.getClass()) {
            System.out.println("button event");
        }
        System.out.println(source);
        System.out.println(source.getClass());
    }
}
