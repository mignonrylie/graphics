import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;
import java.util.concurrent.TimeUnit;


import Jama.*;

/*
JPanel.setAlpha(float); //user defined, sets variable alpha within custom CompositePanel class


//within panel’s paintComponent
Graphics2D.drawImage(BufferedImage, x, y, observer (this)); //draw start image
AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
Graphics2D.setComposite(ac)
Graphics2D.drawImage(BufferedImage, x, y, this); //draw end image
 */

/*
affine transformation:
x' = T_x(x, y)
y' = T_y(x, y)
where (x, y) is a point in the image
affine: transformation T is linear:
x' = a_0x + a_1y + a_2
y' = b_0x + b_1y + b_2
as a matrix:
[x']   [a_0 a_1 a_2][x]
[y'] = [b_0 b_1 b_2][y]
[1 ]   [0   0   1  ][1]

a_0 and b_1 control scaling
a_2 b_2 and 1 control translation
a_0, a_1, b_0, and b_1 control rotation (as cos(theta), -sin(theta), sin(theta), and cos(theta)
 */


//TODO: check for calling child class when not necessary
public class HandlePanelHandler extends JPanel{
    BufferedImage startImg;
    BufferedImage endImg;
    BufferedImage morphImg;
    AffineTransform trans;

    //Triangle[] morphTriangles;

    int[] border;

    static boolean stop = true;

    final int HANDLE_SIZE = 10;
    final int FULL_PANEL_SIZE = 550;

    HandlePanel h1;
    HandlePanel h2;

    int tweens = 30;

    int perimeterLength;

    Rectangle[] morphHandles;
    Triangle[] triangles;
    Triangle[] destTriangles;


    public HandlePanelHandler() {


        h1 = new HandlePanel();
        h2 = new HandlePanel();

        try {
            //TODO: this will only fix the reference issue for the default image
            startImg = ImageIO.read(new File("cat1.jpeg"));
            endImg = ImageIO.read(new File("cat2.jpeg"));

            BufferedImage tempStart = ImageIO.read(new File("cat1.jpeg"));
            BufferedImage tempEnd = ImageIO.read(new File("cat2.jpeg"));

            setImage(tempStart, 1);
            setImage(tempEnd, 2);
        }
        catch(IOException e) {
            System.err.println("Image read exception");
        }

        addListeners(h1);
        addListeners(h2);


        HandlePanel h1Copy = new HandlePanel(h1);





        //TODO: replace with loadHandles?
        morphHandles = new Rectangle[49];
        morphHandles = h1Copy.getHandles();
        border = h1Copy.getBorderList();


        perimeterLength = h1.perimeterLength;

    }



    public void setTweens(int t) {
        tweens = t;
    }

