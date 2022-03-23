import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class ImageRotate {
    //real-time rotation of image via slider bar
    //middle of slider is 0 rotation
    //all the way to the left is 360 counterclockwise
    //all the way to the right is 360 clockwise
    //rotate around center point
    //display in real time
    //reset button
    //quit button
    //read file
    static int angle = 0;
    static BufferedImage image = new BufferedImage(1, 1, 1);
    static JFrame frame = new JFrame("Image Rotate");
    JLabel holdImage = new JLabel();


    private void setImage(BufferedImage img, JFrame frame, JLabel imageHolder) {
        //frame.remove(JLabel);
        //JLabel imageHolder = new JLabel(new ImageIcon(img));


        image = img;

        //imageHolder.setIcon(new ImageIcon(image));
        //imageHolder.setIcon(new ImageIcon(image));

        //TODO: revalidate not needed?
        frame.revalidate();
        frame.repaint();
    }



    public ImageRotate() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);
        JMenu fileMenu = new JMenu("File");
        JMenuItem fileOpen = new JMenuItem("Open file");

        JFileChooser fileChooser = new JFileChooser(); // "."


        //frame.add(menu);
        JLabel holdImage = new JLabel();

        fileOpen.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fileChooser.showOpenDialog(frame);
                        if(returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();
                            try {
                                //TODO: remove this possibly unused variable
                                BufferedImage imagea = ImageIO.read(file);
                                //TODO: change this variable name it's dumb
                                //holdImage = new JLabel(new ImageIcon(image));
                                //holdImage.setIcon(new ImageIcon(imagea));
                                //frame.revalidate();
                                //frame.repaint();
                                System.out.println("image read");
                                //image = ImageIO.read(file);
                                setImage(imagea, frame, holdImage);
                                //TODO: remove println
                            }
                            catch (Exception ex) {
                                System.out.println("File open error!");
                            }
                        }
                    }
                }
        );

        fileMenu.add(fileOpen);
        menu.add(fileMenu);


        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

        JPanel menuPanel = new JPanel();
        JPanel imagePanel = new JPanel();

        JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Reset");
                //TODO: reset image
            }
        });

        JSlider angleSlider = new JSlider(-360, 360);
        int angle = 0;
        angleSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider slide = (JSlider) e.getSource();
                setAngle(slide.getValue());
            }
        });

        JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               System.exit(0);
           }
        });

        //final JFileChooser fc = new JFileChooser(".");
        BufferedImage image = new BufferedImage(1, 1, 1);



        try {
            image = ImageIO.read(new File("pog.png")); //new BufferedImage();
        }
        catch(Exception e) {
            System.exit(0);
        }

        setImage(image, frame, holdImage);

        //holdImage = setImage(image);


        menuPanel.add(reset);
        menuPanel.add(angleSlider);
        menuPanel.add(quit);


        imagePanel.add(holdImage);


        //frame.add(menu);
        main.add(menuPanel);
        //frame.add(imagePanel);
        //main.add(imagePanel);


        frame.add(main);
        //frame.add(image);
        frame.pack();
        frame.setVisible(true);

    }


    private static void setAngle(int a) {
        System.out.println(a);
        angle = a; //not needed?
        //TODO: rotate image

        float radians = (float) ((double) a / (double) 180.0 * (double) (Math.PI));
        float x = (float) image.getWidth() / (float) 2.0;
        float y = (float) image.getHeight() / (float) 2.0;

        AffineTransform transform = new
                AffineTransform(Math.cos(radians), Math.sin(radians),
                -Math.sin(radians), Math.cos(radians),
                x - x * Math.cos(radians) + y * Math.sin(radians),
                y - x * Math.sin(radians) - y * Math.cos(radians));

        Graphics2D g2 = image.createGraphics();
        g2.fillRect(0, 0, (int) x * 2, (int) y * 2);
        g2.setTransform(transform);
        g2.setColor(new Color(0, 0, 0));
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        frame.repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D painted = (Graphics2D) g;
        //super.paintComponent(g);
        //g.drawImage(image, 0, 0, frame);
        painted.drawImage(image, 0, 0, frame);
    }

    public static void main(String[] args) {
        ImageRotate img = new ImageRotate();
    }

}
