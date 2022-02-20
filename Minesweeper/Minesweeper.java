import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Minesweeper {
    //counter for mines
    //timer for seconds (starts on first click)

    //menu:
    //start new game
    //settings:
        //grid size
        //difficulty
            //Beginner:  9x9 grid (81 cells) with 10 mines
            //Intermediate:  16x16 grid (256 cells) with 40 mines
            //Expert:  24x20 grid (480 cells) with 99 mines
            //Custom:  rows [9-30], columns [9-24], and mines from 10% to 25% of available cells for a given setting
    //quit
    //help

    private enum mode {
        BEGINNER,
        INTERMEDIATE,
        EXPERT,
        CUSTOM
    };

    private int rows = 9;
    private int cols = 9;
    private mode mode;

    private int numCells = rows * cols; //rows * cols

    private Cell[] cells = new Cell[numCells];
    private Timer timer;


    public Minesweeper() {
        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setJMenuBar()

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

        JPanel board = new JPanel();
        int hgap = 0;
        int vgap = 0;
        board.setLayout(new GridLayout(rows, cols, hgap, vgap));

        for(int i = 0; i < numCells; i++) {
            cells[i] = new Cell();
            board.add(cells[i].getButton());
        }

        main.add(board);

        frame.add(main);
        frame.pack();

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Minesweeper m = new Minesweeper();
        System.out.println("main");
    }
}
