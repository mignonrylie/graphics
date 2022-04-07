import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class HandlePanelHandler extends JPanel{

    final int HANDLE_SIZE = 10;
    final int FULL_PANEL_SIZE = 550;

    HandlePanel h1;
    HandlePanel h2;

    int tweens = 30;

    Rectangle[] morphHandles;

    public HandlePanelHandler() {
        h1 = new HandlePanel();
        h2 = new HandlePanel();

        addListeners(h1);
        addListeners(h2);

        morphHandles = h1.getHandles();
        border = h1.getBorderList();

        JButton preview = new JButton("Preview");
        preview.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        morphTriangles();
                    }
            }
        );
        this.add(preview);
    }

    public void setTweens(int t) {
        tweens = t;
        System.out.println(t);
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

    Triangle[] tri;
    int[] border;

    public void morphTriangles() {

        System.out.println("morphing");
        //what do i need

        //starting and ending sets of triangles
        //ask child to return set of triangles?

        //border = h1.getBorderList();

        //Triangle[] startSet = h1.drawAllTriangles(null, true);
        //Triangle[] endSet = h2.drawAllTriangles(null, true);



        Rectangle[] startHandles = h1.getHandles();
        Rectangle[] endHandles = h2.getHandles();
        //morphHandles = h1.getHandles();
        resetHandles();


        //tri = startSet;


        //when t = tweens, be at endHandles
        //when t = 0, be at startHandles
        //(position) = (tweens - t) (startHandles) + (t) (endHandles)
        //(x, y) = (tweens - t) (startX, startY) + (t) (endX, endY)
        //x = tweens*startX - startX*t + endX*t
        //y = tweens*startY - startY*t + endX*t

        int[] tempX = new int[startHandles.length];
        int[] tempY = new int[startHandles.length];
        Rectangle[] tempHandles = new Rectangle[startHandles.length];

        morphHandles = new Rectangle[startHandles.length];

        //for(int t = 0; t < tweens; t++) {
        int t = tweens;
            for(int i = 0; i < tempX.length; i++) {
                int startX = (int)startHandles[i].getX();
                int startY = (int)startHandles[i].getY();
                int endX = (int)endHandles[i].getX();
                int endY = (int)endHandles[i].getY();

                //x = tweens*startX - startX*t + endX*t
                tempX[i] = (tweens*startX) - (startX*t) + (endX*t);
                //y = tweens*startY - startY*t + endX*t
                tempY[i] = (tweens*startY) - (startY*t) + (endY*t);

                morphHandles[i].setRect(tempX[i], tempY[i], HANDLE_SIZE, HANDLE_SIZE);
            }
            repaint();


        /*
        //TODO: combine the following into one loop?
        int[] xDiff = new int[startHandles.length];
        int[] yDiff = new int[startHandles.length];

        for(int i = 0; i < xDiff.length; i++) {
            xDiff[i] = (int)(h2.getHandles()[i].getX() - h1.getHandles()[i].getX());
            yDiff[i] = (int)(h2.getHandles()[i].getY() - h1.getHandles()[i].getY());
        }

        /*
        for(int i = 0; i < xDiff.length; i++) {
            xDiff[i] = (int)(endHandles[i].getX() - startHandles[i].getX());
            yDiff[i] = (int)(endHandles[i].getY() - startHandles[i].getY());
        }

         */

        /*
        int[] xInc = new int[xDiff.length];
        int[] yInc = new int[yDiff.length];

        for(int i = 0; i < xInc.length; i++) {
            xInc[i] = xDiff[i]/tweens;
            yInc[i] = yDiff[i]/tweens;
        }

        move(xDiff, yDiff);
        */

        /*
        for(int i = 0; i < tweens; i++) {
            move(xInc, yInc);
        }

         */





        /*
        int[] xInc = new int[xDiff.length];
        int[] yInc = new int[yDiff.length];

        for(int i = 0; i < xInc.length; i++) {
            xInc[i] = xDiff[i] / tweens;
            yInc[i] = yDiff[i] / tweens;
        }

        //morphHandles = startHandles;

        for(int i = 0; i < tweens; i++) {
            int originalX = (int)morphHandles[i].getX();
            int originalY = (int)morphHandles[i].getY();

            morphHandles[i].setRect(originalX+xInc[i], originalY+yInc[i], HANDLE_SIZE, HANDLE_SIZE);

            repaint();
        }

         */

        /*
        for(int j = 0; j < tweens; j++) {
            for(int i = 0; i < startHandles.length; i++) {
                int[] xDiff =
            }


            for(int i = 0; i < tri.length; i++) {
                int[] xs1 = startSet[i].getXs();
                int[] ys1 = startSet[i].getYs();
                int[] xs2 = endSet[i].getXs();
                int[] ys2 = endSet[i].getYs();

                int[] xdiff = new int[3];
                int[] ydiff = new int[3];

                for(int k = 0; k < 3; k++) {
                    xdiff[k] = xs2[k] - xs1[k];
                    ydiff[k] = ys2[k] - ys1[k];
                }

                int[] xinc = new int[3];
                int[] yinc = new int[3];

                for(int k = 0; k < 3; k++) {
                    xinc[k] = xdiff[k]/tweens;
                    yinc[k] = ydiff[k]/tweens;
                }

                int[] adjust = new int[6];
                adjust[0] = xinc[0];
                adjust[1] = yinc[0];
                adjust[2] = xinc[1];
                adjust[3] = yinc[1];
                adjust[4] = xinc[2];
                adjust[5] = yinc[2];

                tri[i].adjustPoints(adjust);
            }
            repaint();
            //TODO: pause
        }

         */
    }

    public void resetHandles() {
        for(int i = 0; i < morphHandles.length; i++) {
            morphHandles[i] = h1.getHandles()[i];
        }
    }

    private void move(int[] xInc, int[] yInc) {
        for(int i = 0; i < morphHandles.length; i++) {
            int originalX = (int)morphHandles[i].getX();
            int originalY = (int)morphHandles[i].getY();
            morphHandles[i].setRect(originalX+(xInc[i]), originalY+(yInc[i]/2), HANDLE_SIZE, HANDLE_SIZE);
        }
        repaint();

        try {
            Thread.sleep(50);
        }
        catch(InterruptedException e) {
            System.out.println("interrupted");
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(FULL_PANEL_SIZE, FULL_PANEL_SIZE);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }




    public void paintComponent(Graphics g) {

        super.paintComponent(g);


        //g.drawLine(0, 100, 0, 100);


        Graphics2D g2D = (Graphics2D) g;


        //TODO: i think i'll need this in part 2
        //g2D.drawImage(bim, 0, 0, this);

        //TODO: make functions like these static?
        h1.drawAllTriangles(g, false, morphHandles);

        //tri =


        for(int i = 0; i < morphHandles.length; i++) {
            //TODO: fix double function stuff
            if(h1.containsInt(border, i)) {
                g2D.setColor(Color.BLUE);
            }
            else {
                //if(i == selected) {
                    g2D.setColor(Color.MAGENTA);
                //}
                //else {
                    g2D.setColor(Color.BLACK);
                //}
            }
            g2D.fill(morphHandles[i]);
        }



        if(tri != null) {
            for(int i = 0; i < tri.length; i++) {
                tri[i].draw(g);
            }
        }
        /*
        public boolean containsInt(int[] values, int toFind) {
            for(int i = 0; i < values.length; i++) {
                if(values[i] == toFind)
                    return true;
            }
            return false;
        }

         */
    }






























    //I would rather this be its own class, but I can't figure out how to do this without extending JPanel
    class HandlePanel extends JPanel{
        final int HANDLE_SIZE = 10;
        final int FULL_PANEL_SIZE = 500;
        final int PANEL_SIZE = 500;


        BufferedImage bim = null;
        BufferedImage transbim;

        boolean showBorder = true;

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
        //int panelNumber = -1;

        //JPanel panel1, panel2;

        Rectangle[] handles;
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

        public HandlePanel() {
            handles = new Rectangle[numHandles];
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


            //g.drawLine(0, 100, 0, 100);


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
        //find which if any point is being clicked
        //highlight the relevant points
        public void clickHandle(int x, int y) {
            for (int i = 0; i < numTotal; i++) {
                if(!containsInt(border, i)) {
                    if (rectangles[i].contains(x, y)) {
                        //highlight the relevant points
                        //set them as the ones being moved
                        drag = i;
                        //selected = i;
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
                //if(this.getParent() == panel1)
                //handles[drag].setRect(x-HANDLE_SIZE/2, y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
                rectangles[drag].setRect(x-HANDLE_SIZE/2, y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
                //else if(this.getParent() == panel2)
                //handles[drag][1].setRect(x-HANDLE_SIZE/2, y-HANDLE_SIZE/2, HANDLE_SIZE, HANDLE_SIZE);
                //else
                //System.out.println("this should not be possible");
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