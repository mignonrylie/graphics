import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class HandlePanelHandler {
    HandlePanel h1;
    HandlePanel h2;

    public HandlePanelHandler() {
        h1 = new HandlePanel();
        h2 = new HandlePanel();

        addListeners(h1);
        addListeners(h2);
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




































    //I would rather this be its own class, but I can't figure out how to do this without extending JPanel
    class HandlePanel extends JPanel{
        final int HANDLE_SIZE = 10;
        final int FULL_PANEL_SIZE = 600;
        final int PANEL_SIZE = 500;

        boolean showBorder = true;

        int gridSize = 5;
        //TODO: make this variable
        int numHandles = (int)Math.pow(gridSize, 2);
        int perimeterLength = gridSize + 2;
        int numPerimeter = gridSize * 4 + 4;





        //0  1  2  3  4  5  6
        //7  8  9  10 11 12 13
        //14 15 16 17 18 19 20
        //21 22 23 24 25 26 27
        //28 29 30 31 32 33 34
        //35 36 37 38 39 40 41
        //42 43 44 45 46 47 48

        //if i/rowLength == 0 or rowLength-1 then it's perimeter
        //else
        //if j == 0 or j == rowLength-1 then it's perimeter



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
        //5x5 (generalize) grid of control points (for each image) - correspondance via numbers?
        //nx2 array (25x2) for start and end?
        //25 parametric equations
        //set of points on the border to allow triangles on outside?

        //this function will take two HandlePanels
        //public static void doMorph(HandlePanel h1, HandlePanel h2) {
        //return;
        //}

        public HandlePanel() {
            //TODO: generalize for rectangles, not just squares
            //length between handles
            int unit = PANEL_SIZE / perimeterLength;
            //panelNumber = panel;
            //this.panel1 = panel1;
            //this.panel2 = panel2;

            handles = new Rectangle[numHandles];
            perimeter = new Rectangle[numPerimeter];

            numTotal = (int)Math.pow(perimeterLength, 2);
            rectangles = new Rectangle[numTotal];

            numHandles = 5;
            numPerimeter = numTotal - (int)Math.pow(numHandles, 2);
            border = new int[numPerimeter];
            perimeterLength = numHandles + 2;

            for(int i = 0; i < numTotal; i++) {
                rectangles[i] = new Rectangle();
                rectangles[i].setRect((i%perimeterLength)*unit, (i/perimeterLength)*unit, HANDLE_SIZE, HANDLE_SIZE);
            }

            int borderIdx = 0;
            int counter = 0;
            /*
            for(int i = 0; i < perimeterLength; i++) {
                if(i == 0 || i == perimeterLength - 1) {
                    if(borderIdx >= 24) {
                        break;
                    }
                    border[borderIdx] = counter;
                    //border.append(counter);
                    borderIdx++;
                    counter++;
                }
                else {
                    for(int j = 0; j < perimeterLength; j++) {
                        if(j == 0 || j == perimeterLength - 1) {
                            if(borderIdx >= 24) {
                                break;
                            }
                            border[borderIdx] = counter;
                            borderIdx++;
                            //border.append(counter);
                        }
                        counter++;
                    }
                }
            }

             */

            //                        i / length(7) =
            //0  1  2  3  4  5  6     0
            //7  8  9  10 11 12 13    1
            //14 15 16 17 18 19 20    2
            //21 22 23 24 25 26 27    3
            //28 29 30 31 32 33 34    4
            //35 36 37 38 39 40 41    5
            //42 43 44 45 46 47 48    6
            //i % length(7) =
            //0  1  2  3  4  5  6

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



            /*
            int count = 0;
            for(int i = 0; i < perimeterLength; i++) {
                if(i == 0 || i == perimeterLength-1) { //top or bottom row
                    for(int j = 0; j < perimeterLength; j++) {
                        perimeter[count] = new Rectangle();
                        //x = j * unit
                        perimeter[count].setRect(j*unit, i*unit, HANDLE_SIZE, HANDLE_SIZE);
                        //y = i * unit

                        count++;
                    }
                }
                else { //middle rows
                    for(int j = 0; j < 2; j++) {
                        perimeter[count] = new Rectangle();
                        //x = j * (perimeterLength-1) * unit
                        //y = i * unit
                        perimeter[count].setRect(j*(perimeterLength-1)*unit, i*unit, HANDLE_SIZE, HANDLE_SIZE);

                        count++;
                    }
                }
                //0  1  2  3  4  5  6 (i == 0)
                //7                 8 (
                //9                 10
                //11                12
                //13                14
                //15                16
                //17 18 19 20 21 22 23 (i == perimeterLength-1)
            }

            //TODO: generalize for rectangles
            count = 0;
            for(int i = 0; i < gridSize; i++) {
                for(int j = 0; j < gridSize; j++) {
                    handles[count] = new Rectangle();
                    //x = (j+1) * unit

                    //y = (i+1) * unit
                    handles[count].setRect((j+1)*unit, (i+1)*unit, HANDLE_SIZE, HANDLE_SIZE);
                    count++;
                }
            }

             */


            //initialize all handles
            /*
            for(int i = 0; i < numHandles; i++) {
                handles[i] = new Rectangle();
                //windowsize/gridSize * i - HANDLE_SIZE/2


                handles[i].setRect(PANEL_SIZE/gridSize * (i/gridSize + 1) - HANDLE_SIZE/2,
                        PANEL_SIZE/gridSize * (i%gridSize + 1) - HANDLE_SIZE/2,
                        HANDLE_SIZE, HANDLE_SIZE);
            }

             */


            repaint();
        }

        public Component uhhh() {
            return this.getParent();
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

            drawAllTriangles(g);


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

            /*
            if(showBorder) {
                for(int i = 0; i < numPerimeter; i++) {
                    g2D.setColor(Color.BLUE);
                    g2D.fill(perimeter[i]);
                }
            }

            for(int i = 0; i < numHandles; i++) {
                if(i == selected){
                    g2D.setColor(Color.MAGENTA);
                }
                else {
                    g2D.setColor(Color.BLACK);
                }

                g2D.fill(handles[i]);
            }


            //g.drawLine((int)handles[0].getX()+HANDLE_SIZE/2, (int)handles[0].getY()+HANDLE_SIZE/2,
            //        (int)handles[1].getX()+HANDLE_SIZE/2, (int)handles[1].getY()+HANDLE_SIZE/2);

            Triangle tri = new Triangle((int)handles[0].getX()+HANDLE_SIZE/2, (int)handles[0].getY()+HANDLE_SIZE/2,
                    (int)handles[1].getX()+HANDLE_SIZE/2, (int)handles[1].getY()+HANDLE_SIZE/2,
                    (int)handles[5].getX()+HANDLE_SIZE/2, (int)handles[5].getY()+HANDLE_SIZE/2);

            tri.draw(g);

             */
        }

        private void drawUpperTriangles() {
            //0  1  2  3  4  5  6
            //7  0  1  2  3  4  8
            //9  5  6  7  8  9  10
            //11 10 11 12 13 14 12
            //13 15 16 17 18 19 14
            //15 20 21 22 23 24 16
            //17 18 19 20 21 22 23

            //just for perimeter

            for(int i = 0; i < perimeterLength-1; i++) { //row num
                //top row:
                if(i == 0) {
                    for(int j = 0; j < perimeterLength-1; j++) { //column num
                        if(j == 0) { //point below will be in perimeter

                        }
                        else { //point below will be in handles

                        }
                    }
                }
                //bottom row: point below will be in handles
                else if(i == perimeterLength-1) {

                }


                //inner rows
                for(int j = 0; j < 2; j++) {

                }

            }




            //within square
            //0, 1, 5
            //1, 2, 6
            //2, 3, 7
            //3, 4, 8
            //from 0 to gridsize-1:
            //

            //0, 1, 5
            //5, 6, 10
            //10, 11, 15
            //15, 16, 20
            //from 0 to gridsize-1:
            //

            for(int i = 0; i < gridSize-1; i++) { //row
                for(int j = 0; j < gridSize-1; j++) { //column
                    //points in triangle:
                    //j, j+1, i+1
                }
            }

        }

        private void drawLowerTriangles() {

        }

        //x x x x x
        //x x x x x
        //x x x x x
        //x x x x x
        //x x x x x

        //TODO: generalize for rectangles
        private void drawAllTriangles(Graphics g) {
            System.out.println(numTotal);
            for(int i = 0; i < numTotal; i++) {
                int row = i / perimeterLength;
                int col = i % perimeterLength;

                if(row != perimeterLength-1 && col != perimeterLength-1) { //bottom row, no triangles below

                    drawTriangle(i, i+1, i+perimeterLength, g);
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
        }

        private void drawTriangle(int point1, int point2, int point3, Graphics g) {
            int x1 = (int)rectangles[point1].getX() + HANDLE_SIZE/2;
            int y1 = (int)rectangles[point1].getY() + HANDLE_SIZE/2;

            int x2 = (int)rectangles[point2].getX() + HANDLE_SIZE/2;
            int y2 = (int)rectangles[point2].getY() + HANDLE_SIZE/2;

            int x3 = (int)rectangles[point3].getX() + HANDLE_SIZE/2;
            int y3 = (int)rectangles[point3].getY() + HANDLE_SIZE/2;

            Triangle tri = new Triangle(x1, y1, x2, y2, x3, y3);
            tri.draw(g);
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
                        System.out.println("handle grabbed");
                        drag = i;
                        //selected = i;
                        selectHandle(i);
                        repaint();
                        return;
                    }
                }
            }

            /*
            for (int i = 0; i < numHandles; i++) {
                if(handles[i].contains(x, y)) {
                    //highlight the relevant points
                    //set them as the ones being moved
                    System.out.println("handle grabbed");
                    drag = i;
                    //selected = i;
                    selectHandle(i);
                    repaint();
                    return;
                }
            }

             */
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
            System.out.println("done");
            drag = -1;
        }
    }

}