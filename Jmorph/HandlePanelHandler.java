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
    BufferedImage img;

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


    public HandlePanelHandler() {
        h1 = new HandlePanel();
        h2 = new HandlePanel();

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

    //TODO: get rid of unused vriables

    public void iterate(int t) {
        Rectangle[] startHandles = h1.getHandles();
        Rectangle[] endHandles = h2.getHandles();

        int[] tempX = new int[startHandles.length];
        int[] tempY = new int[startHandles.length];

        for (int i = 0; i < tempX.length; i++) {
            double startX =  startHandles[i].getX();
            double startY =  startHandles[i].getY();
            double endX = endHandles[i].getX();
            double endY = endHandles[i].getY();

            tempX[i] = (int)(startX + t * ((endX - startX) / tweens));
            tempY[i] = (int)(startY + t * ((endY - startY) / tweens));

            morphHandles[i].setRect(tempX[i], tempY[i], HANDLE_SIZE, HANDLE_SIZE);
        }
    }

    public void morphTriangles() {
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


        g2D.drawImage(img, 0, 0, this);
    }

    //TODO: this is really stupid
    private void drawMyOwnTriangles(Graphics g) {

        //WARNING length wrong?
        Triangle[] triToReturn = new Triangle[morphHandles.length];
        int numTri = 0;

        for(int i = 0; i < morphHandles.length; i++) {
            int row = i / perimeterLength;
            int col = i % perimeterLength;

            if(row != perimeterLength-1 && col != perimeterLength-1) {
                    drawMyOwnSingleTriangle(g, i, i+1, i+perimeterLength);
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
            /*
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
             */

        }
        return triToReturn;
    }



    private int[] defineMatrix(int x1, int y1, int x2, int y2, int x3, int y3) {
        int a, b, c, d, e, f, g, h, i, j, k, l, m, n, o;
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

        return new int[]{a, b, c, d, e, f, g, h, i};
    }

    public void transformImage(BufferedImage in, Triangle[] triangles1, Triangle triangles2) {
        //for each triangle, define transform matrix and apply that transform to the image
        //and also fade from one image to the other



        //testing
        int[] xs1 = triangles1.getXs();
        int[] ys1 = triangles1.getYs();
        int[] xs2 = triangles2.getXs();
        int[] ys2 = triangles2.getYs();

        int[] matrix = defineMatrix(xs1[0], ys1[0], );

        AffineTransform trans = AffineTransform

    }






























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