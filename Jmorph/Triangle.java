import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.imageio.*;

public class Triangle extends JFrame {
    int[] xs, ys;


    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        xs = new int[3];
        ys = new int[3];
        xs[0] = x1;
        xs[1] = x2;
        xs[2] = x3;
        ys[0] = y1;
        ys[1] = y2;
        ys[2] = y3;
    }



    /*
    public Triangle(int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        xs = new int[4];
        ys = new int[4];
        xs[0] = x1;
        xs[1] = x2;
        xs[2] = x3;
        xs[3] = x4;
        ys[0] = y1;
        ys[1] = y2;
        ys[2] = y3;
        ys[3] = y4;
    }

     */

    public void draw(Graphics g) {
        //super.paintComponent(g);
        g.drawLine(xs[0], ys[0], xs[1], ys[1]);
        g.drawLine(xs[1], ys[1], xs[2], ys[2]);
        g.drawLine(xs[2], ys[2], xs[0], ys[0]);
    }
    public void draw2(Graphics g) {
        g.drawLine(xs[0], ys[0], xs[1], ys[1]);
        g.drawLine(xs[0], ys[0], xs[3], ys[3]);
        g.drawLine(xs[1], ys[1], xs[4], ys[4]);
        g.drawLine(xs[3], ys[3], xs[4], ys[4]);
    }

}