// A customized JPanel class to demonstrate
// geometric transforms applied to graphics primitives
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;
import java.awt.Graphics2D;

public class OvalPanel extends JPanel {
   // parameters to transform from ideal coordinates
   // to screen parameters:  oval description
   // The ellipse has an anchor point (upper left corner
   // of its bounding rectangle), a width and height,
   // and a rotation.
   private int ellipseX = 0;
   private int ellipseY = 0;
   private int width = 10;
   private int height = 10;

   // parameters for transformation
   private double rot=0.0;
   private double delta=0.3;

   // draw an oval of the specified width and height
   // at its current anchor point
   // and apply a rotation about a rotation point
   public void paintComponent( Graphics g ) {
      super.paintComponent( g );

      // Get the "graphics context" for drawing and 
      // applying transforms
      Graphics2D g2 = (Graphics2D) g;

      // Define the ellipse object to be rendered in 
      // screen coords
      Ellipse2D e = new Ellipse2D.Double((double) ellipseX,
          (double) ellipseY, (double) width, (double) height );

      // Define the transform that will position the ellipse 
      // in screen coords
      AffineTransform a = AffineTransform.getRotateInstance (rot, 
	(double) (ellipseX+width/2), (double) (ellipseY+width/2));
      // Update the rotation;  every time the shape is drawn, the
      // rotation increases...
      rot+=delta;

      // Establish the transform in the graphics context, 
      // then draw/fill
      g2.setTransform(a);
      // g2.draw(e); 
      g2.fill(e);
   } 

   // set the center point
   public void setPosition( int newX, int newY ) {
      ellipseX = newX; 
      ellipseY = newY;
      repaint();
   }

   public void setOvalWidth( int newWidth ) {
      width = newWidth;
      repaint();
   }

   public void setOvalHeight ( int newHeight ) {
      height = newHeight;
      repaint();
   }

   // used by layout manager to determine preferred size
   public Dimension getPreferredSize() {
      return new Dimension( 200, 200 );
   } // end method getPreferredSize

   // used by layout manager to determine minimum size
   public Dimension getMinimumSize() {
      return getPreferredSize();
   } // end method getMinimumSize
} // end class OvalPanel
