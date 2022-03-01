public class AnimCurve2 extends JComponent implements Runnable
{
    /* points of control structure */
    int xs[] = new int[4],
            ys[] = new int[4],

    /* starting point of dragged handle */
    startx,
            starty;

    /* draggable point handles */
    Rectangle handles[] = new Rectangle[4];

    /* which handle is dragged */
    int drag = -1;

    /* are we animating the curve? */
    boolean animating = false;

    Bspline curve = new Bspline();

    public AnimCurve2()
    {
        setPreferredSize(new Dimension(400, 400));
        for (int i = 0; i < 4; i++) {
            handles[i] = new Rectangle();
        }
    }


    public void paint(Graphics g)
    {
        /* we always want the curve */
        curve.paintCurve(g);

        /* unless animating, we want the control structure too */
        if (!animating) {
            ((Graphics2D)g).fill(handles[0]);
            for (int i = 1; i < 4; i++) {
                g.drawLine(xs[i-1], ys[i-1], xs[i], ys[i]);
                ((Graphics2D)g).fill(handles[i]);
            }
        }

    }


    /* reset points */
    public void reset(int xs[], int ys[])
    {
        /* only if not animating */
        if (!animating) {
            curve.resetCurve();
            for (int i = 0; i < 4; i++) {
                this.xs[i] = xs[i];
                this.ys[i] = ys[i];
                handles[i].setRect(xs[i]-5, ys[i]-5, 10, 10);
                curve.addPoint(xs[i], ys[i]);
            }
            repaint();
        }
    }

    /* store starting point, which point is dragged */
    public void startDrag(int x, int y)
    {
        /* find which handle if any is trying to be dragged */
        for (int i = 0; i < 4; i++) {
            if (handles[i].contains(x, y)) {
                drag = i;
                startx = xs[i];
                starty = ys[i];
                return;
            }
        }
    }

    /* move the handle and repaint */
    public void doDrag(int x, int y)
    {
        /* only if a handle is being dragged */
        if (drag > -1) {
            xs[drag] = x;
            ys[drag] = y;
            handles[drag].setRect(xs[drag]-5, ys[drag]-5, 10, 10);
            repaint();
        }
    }

    /* start the animation */
    public void endDrag()
    {
        /* only if a handle was dragged */
        if (drag > -1) {
            (new Thread(this)).start();
        }
    }

    public void run()
    {
        try {
            animating = true;
            int j,

                    /* need to know where we end up */
                    endx = xs[drag],
                    endy = ys[drag];

            /* how much to move the dragged point each step */
            double stepx = (endx - startx) / 30.0,
                    stepy = (endy - starty) / 30.0;

				/* for the first 29 steps, increment dragged point and
				   rebuild curve, then repaint - with a delay */
            for (int i = 1; i < 30; i++) {
                curve.resetCurve();
                xs[drag] = (int)(startx + i * stepx);
                ys[drag] = (int)(starty + i * stepy);
                for (j = 0; j < 4; j++) {
                    curve.addPoint(xs[j], ys[j]);
                }
                repaint();
                Thread.sleep(50);
            }

				/* for the last step, set to computed end point to avoid
				   any round error that might occur */
            curve.resetCurve();
            xs[drag] = endx;
            ys[drag] = endy;
            for (j = 0; j < 4; j++) {
                curve.addPoint(xs[j], ys[j]);
            }

            /* make sure controls structure is redisplayed too */
            animating = false;
            repaint();
            drag = -1; // done with that point
        } catch (Exception e) {}
    }
}
