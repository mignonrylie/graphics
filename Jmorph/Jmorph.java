import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class Jmorph extends JFrame{
    final static int MIN_TWEEN = 1;
    final static int MAX_TWEEN = 60;

    private HandlePanelHandler h;


    //static JFrame frame;
    static JSpinner tweens;





    private void openMorphWindow() {
        h.resetHandles();
        JFrame frame = new JFrame("Preview");


        //TODO: fix overlap
        JButton preview = new JButton("Preview");
        preview.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        h.morphTriangles();
                    }
                }
        );
        frame.add(preview, BorderLayout.PAGE_END);
        //frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //frame.addWindowListener(new WindowAdapter() {
        //    @Override
        //    public void windowClosing(WindowEvent e) {
        //        h.resetHandles();
                //frame.setVisible(false);
        //    }
        //});
        frame.add(h);
        frame.pack();
        frame.setVisible(true);
    }



    //user controls
    //image 1
    //image 2

    //number of tweens
    //preview button (new window?)
    private void createLayout() {
        //frame = new JFrame();
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openFileStart = new JMenuItem("Open Start Image");
        JMenuItem openFileEnd = new JMenuItem("Open End Image");
        JFileChooser fc = new JFileChooser(".");

        JPanel holder = new JPanel();
        JPanel imagePanel1 = new JPanel();
        JPanel imagePanel2 = new JPanel();
        JPanel controlPanel = new JPanel();

        JButton reset = new JButton("Reset");
        JButton morph = new JButton("Preview Morph");


        //holder.setLayout(new BoxLayout(holder, BoxLayout.PAGE_AXIS));

        h = new HandlePanelHandler();

        tweens = new JSpinner(new SpinnerNumberModel(MAX_TWEEN/2, MIN_TWEEN, MAX_TWEEN, 1));
        //JSlider gridSlider = new JSlider(1, 10);

        openFileStart.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fc.showOpenDialog(Jmorph.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            BufferedImage image = null;
                            try {
                                image = ImageIO.read(file);
                            } catch (IOException e1){}

                            h.setImage(image, 1);
                        }
                    }
                }
        );

        openFileEnd.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fc.showOpenDialog(Jmorph.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            BufferedImage image = null;
                            try {
                                image = ImageIO.read(file);
                            } catch (IOException e1){};

                            h.setImage(image, 2);
                        }
                    }
                }
        );

        tweens.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        JSpinner spin = (JSpinner) e.getSource();
                        try {
                            spin.commitEdit();
                        }
                        catch (java.text.ParseException err) {}
                        int tween = (int) spin.getValue();


                        h.setTweens(tween);
                    }
                }
        );

        reset.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        h.getChild(1).resetHandles();
                        h.getChild(2).resetHandles();
                        //TODO: reset
                    }
                }
        );

        morph.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //TODO: morph
                        openMorphWindow();
                    }
                }
        );

        fileMenu.add(openFileStart);
        fileMenu.add(openFileEnd);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
        //TODO: reference this everywhere


        controlPanel.add(reset);
        controlPanel.add(tweens);
        controlPanel.add(morph);
        //frame.add(controlPanel);
        //add(controlPanel);






        imagePanel1.add(h.getChild(1));
        imagePanel2.add(h.getChild(2));


        holder.add(imagePanel1, BorderLayout.LINE_START);
        //holder.add(test, BorderLayout.CENTER);
        holder.add(imagePanel2, BorderLayout.LINE_END);

        holder.add(controlPanel, BorderLayout.PAGE_END);

        this.add(holder);
        //add(imagePanel1);
        //add(imagePanel2);
        //add(h);
        //frame.pack();
        //frame.setVisible(true);
        this.pack();
        this.setVisible(true);

        //frame.repaint();
        this.repaint();


    }

    public Jmorph() {
        super();
        createLayout();


    }

    public static void main(String[] args) {
        Jmorph morph = new Jmorph();
    }
}
