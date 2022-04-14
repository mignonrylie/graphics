import java.awt.Component;
import java.awt.AlphaComposite;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

// A customized JPanel class.
import java.awt.Graphics;
import java.awt.Dimension;
import javax.swing.JPanel;

public class CompositePanel extends JPanel {
  private float alpha=0.5f;
  private BufferedImage bufferedImage1;
  private BufferedImage bufferedImage2;
  private boolean imageLoaded = false;
  private String image1URL = "Bird1.jpg";
  private String image2URL = "Bird2.jpg";

  // Constructor to read in and allocate the texture image
  // that will be used for the fill operation
  CompositePanel () {

    // The ImageObserver implementation to observe loading of the image
    ImageObserver myImageObserver = new ImageObserver() {
      public boolean imageUpdate(Image image, int flags, int x, int y, 
                                 int width, int height) {
        // If image is loaded, we don't need further notification
        // from the image observer (return "false") and we set our
        // local flag ("imageLoaded = true") so code waiting on the
        // image to load can continue
        if ((flags & ALLBITS) != 0) {
          imageLoaded = true;
          return false;
        }
        return true;
      }
    };

    // load the image
    Image sourceImage1 = Toolkit.getDefaultToolkit().getImage(image1URL);
    sourceImage1.getWidth(myImageObserver);

    Image sourceImage2 = Toolkit.getDefaultToolkit().getImage(image2URL);
    sourceImage2.getWidth(myImageObserver);

    // Wait based on the "ImageObserver", until the image is fully loaded
    while(!imageLoaded) {
      try { 
        Thread.sleep(100);
      } catch (InterruptedException e) { }
    }

    // Create the first buffered image from the loaded "Image" object
    bufferedImage1 = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
    bufferedImage1.getGraphics().drawImage(sourceImage1, 0,0,300,300,0, 0, 
               sourceImage1.getWidth(myImageObserver), 
               sourceImage1.getHeight(myImageObserver), null);

    // Create the second buffered image from the loaded "Image" object
    bufferedImage2 = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
    bufferedImage2.getGraphics().drawImage(sourceImage2, 0,0,300,300,0, 0, 
               sourceImage2.getWidth(myImageObserver), 
               sourceImage2.getHeight(myImageObserver), null);

    repaint(); // repaint panel
  }

  // Render the alpha composited images here...
  public void paintComponent( Graphics g ) {
    super.paintComponent( g );

    Graphics2D big = (Graphics2D) g;

    // Draw the start and end images
    big.drawImage(bufferedImage1, 0, 0, this);
    big.drawImage(bufferedImage2, 300, 0, this);

    // Draw in the composite area
    big.drawImage(bufferedImage1, 600, 0, this);

    // Create the compositing object, use SRC_OVER
    // Change this compositing flag to change the mode
    // Also change the alpha value (it is controlled by the slider)
    AlphaComposite ac =
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
    big.setComposite(ac);

    // Now draw over the already rendered compositing area
    // This last draw will be augmented by the alpha composite object
    big.drawImage(bufferedImage2, 600, 0, this);

  } // end method paintComponent


   // set the Alpha value from the slider
   public void setAlpha( float alpha) {
      this.alpha = alpha;
      repaint();
   }
}
