import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

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
    private int numBombs = 10;

    //int[] locations = {i-rows-1, i-rows, i-rows+1, //cells above
    //        i-1, i+1, //row of cell
    //        i+rows-1, i+rows, i+rows+1};

    private Cell[] cells = new Cell[numCells];
    private int[] bombs = new int[numBombs];
    private Timer timer;

    private boolean containsInt(int[] list, int num) {
        for(int i=0; i<list.length; i++) {
            if(list[i] == num) {
                return true;
            }
        }
        return false;
    }

    /*
    private void countNeighbors(int i) {
        //int[] locations = new Int[8];
        //int[] locations = {i-rows-1, i-rows, i-rows+1, //cells above
        //                i-1, i+1, //row of cell
        //            i+rows-1, i+rows, i+rows+1}; //cells below

        for(int n : locations) { //for each neighbor
            if(n >= 0 && n < numCells) { //valid locations only
                if(containsInt(bombs, n)) { //if that neighbor is a bomb
                    cells[i].incBombs();
                }
            }
        }
    }

     */

    private void openTiles() {
        //opened
        //toCheck

    }

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


        //generating bmbs
        //create numBombs sets of row,col pairs
        //of course checking that that isn't already a bomb
        Random rand = new Random();
        bombs = new int[numBombs];
        for(int i = 0; i < numBombs; i++) {
            int num;
            do {
               num = rand.nextInt(numCells);
               System.out.println(num);
            } while (containsInt(bombs, num));

            bombs[i] = num;
        }

        Cell[][] newBoard = new Cell[rows][cols];

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                newBoard[i][j] = new Cell();
                board.add(newBoard[i][j].getButton());
                //set bombs
            }
        } //set neighbors

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
