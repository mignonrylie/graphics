// Slider controlling geometric transformations on a shape
import javax.swing.JFrame;

public class TransformDemo {
   public static void main( String args[] ) { 
      TransformFrame transformFrame = new TransformFrame(); 
      transformFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      transformFrame.setSize( 1024, 768 ); // set frame size
      transformFrame.setVisible( true ); // display frame
   } // end main
} // end class TransformDemo
