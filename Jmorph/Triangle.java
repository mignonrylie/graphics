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
    double[] xsDouble, ysDouble;


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

    public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        xsDouble = new double[3];
        ysDouble = new double[3];
        xsDouble[0] = x1;
        xsDouble[1] = x2;
        xsDouble[2] = x3;
        ysDouble[0] = y1;
        ysDouble[1] = y2;
        ysDouble[2] = y3;
    }


    public void adjustPoints(int[] amount) {
        xs[0] += amount[0];
        ys[0] += amount[1];
        xs[1] += amount[2];
        ys[1] += amount[3];
        xs[2] += amount[4];
        ys[2] += amount[5];
    }

    public int[] getXs() {
        return xs;
    }

    public double[] getXsDouble() {
        return xsDouble;
    }

    public int[] getYs() {
        return ys;
    }

    public double[] getYsDouble() {
        return ysDouble;
    }

    public void draw(Graphics g) {
        //super.paintComponent(g);
        g.drawLine(xs[0], ys[0], xs[1], ys[1]);
        g.drawLine(xs[1], ys[1], xs[2], ys[2]);
        g.drawLine(xs[2], ys[2], xs[0], ys[0]);
    }

}