    public void setImage(BufferedImage img, int which) {
        if(which == 1) {
            //startImg = img;
            //morphImg = img;
            morphImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
            morphImg = img;

            h1.addImage(img);
            h1.repaint();
        }
        else if(which == 2) {
            h2.addImage(img);
            h2.repaint();
        }
        else {
            System.out.println("Error: this child doesn't exist");
        }
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


    //TODO: this is not my code so clean it up
    /*
    protected BufferedImage applyTransform(BufferedImage bi,
                                           AffineTransform atx,
                                           int interpolationType){
        Dimension d = getSize();
        BufferedImage displayImage =
                new BufferedImage(d.width, d.height, interpolationType);
        Graphics2D dispGc = displayImage.createGraphics();
        dispGc.drawImage(bi, atx, this);
        return displayImage;
    }
    */

    /*
    protected BufferedImage applyTransform(BufferedImage bi,
                                           AffineTransform atx,
                                           int interpolationType){
        //Dimension d = getSize();
        //this is for width == height
        BufferedImage displayImage = new BufferedImage(bi.getHeight(),
                bi.getWidth(),
                bi.getType());
        Graphics2D dispGc = displayImage.createGraphics();
        AffineTransformOp atop = new AffineTransformOp(atx, interpolationType);
        return atop.filter(bi, null);
    }

     */

    /*
    private void doWarp() {
        Graphics2D g2 = morphImg.createGraphics();
        g2.setTransform(trans);
        g2.drawImage(morphImg, 0, 0, null);
        g2.dispose();
    }

     */



    //this code was taken almost verbatim from the given code, MorphTools.java
    public void alternateMatrix(double[] Sx, double[] Sy, double[] Dx, double[] Dy,
                                BufferedImage src, BufferedImage dest,
                                Object ALIASING, Object INTERPOLATION) {
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


    //TODO: get rid of unused vriables

    public void iterate(int t) {
        Rectangle[] startHandles = h1.getHandles();
        Rectangle[] endHandles = h2.getHandles();

        int[] tempX = new int[startHandles.length];
        int[] tempY = new int[startHandles.length];

        //TODO: it's probably here that I'll use the global matrix incrementally

        for (int i = 0; i < tempX.length; i++) {
            double startX =  startHandles[i].getX();
            double startY =  startHandles[i].getY();
            double endX = endHandles[i].getX();
            double endY = endHandles[i].getY();

            tempX[i] = (int)(startX + t * ((endX - startX) / tweens));
            tempY[i] = (int)(startY + t * ((endY - startY) / tweens));

            morphHandles[i].setRect(tempX[i], tempY[i], HANDLE_SIZE, HANDLE_SIZE);


            generateAllTriangles();

            //for each triangle
            //do morph
            for(int j = 0; j < triangles.length; j++) {
                //double[] Sx = intToDouble(triangles[j].getXs());
                //double[] Sy = intToDouble(triangles[j].getYs());
                //double[] Dx = intToDouble(triangles[j+1].getXs());
                //double[] Dy = intToDouble(triangles[j+1].getYs());
                //alternateMatrix(Sx, Sy, Dx, Dy, startImg, morphImg, null, null);
                morphOneTriangle(startHandles, endHandles, 0, 0, 0, triangles[j], destTriangles[j]);
            }
        }

        //TODO: create img
        if (t == tweens) {
            //System.out.println(trans == null);
            //morphImg = applyTransform(startImg, trans, 1);
            //doWarp();
        }

    }

    private double[] intToDouble(int[] arr) {
        double[] returnArr = new double[arr.length];
        for(int i = 0; i < arr.length; i++) {
            returnArr[i] = (double)arr[i];
        }

        return returnArr;
    }


    public void morphOneTriangle(Rectangle[] startHandles, Rectangle[] endHandles, int tri1, int tri2, int tri3,
                                 Triangle triangle1, Triangle triangle2) {
        //this code was taken from MorphTools.java
        double[] Sx = new double[3];
        double[] Sy = new double[3];
        double[] Dx = new double[3];
        double[] Dy = new double[3];

        Sx = triangle1.getXsDouble();
        Sy = triangle1.getYsDouble();
        Dx = triangle2.getXsDouble();
        Dy = triangle2.getYsDouble();



        /*
        Sx[0] = startHandles[tri1].getX();
        Sx[1] = startHandles[tri2].getX();
        Sx[2] = startHandles[tri3].getX();
        Sy[0] = startHandles[tri1].getY();
        Sy[1] = startHandles[tri2].getY();
        Sy[2] = startHandles[tri3].getY();

        Dx[0] = endHandles[tri1].getX();
        Dx[1] = endHandles[tri2].getX();
        Dx[2] = endHandles[tri3].getX();
        Dy[0] = endHandles[tri1].getY();
        Dy[1] = endHandles[tri2].getY();
        Dy[2] = endHandles[tri3].getY();


         */


        /*
        int[] tempSx = new int[3];
        int[] tempSy = new int[3];
        int[] tempDx = new int[3];
        int[] tempDy = new int[3];


        tempSx = triangle1.getXs();
        tempSy = triangle1.getYs();
        tempDx = triangle2.getXs();
        tempDy = triangle2.getYs();

        for(int i = 0; i < 2; i++) {
            Sx[i] = (double)tempSx[i];
            Sy[i] = (double)tempSy[i];
            Dx[i] = (double)tempDx[i];
            Dy[i] = (double)tempDy[i];
        }

         */




        //TODO: change this and give credit
        alternateMatrix(Sx, Sy, Dx, Dy, startImg, morphImg, null, null);
    }

    public void morphTriangles() {
        morphImg = new BufferedImage(startImg.getWidth(), startImg.getHeight(), startImg.getType());
        morphImg = startImg;

        //TODO: define transform matrix here as a global
        //TODO: this is repeated code
        Rectangle[] startHandles = h1.getHandles();
        Rectangle[] endHandles = h2.getHandles();

        Triangle tri1 = new Triangle(
                startHandles[8].getX(), startHandles[8].getY(),
                startHandles[9].getX(), startHandles[9].getY(),
                startHandles[15].getX(), startHandles[15].getY());
        Triangle tri2 = new Triangle(
                endHandles[8].getX(), endHandles[8].getY(),
                endHandles[9].getX(), endHandles[9].getY(),
                endHandles[15].getX(), endHandles[15].getY());

        //transformImage(tri1, tri2);



        morphOneTriangle(startHandles, endHandles, 8, 9, 15, tri1, tri2);
        //morphOneTriangle(startHandles, endHandles, 9, 15, 16, tri1, tri2);



        stop = false;
        Timer timer = new Timer(1, new ActionListener() {
            int count = 0;

            public void actionPerformed(ActionEvent e) {
                if(count < tweens) {
                    count++;
                    iterate(count);



                    repaint();
                } else {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        timer.setRepeats(true);
        //TODO: change duration? math
        timer.setDelay(10);
        timer.start();

        //TODO: this needs to reference the morphed triangles, if it doesn't
        //generateAllTriangles();

        //for(int i = 0; i < triangles.length/2; i++) {
        //    System.out.println(i);
        //    morphOneTriangle(startHandles, endHandles, 0, 0, 0, triangles[i], triangles[i+1]);
        //}
        repaint(); //can remove after testing i think
    }


    public void resetHandles() {
        HandlePanel h1Copy = new HandlePanel(h1);
        //TODO: needs to be variable
        morphHandles = new Rectangle[49];
        morphHandles = h1Copy.getHandles();
    }


    public Dimension getPreferredSize() {
        return new Dimension(FULL_PANEL_SIZE, FULL_PANEL_SIZE);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;

        //TODO: i think i'll need this in part 2
        //g2D.drawImage(bim, 0, 0, this);

        //TODO: make functions like these static?
        //h1.drawAllTriangles(g, false, morphHandles);
        //actually draws triangles
        //morphHandles is where I expect it to be

        //TODO: this method is bad
        drawMyOwnTriangles(g);

        for(int i = 0; i < morphHandles.length; i++) {
            //TODO: fix double function stuff
            if(h1.containsInt(border, i)) {
                g2D.setColor(Color.BLUE);
            }
            else {
                g2D.setColor(Color.BLACK);
            }
            g2D.fill(morphHandles[i]);
        }

        //if(trans!=null) {
        //    g2D.setTransform(trans);
        //}


        g2D.drawImage(morphImg, 0, 0, this);

    }

    int unit;

    private void generateAllTriangles() {
        triangles = new Triangle[2*perimeterLength*perimeterLength];
        destTriangles = new Triangle[2*perimeterLength*perimeterLength];
        Rectangle[] endHandles = h2.getHandles();
        int numTriangles = 0;
        for(int i = 0; i < perimeterLength - 1; i++) {
            for(int j = 0; j < perimeterLength - 1; j++) {
                //upper triangle
                triangles[numTriangles] = new Triangle(
                        morphHandles[j].getX() + HANDLE_SIZE/2,
                        morphHandles[i].getY() + HANDLE_SIZE/2,
                        morphHandles[j+1].getX() + HANDLE_SIZE/2,
                        morphHandles[i].getY() + HANDLE_SIZE/2,
                        morphHandles[j].getX() + HANDLE_SIZE/2,
                        morphHandles[i+1].getY() + HANDLE_SIZE/2);
                destTriangles[numTriangles] = new Triangle(
                        endHandles[j].getX() + HANDLE_SIZE/2,
                        endHandles[i].getY() + HANDLE_SIZE/2,
                        endHandles[j+1].getX() + HANDLE_SIZE/2,
                        endHandles[i].getY() + HANDLE_SIZE/2,
                        endHandles[j].getX() + HANDLE_SIZE/2,
                        endHandles[i+1].getY() + HANDLE_SIZE/2);
                numTriangles++;

                //lower triangle
                triangles[numTriangles] = new Triangle(
                        morphHandles[j].getX() + HANDLE_SIZE/2,
                        morphHandles[i+1].getY() + HANDLE_SIZE/2,
                        morphHandles[j+1].getX() + HANDLE_SIZE/2,
                        morphHandles[i].getY() + HANDLE_SIZE/2,
                        morphHandles[j+1].getX() + HANDLE_SIZE/2,
                        morphHandles[i+1].getY() + HANDLE_SIZE/2);
                destTriangles[numTriangles] = new Triangle(
                        endHandles[j].getX() + HANDLE_SIZE/2,
                        endHandles[i+1].getY() + HANDLE_SIZE/2,
                        endHandles[j+1].getX() + HANDLE_SIZE/2,
                        endHandles[i].getY() + HANDLE_SIZE/2,
                        endHandles[j+1].getX() + HANDLE_SIZE/2,
                        endHandles[i+1].getY() + HANDLE_SIZE/2);
                numTriangles++;
            }
        }
    }

    //TODO: reset morph on window open?


    //TODO: this is really stupid
    //TODO: change this method to just get every triangle for use in transformation
    private void drawMyOwnTriangles(Graphics g) {

        //WARNING length wrong?
        triangles = new Triangle[2*perimeterLength*perimeterLength];
        Triangle[] triToReturn = new Triangle[morphHandles.length];
        int numTri = 0;
        //num of triangles = 2*perimeterLength*perimeterLength

        //TODO: account for edge triangles?
        for(int i = 0; i < morphHandles.length; i++) {
            int row = i / perimeterLength;
            int col = i % perimeterLength;

            if(row != perimeterLength-1 && col != perimeterLength-1) {
                    drawMyOwnSingleTriangle(g, i, i+1, i+perimeterLength);
                    //triangles[numTri] = createTriangle(i, i+1, i*perimeterLength);
                    numTri++;
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
    private void drawMyOwnSingleTriangle(Graphics g, int point1, int point2, int point3) {
        /*
        int x1 = (int)morphHandles[point1].getX() + HANDLE_SIZE/2;
        int y1 = (int)morphHandles[point1].getY() + HANDLE_SIZE/2;

        int x2 = (int)morphHandles[point2].getX() + HANDLE_SIZE/2;
        int y2 = (int)morphHandles[point2].getY() + HANDLE_SIZE/2;

        int x3 = (int)morphHandles[point3].getX() + HANDLE_SIZE/2;
        int y3 = (int)morphHandles[point3].getY() + HANDLE_SIZE/2;

        Triangle tri = new Triangle(x1, y1, x2, y2, x3, y3);
         */

        Triangle tri = createTriangle(point1, point2, point3);

        tri.draw(g);
    }

    private Triangle createTriangle(int point1, int point2, int point3) {
        int x1 = (int)morphHandles[point1].getX() + HANDLE_SIZE/2;
        int y1 = (int)morphHandles[point1].getY() + HANDLE_SIZE/2;

        int x2 = (int)morphHandles[point2].getX() + HANDLE_SIZE/2;
        int y2 = (int)morphHandles[point2].getY() + HANDLE_SIZE/2;

        int x3 = (int)morphHandles[point3].getX() + HANDLE_SIZE/2;
        int y3 = (int)morphHandles[point3].getY() + HANDLE_SIZE/2;

        return new Triangle(x1, y1, x2, y2, x3, y3);
    }


    private Triangle[] getAllTriangles() {
        //WARNING length wrong?
        Triangle[] triToReturn = new Triangle[morphHandles.length];
        int numTri = 0;

        for(int i = 0; i < morphHandles.length; i++) {
            int row = i / perimeterLength;
            int col = i % perimeterLength;

            if (row != perimeterLength - 1 && col != perimeterLength - 1) {
                triToReturn[i] = createTriangle(i, i + 1, i + perimeterLength);
            }
        }
        return triToReturn;
    }


    /*
    private double[] defineMatrix(double x1, double y1, double x2, double y2, double x3, double y3) {
        double a, b, c, d, e, f, g, h, i, j, k, l, m, n, o;
        j = x1 - x2 - x3;
        k = -x1 - x2 + x3;
        l = -x1 + x2 - x3;
        m = y1 - y2 - y3;
        n = -y1 - y2 + y3;
        o = -y1 + y2 - y3;

        i = 1;
        h = ((j*o - m*l)*i) / (m*k - j*n);
        g = (k*h + l*i) / j;
        f = (y1*(g+h+i) + y3*(-g-h+i)) / 2;
        e = (y1*(g+h+i) - y2*(g-h+i)) / 2;
        d = y1*(g+h+i) - f - e;
        c = (x1*(g+h+i) + x3*(-g-h+i)) / 2;
        b = (x1*(g+h+i) - x2*(g-h+i)) / 2;
        a = x1*(g+h+i) - c - b;

        //return new int[]{a, b, c, d, e, f, g, h, i};


        // method 2
        a = y2-y3;
        b = x3-x2;
        c = x2*y3 - x2*y2;
        d = y3-y1;
        e = x1-x3;
        f = x3*y1 - x1*y3;
        g = y1-y2;
        h = x2-x1;
        i = x1*y2 - x2*y1;

        double factor = 1 / (
                x1*(y2-y3) -
                x2*(y1-y3) +
                x3*(y1-y2));

        a = a*factor;
        b = b*factor;
        c = c*factor;
        d = d*factor;
        e = e*factor;
        f = f*factor;
        g = g*factor;
        h = g*factor;
        i = i*factor;

        return new double[]{a, b, c, d, e, f, g, h, i};



    }

     */

    //TODO: generalize?
    /*
    private double[] multiplyMatrix(double[] X, double[] A) {
        double a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r;
        a = X[0];
        b = X[1];
        c = X[2];
        d = X[3];
        e = X[4];
        f = X[5];
        g = X[6];
        h = X[7];
        i = X[8];
        j = A[0];
        k = A[1];
        l = A[2];
        m = A[3];
        n = A[4];
        o = A[5];
        p = A[6];
        q = A[7];
        r = A[8];

        return new double[] {
            a*j + b*m + c*p,
            a*k + b*n + c*q,
            a*l + b*o + c*r,
            d*j + e*m + f*p,
            d*k + e*n + f*q,
            d*l + e*o + f*r,
            g*j + h*m + i*p,
            g*k + h*n + i*q,
            g*l + h*o + i*r
        };
    }

     */

    /*
    public void transformImage(Triangle triangles1, Triangle triangles2) {
        //for each triangle, define transform matrix and apply that transform to the image
        //and also fade from one image to the other



        //testing
        int[] xs1 = triangles1.getXs();
        int[] ys1 = triangles1.getYs();
        int[] xs2 = triangles2.getXs();
        int[] ys2 = triangles2.getYs();

        double[] A = defineMatrix(
                (double)xs1[0], (double)ys1[0],
                (double)xs1[1], (double)ys1[1],
                (double)xs1[2], (double)ys1[2]);
        //multiply matrix
        double[] X = new double[] {
                xs2[0], ys2[0], 1,
                xs2[1], ys2[1], 1,
                xs2[2], ys2[2], 1
        };

        double[] T = multiplyMatrix(X, A);


        trans = new AffineTransform(T[0], T[3], T[1], T[4], T[2], T[5]);
        //trans = new AffineTransform(T[3], T[0], T[4], T[1], T[5], T[2]);


    }

     */






























    //I would rather this be its own class, but I can't figure out how to do this without extending JPanel
    class HandlePanel extends JPanel implements Cloneable{
        final int HANDLE_SIZE = 10;
        final int FULL_PANEL_SIZE = 500;
        final int PANEL_SIZE = 500;


        BufferedImage bim = null;
        BufferedImage transbim;

        boolean showBorder = true;

        Triangle[] triangles;

        int gridSize = 5;
        //TODO: make this variable
        int numHandles = (int)Math.pow(gridSize, 2);
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


        public BufferedImage getBim() {
            return bim;
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
            perimeter = new Rectangle[numPerimeter];

            numTotal = (int)Math.pow(perimeterLength, 2);
            rectangles = new Rectangle[numTotal];

            numHandles = 5;
            numPerimeter = numTotal - (int)Math.pow(numHandles, 2);
            border = new int[numPerimeter];
            perimeterLength = numHandles + 2;


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
                else if(col == 0 || col == 6) {
                    border[borderIdx] = i;
                    borderIdx++;
                }
            }
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

            g2D.drawImage(bim, 0, 0, this);

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

        //TODO: generalize for rectangles
        //TODO: remove return values
        protected Triangle[] drawAllTriangles(Graphics g, boolean returnTri, Rectangle[] rectangles) {

            Triangle[] triToReturn = new Triangle[numTotal];
            int numTri = 0;

            for(int i = 0; i < numTotal; i++) {
                int row = i / perimeterLength;
                int col = i % perimeterLength;

                if(row != perimeterLength-1 && col != perimeterLength-1) { //bottom row, no triangles below
                    if(returnTri) {
                        triToReturn[numTri] = drawTriangle(i, i+1, i+perimeterLength, g, false);
                        numTri++;
                    }
                    else
                        drawTriangle(i, i+1, i+perimeterLength, g, true);
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