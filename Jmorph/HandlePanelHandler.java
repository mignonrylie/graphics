import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;


import Jama.*;

//TODO: project requirements
//TODO: use ffmpeg to export mp4 of morph



//extra credit:
//disallow overlapping triangles
//move control points as a group
//project save/load



//TODO: turn reused functions (between classes especially) into static functions
//TODO: set scope of functions/variables
//TODO: get rid of unused variables
//TODO: rename functions for clarity
//TODO: find a way to combine triangle functions
//TODO: make this run faster wtf. optimize to only update triangles that were moved?
//TODO: check for calling child class when not necessary


public class HandlePanelHandler extends JPanel{
    //debugging
    boolean doFade = false;
    boolean doWarp = true;
    boolean doBrightness = true;

    boolean outputImages = true;

    int gridSize;

    BufferedImage startImg;
    BufferedImage endImg;
    BufferedImage morphImg;
    BufferedImage morphImg2;

    int[] border;
    double alpha = 0;

    static boolean stop = true;

    final int HANDLE_SIZE = 10;
    final int FULL_PANEL_SIZE = 550;

    HandlePanel h1;
    HandlePanel h2;

    int tweens = 30;

    boolean init = false;

    int perimeterLength;

    Rectangle[] morphHandles;
    Rectangle[] morphHandles2;
    Triangle[] startTriangles;
    Triangle[] triangles;
    Triangle[] triangles2;
    Triangle[] destTriangles;
    int numTriangles;


    int brightness = 0;



    public void setBrightness(int bright) {
        //TODO: set brightness separately?
        h1.setBrightness(bright);
        h2.setBrightness(bright);
    }




    public void setGridSize(int size) {
        h1.setGridSize(size);
        h2.setGridSize(size);
        gridSize = size;

        gridSizeDependencies();
    }


    private void gridSizeDependencies() {
        //TODO: replace with loadHandles?
        //uses child specs to save local variables
        HandlePanel h1Copy = new HandlePanel(h1);
        HandlePanel h2Copy = new HandlePanel(h2);

        morphHandles = new Rectangle[gridSize*gridSize];
        morphHandles = h1Copy.getHandles();
        morphHandles2 = new Rectangle[gridSize*gridSize];
        morphHandles2 = h2Copy.getHandles();

        border = h1Copy.getBorderList();
        perimeterLength = h1.perimeterLength;
    }





    public HandlePanelHandler() {
        //create child panels
        h1 = new HandlePanel();
        h2 = new HandlePanel();

        //adds the mouselisteners to the children
        addListeners(h1);
        addListeners(h2);

        setGridSize(h1.getGridSize());

        try {
            //save beginning and end images to parent class
            startImg = ImageIO.read(new File("cat1.jpeg"));
            endImg = ImageIO.read(new File("cat2.jpeg"));

            morphImg2 = copyImage(endImg);
            warpImage(endImg, morphImg2, h2.getHandles(), h1.getHandles(), morphHandles2, 1);

            //set images within children
            BufferedImage tempStart = ImageIO.read(new File("cat1.jpeg"));
            BufferedImage tempEnd = ImageIO.read(new File("cat2.jpeg"));
            setImage(tempStart, 1);
            setImage(tempEnd, 2);
        }
        catch(IOException e) {
            System.err.println("Image read exception");
        }






        //resetHandles();
    }

    public void setImage(BufferedImage img, int which) {
        if(img.getHeight() != 500 || img.getWidth() != 500) {
            //TODO: ? make image size a variable
            //TODO: ? crop to center of image
            img = img.getSubimage(0, 0, 500, 500);
        }


        //TODO: crop/scale
        if(which == 1) {
            morphImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
            morphImg = img;

            h1.addImage(img);
            h1.repaint();
        }
        else if(which == 2) {
            morphImg2 = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
            morphImg2 = img;

            warpImage(endImg, morphImg2, h2.getHandles(), h1.getHandles(), morphHandles2, 1);


            h2.addImage(img);
            h2.repaint();
        }
        else {
            System.out.println("Error: this child doesn't exist");
        }
    }

    public void setTweens(int t) {
        tweens = t;
    }

