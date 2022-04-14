// Using a slider to control the alpha value for compositing

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class CompositeFrame extends JFrame {
   private int sliderMin = 0;
   private int sliderMax = 100;
   private JSlider alphaJSlider; // slider to select circle radius
   private CompositePanel myPanel; // panel to draw circle

   // no-argument constructor
   public CompositeFrame() {
      super( "Composite Demo" );
      myPanel = new CompositePanel();

      // set up JSlider to control the alpha value
      // (will be scaled to a floating point value between 0 and 1)
      // and then register JSlider event listener
      alphaJSlider = 
         new JSlider( SwingConstants.HORIZONTAL, sliderMin, sliderMax, 50 );
      alphaJSlider.setMajorTickSpacing( 10 ); 
      alphaJSlider.setPaintTicks( true ); 
      alphaJSlider.setPaintLabels(true);
      alphaJSlider.addChangeListener(
         new ChangeListener() {  
            // handle change in slider value
            public void stateChanged( ChangeEvent e ) {
               // set the alpha value to a scaled floating point value 
               // between 0.0 and 1.0
               myPanel.setAlpha ( (float)alphaJSlider.getValue() / 
                                  (float)sliderMax );
            } // end method stateChanged
         } // end anonymous inner class
      ); // end call to addChangeListener

      // Add the slider to the frame
      add( alphaJSlider, BorderLayout.SOUTH ); 
      add( myPanel, BorderLayout.CENTER ); 
   }
}
