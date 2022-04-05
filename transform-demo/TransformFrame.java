// TransformFrame.java
// Using a JSlider to show how to make transformations
// on a graphics primitive
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class TransformFrame extends JFrame 
{
   private JSlider diameterJSlider;
   private JButton changeButton;
   private int changeState=0;

   private OvalPanel myPanel;

   // no-argument constructor
   public TransformFrame() 
   {
      super( "Transformation Demo" );

      myPanel = new OvalPanel();
      myPanel.setBackground( Color.YELLOW );
 
      // changes the interpretation of the slider event
      // the instance variable "changeState" rotates through
      // three values, each of which causes different
      // behavior relative to the shape being rendered in
      // the oval panel.
      changeButton = new JButton ("change");
      changeButton.addActionListener(
         new ActionListener() {  
            public void actionPerformed ( ActionEvent e ) {
               changeState = (changeState+1)%3;
            } 
         }
      );

      // set up JSlider to control a geometric value
      // tied to the shape we are displaying in the panel
      diameterJSlider = 
         new JSlider( SwingConstants.HORIZONTAL, 0, 2000, 100 );
      // create tick every 10 units
      diameterJSlider.setMajorTickSpacing( 10 ); 
      // paint ticks on slider
      diameterJSlider.setPaintTicks( true ); 

      // register JSlider event listener
      diameterJSlider.addChangeListener(
         new ChangeListener() {  
            public void stateChanged( ChangeEvent e ) {
               // height and width are the same
               if (changeState == 0) {
                 int rw = diameterJSlider.getValue();
                 int rh = rw;
                 int pw = myPanel.getWidth();
                 int ph = myPanel.getHeight();
                 myPanel.setPosition ( (pw-rw)/2, (ph-rh)/2 );
                 myPanel.setOvalWidth ( rw );
                 myPanel.setOvalHeight ( rh );
               }
               // slider now controls width of shape
               else if (changeState == 1) {
                 int rw = diameterJSlider.getValue();
                 myPanel.setOvalWidth ( rw );
               }
               else if (changeState == 2) {
                 // slider now controls height of shape
                 int rh = diameterJSlider.getValue();
                 myPanel.setOvalHeight ( rh );
               }
            } 
         }
      );

      add( changeButton, BorderLayout.NORTH );
      add( diameterJSlider, BorderLayout.SOUTH ); // add slider to frame
      add( myPanel, BorderLayout.CENTER ); // add panel to frame
   }
}