    //TODO: see if you can combine button or whatever
    private void addListeners(HandlePanel h) {
        h.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                h.clickHandle(e.getX(), e.getY());
            }
            public void mouseReleased(MouseEvent e) {
                h.mouseDone();
            }
        });

        h.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                h.moveHandle(e.getX(), e.getY());
            }
        });
    }

    public HandlePanel getChild(int which) {
        if(which == 1)
            return h1;
        else if(which == 2)
            return h2;
        System.out.println("Error: this child doesn't exist");
        return null;
    }

    protected void selectHandle(int i) {
        h1.colorHandle(i);
        h2.colorHandle(i);
    }

    //this code was taken almost verbatim from the given code, MorphTools.java
    private void alternateMatrix(double[] Sx, double[] Sy, double[] Dx, double[] Dy,
                                BufferedImage src, BufferedImage dest) {
        double [][] Aarray = new double [3][3];
        double [][] BdestX = new double [3][1];
        double [][] BdestY = new double [3][1];

        for( int i= 0; i<3; ++i){
            Aarray[i][0] = Sx[i];
            Aarray[i][1] = Sy[i];
            Aarray[i][2] = 1.0;
            BdestX[i][0] = Dx[i];
            BdestY[i][0] = Dy[i];
        }

        Matrix A = new Matrix(Aarray);
        Matrix bx = new Matrix(BdestX);
        Matrix by = new Matrix(BdestY);

        Matrix affineRow1 = A.solve(bx);
        Matrix affineRow2 = A.solve(by);

        AffineTransform af = new
                AffineTransform(affineRow1.get(0,0), affineRow2.get(0,0),
                affineRow1.get(1,0), affineRow2.get(1,0),
                affineRow1.get(2,0), affineRow2.get(2,0));

        Graphics2D g2 = dest.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//ALIASING);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);//INTERPOLATION);

        GeneralPath destPath = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
        destPath.moveTo((float)Dx[0], (float)Dy[0]);
        destPath.lineTo((float)Dx[1], (float)Dy[1]);
        destPath.lineTo((float)Dx[2], (float)Dy[2]);
        destPath.lineTo((float)Dx[0], (float)Dy[0]);

        g2.clip(destPath);
        // Apply the affine transform, which will map the pixels in
        // the source triangle onto the destination image
        // the destination
        g2.setTransform(af);
        // Map the pixels from the source image into the destination
        // according to the destination image's graphics context
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
    }



    //this creates triangles for calculations
    //TODO: the first parameter is maybe not necessary
    private Triangle[] generateCustomTriangles(Triangle[] triangles,
                                         Rectangle[] newHandles) {
        triangles = new Triangle[2*perimeterLength*perimeterLength];
        numTriangles = 0;

        for(int i = 0; i < perimeterLength*perimeterLength; i++) {
            int col = i % perimeterLength;
            int row = i / perimeterLength;
            if((col != perimeterLength-1) && (row != perimeterLength-1)) {
                //upper
                triangles[numTriangles] = new Triangle(
                        newHandles[i].getX(),
                        newHandles[i].getY(),
                        newHandles[i+1].getX(),
                        newHandles[i+1].getY(),
                        newHandles[i+perimeterLength].getX(),
                        newHandles[i+perimeterLength].getY()
                );
                numTriangles++;


                //lower
                triangles[numTriangles] = new Triangle(
                        newHandles[i+1].getX(),
                        newHandles[i+1].getY(),
                        newHandles[i+perimeterLength].getX(),
                        newHandles[i+perimeterLength].getY(),
                        newHandles[i+perimeterLength+1].getX(),
                        newHandles[i+perimeterLength+1].getY()
                );
                numTriangles++;
            }
        }
        return triangles;
    }


    private void warpImage(BufferedImage src, BufferedImage dest, Rectangle[] srcRect,
                           Rectangle[] destRect, Rectangle[] handles, int t) {
        //for the second image:
        //take the second image's morphed point and put it where the first image's morphed point is




        //to hold the calculated in-between
        int[] tempX = new int[srcRect.length];
        int[] tempY = new int[srcRect.length];

        for (int i = 0; i < tempX.length; i++) {
            if(srcRect[i].getX() == destRect[i].getX() &&
                    srcRect[i].getY() == destRect[i].getY()) continue;
            System.out.println("comparing");

            double startX = srcRect[i].getX();
            double startY = srcRect[i].getY();
            double endX = destRect[i].getX();
            double endY = destRect[i].getY();

            //parametric equation to calculate in-between
            tempX[i] = (int)(startX + t * ((endX - startX) / tweens));
            tempY[i] = (int)(startY + t * ((endY - startY) / tweens));

            //updating the in-between handles
            handles[i].setRect(tempX[i], tempY[i], HANDLE_SIZE, HANDLE_SIZE);

            //update the in-between triangles
            Triangle[] srcTriangles = new Triangle[numTriangles];
            srcTriangles = generateCustomTriangles(srcTriangles, srcRect);

            //TODO: rename triangles to morphTriangles1 or something
            Triangle[] morphingTriangles = new Triangle[numTriangles];
            morphingTriangles = generateCustomTriangles(morphingTriangles, handles);

            for(int j = 0; j < numTriangles; j++) {
                //TODO: redundant?
                if(sameSpot(srcTriangles[j], morphingTriangles[j])) continue;


                    morphOneTriangle(srcTriangles[j], morphingTriangles[j], src, dest);
                    //reference image, then the one that you want to change
                    //trianglesUpdated++;



                //TODO: morph second image?

            }

        }
        //System.out.println(trianglesUpdated);

    }

    /*
    public void iterate(int t) {
        //for the second image
        //take the second image's morphed point and put it where the first image's morphed point is




        //TODO: alpha out of range
        alpha += 1/(double)tweens;

        int trianglesUpdated = 0;
        //if(alpha/)

        //load beginning and end points
        Rectangle[] startHandles = h1.getHandles();
        Rectangle[] endHandles = h2.getHandles();

        //to hold the calculated in-between
        int[] tempX = new int[startHandles.length];
        int[] tempY = new int[startHandles.length];
        int[] tempX2 = new int[startHandles.length];
        int[] tempY2 = new int[startHandles.length];

        for (int i = 0; i < tempX.length; i++) {
            if(startHandles[i].getX() == endHandles[i].getX() &&
            startHandles[i].getY() == endHandles[i].getY()) continue;
            System.out.println("comparing");

            double startX =  startHandles[i].getX();
            double startY =  startHandles[i].getY();
            double endX = endHandles[i].getX();
            double endY = endHandles[i].getY();

            //parametric equation to calculate in-between
            tempX[i] = (int)(startX + t * ((endX - startX) / tweens));
            tempY[i] = (int)(startY + t * ((endY - startY) / tweens));

            tempX2[i] = (int)(endX + t * ((startX - endX) / tweens));
            tempY2[i] = (int)(endY + t * ((startY - endY) / tweens));

            //updating the in-between handles
            morphHandles[i].setRect(tempX[i], tempY[i], HANDLE_SIZE, HANDLE_SIZE);
            morphHandles2[i].setRect(tempX2[i], tempY2[i], HANDLE_SIZE, HANDLE_SIZE);

            //update the in-between triangles
            startTriangles = generateCustomTriangles(startTriangles, startHandles);
            //TODO: rename triangles to morphTriangles1 or something
            triangles = generateCustomTriangles(triangles, morphHandles);
            //generateAllTriangles();
            trianglesUpdated = 0;

            for(int j = 0; j < numTriangles; j++) {
                //TODO: redundant?
                if(sameSpot(startTriangles[j], triangles[j])) continue;

                if(init) {
                    morphOneTriangle(triangles[j], startTriangles[j], endImg, morphImg2);
                }
                else {
                    morphOneTriangle(startTriangles[j], triangles[j], startImg, morphImg);
                    trianglesUpdated++;
                }


                //TODO: morph second image?

            }

        }
        System.out.println(trianglesUpdated);
    }

     */

    private boolean sameSpot(Triangle one, Triangle two) {
        //TODO: allow int interpretation?
        for(int i = 0; i < one.getXsDouble().length; i++) {
            if(one.getXsDouble()[i] != two.getXsDouble()[i] ||
            one.getYsDouble()[i] != one.getYsDouble()[i]) return false;
        }
        return true;
    }

    private double[] intToDouble(int[] arr) {
        double[] returnArr = new double[arr.length];
        for(int i = 0; i < arr.length; i++) {
            returnArr[i] = arr[i];
        }
        return returnArr;
    }




    public void morphOneTriangle(Triangle triangle1, Triangle triangle2,
                                 BufferedImage startImg, BufferedImage endImg) {
        //this code was taken from MorphTools.java
        double[] Sx = triangle1.getXsDouble();
        double[] Sy = triangle1.getYsDouble();
        double[] Dx = triangle2.getXsDouble();
        double[] Dy = triangle2.getYsDouble();

        //TODO: change this and give credit
        //TODO: specify images maybe? that could be useful
        alternateMatrix(Sx, Sy, Dx, Dy, startImg, endImg);
    }

    boolean warping = false;
    int count = 0;

    public void doWarp() {
        System.out.println("warping");
        alpha = 0;


        warpImage(endImg, morphImg2, h1.getHandles(), h2.getHandles(), morphHandles2, 1);


        //need to take the original second image and apply the warp according to the first.
        //then go from that back to neutral?


        //initialize the in-between image as a copy of the initial image
        //morphImg = new BufferedImage(startImg.getWidth(), startImg.getHeight(), startImg.getType());
        //morphImg = copyImage(startImg);

        //needs to be initialized as second image with warp applied
        //morphImg2 = new BufferedImage(endImg.getWidth(), endImg.getHeight(), endImg.getType());
        //morphImg2 = copyImage(endImg);

        //setting up the timer between frames
        stop = false;

        count = 0;
        Timer timer = new Timer(1, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //continue updating until all frames have been drawn
                if(count < tweens) {
                    count++;
                    //iterate(count);

                    warpImage(startImg, morphImg, h1.getHandles(), h2.getHandles(), morphHandles, count);
                    //TODO: do morph for second image from h1 to h2
                    //warpImage(endImg, morphImg2, h1.getHandles(), h2.getHandles(), morphHandles2, tweens - count);
                    repaint();
                    System.out.println(count);

                    alpha += (double)1/tweens;

                    //TODO: debugging second image, remove later
                    //alpha = 1;


                    warping = true;

                } else {
                    ((Timer)e.getSource()).stop();
                    warping = false;
                }
            }
        });
        timer.setRepeats(true);

        //TODO: change duration?
        timer.setDelay(10);
        timer.start();
    }

    //thank you stackOverflow
    private static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public void resetHandles() {
        HandlePanel h1Copy = new HandlePanel(h1);
        HandlePanel h2Copy = new HandlePanel(h2);
        //TODO: needs to be variable. add variable for length?
        morphHandles = new Rectangle[gridSize*gridSize]; //TODO: perimeterlength?
        morphHandles = h1Copy.getHandles();
        morphHandles2 = new Rectangle[gridSize*gridSize];
        morphHandles2 = h2Copy.getHandles();


        startImg = new BufferedImage(h1.getBim().getWidth(), h1.getBim().getHeight(), h1.getBim().getType());
        startImg = copyImage(h1.getBim());

        endImg = new BufferedImage(h2.getBim().getWidth(), h2.getBim().getHeight(), h2.getBim().getType());
        endImg = copyImage(h2.getBim());

        morphImg = new BufferedImage(startImg.getWidth(), startImg.getHeight(), startImg.getType());
        morphImg = copyImage(startImg);

        morphImg2 = new BufferedImage(endImg.getWidth(), endImg.getHeight(), endImg.getType());
        morphImg2 = copyImage(endImg);

        warpImage(endImg, morphImg2, h2.getHandles(), h1.getHandles(), morphHandles2, 1);

        //TODO: there is a better way to do this. separate functions?
        //iterate(tweens);
    }

    public Dimension getPreferredSize() {
        return new Dimension(FULL_PANEL_SIZE, FULL_PANEL_SIZE);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    BufferedImage finalImage;
    private BufferedImage generateImage() {
        finalImage = new BufferedImage(morphImg.getWidth(), morphImg.getHeight(), morphImg.getType());
        Graphics g = finalImage.getGraphics();

        Graphics2D g2D = (Graphics2D) g;


        //g2D.drawImage(startImg, 0, 0, this); //draw start image

        g2D.drawImage(morphImg, 0, 0, this);


        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)alpha);
        g2D.setComposite(ac);
        g2D.drawImage(morphImg2, 0, 0, this); //draw end image






        if(outputImages) {
            try {
                String whichFrame = "";
                if(count > 10) {
                    whichFrame = "0" + Integer.toString(count);
                }
                else {
                    whichFrame = Integer.toString(count);
                }

                File output = new File("frames/frame" + whichFrame + ".jpeg");
                ImageIO.write(finalImage, "jpeg", output);
            }
            catch(IOException e) {
                System.err.println("IOException: image write fail");
            }
        }

        return null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        generateImage();

        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(finalImage, 0, 0, this);

        //image one should go from original to morph
        //image two should go from morph to original if i'm feeling ambitious
        //alpha composite between first morph and second morph
    }


    //this creates triangles for calculations
    /*
    private void generateAllTriangles() {
        startTriangles = new Triangle[2*perimeterLength*perimeterLength];
        triangles = new Triangle[2*perimeterLength*perimeterLength];
        triangles2 = new Triangle[2*perimeterLength*perimeterLength];
        destTriangles = new Triangle[2*perimeterLength*perimeterLength];
        Rectangle[] startHandles = h1.getHandles();
        Rectangle[] endHandles = h2.getHandles();
        numTriangles = 0;

        for(int i = 0; i < perimeterLength*perimeterLength; i++) {
            int col = i % perimeterLength;
            int row = i / perimeterLength;
            if((col != perimeterLength-1) && (row != perimeterLength-1)) {
                //upper
                startTriangles[numTriangles] = new Triangle(
                        startHandles[i].getX(),
                        startHandles[i].getY(),
                        startHandles[i+1].getX(),
                        startHandles[i+1].getY(),
                        startHandles[i+perimeterLength].getX(),
                        startHandles[i+perimeterLength].getY()
                );
                triangles[numTriangles] = new Triangle(
                        morphHandles[i].getX(),
                        morphHandles[i].getY(),
                        morphHandles[i+1].getX(),
                        morphHandles[i+1].getY(),
                        morphHandles[i+perimeterLength].getX(),
                        morphHandles[i+perimeterLength].getY()
                );
                triangles2[numTriangles] = new Triangle(
                        morphHandles2[i].getX(),
                        morphHandles2[i].getY(),
                        morphHandles2[i+1].getX(),
                        morphHandles2[i+1].getY(),
                        morphHandles2[i+perimeterLength].getX(),
                        morphHandles2[i+perimeterLength].getY()
                );
                destTriangles[numTriangles] = new Triangle(
                        endHandles[i].getX(),
                        endHandles[i].getY(),
                        endHandles[i+1].getX(),
                        endHandles[i+1].getY(),
                        endHandles[i+perimeterLength].getX(),
                        endHandles[i+perimeterLength].getY()
                );


                numTriangles++;


                //lower
                startTriangles[numTriangles] = new Triangle(
                        startHandles[i+1].getX(),
                        startHandles[i+1].getY(),
                        startHandles[i+perimeterLength].getX(),
                        startHandles[i+perimeterLength].getY(),
                        startHandles[i+perimeterLength+1].getX(),
                        startHandles[i+perimeterLength+1].getY()
                );
                triangles[numTriangles] = new Triangle(
                        morphHandles[i+1].getX(),
                        morphHandles[i+1].getY(),
                        morphHandles[i+perimeterLength].getX(),
                        morphHandles[i+perimeterLength].getY(),
                        morphHandles[i+perimeterLength+1].getX(),
                        morphHandles[i+perimeterLength+1].getY()
                );
                triangles2[numTriangles] = new Triangle(
                        morphHandles2[i+1].getX(),
                        morphHandles2[i+1].getY(),
                        morphHandles2[i+perimeterLength].getX(),
                        morphHandles2[i+perimeterLength].getY(),
                        morphHandles2[i+perimeterLength+1].getX(),
                        morphHandles2[i+perimeterLength+1].getY()
                );
                destTriangles[numTriangles] = new Triangle(
                        endHandles[i+1].getX(),
                        endHandles[i+1].getY(),
                        endHandles[i+perimeterLength].getX(),
                        endHandles[i+perimeterLength].getY(),
                        endHandles[i+perimeterLength+1].getX(),
                        endHandles[i+perimeterLength+1].getY()
                );

                numTriangles++;
            }
        }
    }

     */

    //TODO: this is really stupid
    //TODO: change this method to just get every triangle for use in transformation
    private void drawMyOwnTriangles(Graphics g) {
        triangles = new Triangle[2*perimeterLength*perimeterLength];
        Triangle[] triToReturn = new Triangle[morphHandles.length];
        //num of triangles = 2*perimeterLength*perimeterLength

        //TODO: account for edge triangles?
        for(int i = 0; i < morphHandles.length; i++) {
            int row = i / perimeterLength;
            int col = i % perimeterLength;
            //draw triangles within grid
            if(row != perimeterLength-1 && col != perimeterLength-1) {
                    drawOneTriangle(g, i, i+1, i+perimeterLength);
            }
            else {
                if(row == perimeterLength-1 && col != perimeterLength-1) {
                    //draw lines across
                    int x1 = (int)morphHandles[i].getX() + HANDLE_SIZE/2;
                    int y1 = (int)morphHandles[i].getY() + HANDLE_SIZE/2;
                    int x2 = (int)morphHandles[i+1].getX() + HANDLE_SIZE/2;
                    int y2 = (int)morphHandles[i+1].getY() + HANDLE_SIZE/2;
                    g.drawLine(x1, y1, x2, y2);


                }
                if(col == perimeterLength-1 && row != perimeterLength-1) {
                    //draw lines down
                    int x1 = (int)morphHandles[i].getX() + HANDLE_SIZE/2;
                    int y1 = (int)morphHandles[i].getY() + HANDLE_SIZE/2;
                    int x2 = (int)morphHandles[i+perimeterLength].getX() + HANDLE_SIZE/2;
                    int y2 = (int)morphHandles[i+perimeterLength].getY() + HANDLE_SIZE/2;
                    g.drawLine(x1, y1, x2, y2);
                }
            }
        }
    }

    //TODO: this is bad practice
    private void drawOneTriangle(Graphics g, int point1, int point2, int point3) {
        Triangle tri = createTriangle(point1, point2, point3);
        tri.draw(g);
    }

    //this creates a triangle for the purposes of drawing
    //TODO: is morphHandles2 needed here? I don't think so
    private Triangle createTriangle(int point1, int point2, int point3) {
        int x1 = (int)morphHandles[point1].getX() + HANDLE_SIZE/2;
        int y1 = (int)morphHandles[point1].getY() + HANDLE_SIZE/2;

        int x2 = (int)morphHandles[point2].getX() + HANDLE_SIZE/2;
        int y2 = (int)morphHandles[point2].getY() + HANDLE_SIZE/2;

        int x3 = (int)morphHandles[point3].getX() + HANDLE_SIZE/2;
        int y3 = (int)morphHandles[point3].getY() + HANDLE_SIZE/2;

        return new Triangle(x1, y1, x2, y2, x3, y3);
    }








    //TODO: list of functions/variables which are shared between classes
    //BufferedImage image to display
    //so much stuff with triangles
    //Rectangle[] handles
    //int[] border



























    //I would rather this be its own class, but I can't figure out how to do this without extending JPanel
    //TODO: maybe this can become its own class?
    class HandlePanel extends JPanel implements Cloneable{
        final int HANDLE_SIZE = 10;
        final int FULL_PANEL_SIZE = 500;
        final int PANEL_SIZE = 500;

        int brightness = 0;


        BufferedImage bim = null;
        BufferedImage drawImage = null;

        boolean showBorder = true;

        Triangle[] triangles;

        int gridSize = 5;
        //TODO: make this variable
        int numHandles;
        int perimeterLength = gridSize + 2;
        int numPerimeter = gridSize * 4 + 4;

        double unit = (PANEL_SIZE-HANDLE_SIZE) / (perimeterLength-1) + .75;

        int[] border; //list of points on the perimeter that are not moveable
        int[] clickable;
        int drag = -1;
        int selected = -1;

        Rectangle[] perimeter;
        Rectangle[] rectangles;
        int numTotal;

        public void setGridSize(int size) {
            gridSize = size;
            gridSizeDependencies();
            repaint();
        }

        public int getGridSize() {
            return gridSize;
        }

        public void setBrightness(int bright) {
            //TODO: set brightness
            brightness = bright;
            repaint();
        }

        private int withinBounds(int val) {
            if(val > 255) return 255;
            if(val < 0) return 0;
            return val;
        }

        private void adjustBrightness() {
            drawImage = new BufferedImage(bim.getWidth(), bim.getHeight(), bim.getType());

            //TODO: set temporary image otherwise you'll lose all the data?
            int[] rgb;
            for(int i = 0; i < bim.getWidth(); i++) {
                for(int j = 0; j < bim.getHeight(); j++) {
                    rgb = bim.getRaster().getPixel(i, j, new int[3]);

                    int red = withinBounds(rgb[0] + brightness);
                    int green = withinBounds(rgb[1] + brightness);
                    int blue = withinBounds(rgb[2] + brightness);

                    int[] arr = {red, green, blue};

                    drawImage.getRaster().setPixel(i, j, arr);
                }
            }


            //TODO: do this for both images?
        }

        //updates all the variables that depend on the gridSize
        private void gridSizeDependencies() {

            numHandles = (int)Math.pow(gridSize, 2);
            perimeterLength = gridSize + 2;
            numTotal = (int)Math.pow(perimeterLength, 2);
            numPerimeter = numTotal - numHandles;
            border = new int[numPerimeter];



            perimeter = new Rectangle[numPerimeter];

            rectangles = new Rectangle[numTotal];


            unit = (PANEL_SIZE-HANDLE_SIZE) / (perimeterLength-1) + .75;

            for(int i = 0; i < numTotal; i++) {
                rectangles[i] = new Rectangle();
                rectangles[i].setRect((i%perimeterLength)*unit, (i/perimeterLength)*unit, HANDLE_SIZE, HANDLE_SIZE);
            }

            int borderIdx = 0;
            int counter = 0;

            for(int i = 0; i < numTotal; i++) {
                int row = i / perimeterLength;
                int col = i % perimeterLength;

                if(row == 0 || row == perimeterLength-1) {
                    border[borderIdx] = i;
                    borderIdx++;
                }
                else if(col == 0 || col == perimeterLength-1) {
                    border[borderIdx] = i;
                    borderIdx++;
                }
            }
        }




        public BufferedImage getBim() {
            return drawImage;
        }

        public int[] getBorderList() {
            return border;
        }

        public void addImage(BufferedImage bim) {
            int width = bim.getWidth();
            int height = bim.getHeight();
            int length = 0;
            int midpoint = 0;
            int startX = 0;
            int startY = 0;
            int endX = 0;
            int endY = 0;

            if(width > height) {
                length = height;
                midpoint = length/2;
                startX = midpoint - length/2;
                startY = 0;
                endX = midpoint + length/2;
                endY = length;
            }
            else {
                length = width;
                midpoint = height/2;
                startX = 0;
                startY = midpoint - length/2;
                endX = length;
                endY = midpoint + length/2;
            }

            BufferedImage newbim = bim.getSubimage(startX, startY, length, length);



            this.bim = newbim;
            repaint();
        }

        protected Rectangle[] getHandles() {
            return rectangles;
        }

        public void resetHandles() {
            for(int i = 0; i < numTotal; i++)
                rectangles[i].setRect((i%perimeterLength)*unit, (i/perimeterLength)*unit, HANDLE_SIZE, HANDLE_SIZE);
            repaint();
        }



        //copy constructor
        public HandlePanel(HandlePanel in) {
            //this.bim = in.bim;
            //this.bim = new BufferedImage(in.bim);
            //this.transbim = in.transbim;
            //this.transbim = new BufferedImage(in.transbim);
            this.showBorder = in.showBorder;
            this.gridSize = in.gridSize;
            this.numHandles = in.numHandles;
            this.perimeterLength = in.perimeterLength;
            this.numPerimeter = in.numPerimeter;
            this.unit = in.unit;
            this.border = in.border;
            this.clickable = in.clickable;
            this.drag = in.drag;
            this.selected = in.selected;
            this.numTotal = in.numTotal;

            this.rectangles = new Rectangle[in.rectangles.length];
            for(int i = 0; i < in.rectangles.length; i++) {
                this.rectangles[i] = new Rectangle((int)in.rectangles[i].getX(),
                        (int)in.rectangles[i].getY(),
                        HANDLE_SIZE, HANDLE_SIZE);
                }
        }

        public HandlePanel() {


            gridSizeDependencies();

            repaint();
        }

        public boolean containsInt(int[] values, int toFind) {
            for(int i = 0; i < values.length; i++) {
                if(values[i] == toFind)
                    return true;
            }
            return false;
        }



        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2D = (Graphics2D) g;

            adjustBrightness();

            g2D.drawImage(drawImage, 0, 0, this);

            drawAllTriangles(g, false, rectangles);

            for(int i = 0; i < numTotal; i++) {
                if(containsInt(border, i)) {
                    g2D.setColor(Color.BLUE);
                }
                else {
                    if(i == selected) {
                        g2D.setColor(Color.MAGENTA);
                    }
                    else {
                        g2D.setColor(Color.BLACK);
                    }
                }
                g2D.fill(rectangles[i]);
            }
        }

        //TODO: generalize for rectangles?
        //TODO: remove return values
        protected Triangle[] drawAllTriangles(Graphics g, boolean returnTri, Rectangle[] rectangles) {

            Triangle[] triToReturn = new Triangle[numTotal];
            int numTri = 0;

            for(int i = 0; i < numTotal; i++) {
                int row = i / perimeterLength;
                int col = i % perimeterLength;

                if(row != perimeterLength-1 && col != perimeterLength-1) { //bottom row, no triangles below
                    try {
                        if (returnTri) {
                            triToReturn[numTri] = drawTriangle(i, i + 1, i + perimeterLength, g, false);
                            numTri++;
                        } else
                            drawTriangle(i, i + 1, i + perimeterLength, g, true);
                    }
                    catch(ArrayIndexOutOfBoundsException e) {
                        System.err.println(numTotal);
                        System.err.println(perimeterLength);
                        System.exit(-1);
                    }
                }
                else {
                    if(row == perimeterLength-1 && col != perimeterLength-1) {
                        //draw lines across
                        int x1 = (int)rectangles[i].getX() + HANDLE_SIZE/2;
                        int y1 = (int)rectangles[i].getY() + HANDLE_SIZE/2;
                        int x2 = (int)rectangles[i+1].getX() + HANDLE_SIZE/2;
                        int y2 = (int)rectangles[i+1].getY() + HANDLE_SIZE/2;
                        g.drawLine(x1, y1, x2, y2);
                    }
                    if(col == perimeterLength-1 && row != perimeterLength-1) {
                        //draw lines down
                        int x1 = (int)rectangles[i].getX() + HANDLE_SIZE/2;
                        int y1 = (int)rectangles[i].getY() + HANDLE_SIZE/2;
                        int x2 = (int)rectangles[i+perimeterLength].getX() + HANDLE_SIZE/2;
                        int y2 = (int)rectangles[i+perimeterLength].getY() + HANDLE_SIZE/2;
                        g.drawLine(x1, y1, x2, y2);
                    }
                }
            }
            if(returnTri)
                return triToReturn;
            return null;
        }

        private Triangle drawTriangle(int point1, int point2, int point3, Graphics g, boolean drawTri) {
            int x1 = (int)rectangles[point1].getX() + HANDLE_SIZE/2;
            int y1 = (int)rectangles[point1].getY() + HANDLE_SIZE/2;

            int x2 = (int)rectangles[point2].getX() + HANDLE_SIZE/2;
            int y2 = (int)rectangles[point2].getY() + HANDLE_SIZE/2;

            int x3 = (int)rectangles[point3].getX() + HANDLE_SIZE/2;
            int y3 = (int)rectangles[point3].getY() + HANDLE_SIZE/2;

            Triangle tri = new Triangle(x1, y1, x2, y2, x3, y3);

            if(drawTri)
                tri.draw(g);
            return tri;
        }

        public void colorHandle(int i) {
            selected = i;
            repaint();
        }

        public Dimension getPreferredSize() {
            return new Dimension(FULL_PANEL_SIZE, FULL_PANEL_SIZE);
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        //mouseclick:
        //find which, if any, point is being clicked
        //highlight the relevant points
        public void clickHandle(int x, int y) {
            for (int i = 0; i < numTotal; i++) {
                if(!containsInt(border, i)) {
                    if (rectangles[i].contains(x, y)) {
                        //highlight the relevant points
                        //set them as the ones being moved
                        drag = i;
                        selectHandle(i);
                        repaint();
                        return;
                    }
                }
            }
        }

        //mousedrag:
        //move point around and re-render stuff
        //setRect() can be used to change the location of the rectangle
        public void moveHandle(int x, int y) {
            if(drag > -1) {
                rectangles[drag].setRect(x-HANDLE_SIZE/2, y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
                repaint();
            }
        }

        //mouseend:
        //do nothing? idk
        public void mouseDone() {
            drag = -1;
        }
    }
}