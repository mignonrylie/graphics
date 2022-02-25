import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;

public class Minesweeper implements ActionListener{
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

    private static int rows = 9;
    private static int cols = 9;

    private int numCells = rows * cols; //rows * cols
    private static int numBombs = 10;

    //int[] locations = {i-rows-1, i-rows, i-rows+1, //cells above
    //        i-1, i+1, //row of cell
    //        i+rows-1, i+rows, i+rows+1};

    private Cell[] cells = new Cell[numCells];
    private static Cell[][] newBoard = new Cell[rows][cols];
    private static int[][] bombs = new int[numBombs][2];
    private static Timer timer;
    private static int time = 0;
    private static boolean gameStart = false;
    private static JButton reset;
    private static JPanel menu;

    private static int cellSize = 40;

    //includes function for int[] and int
    private boolean containsInt(int[] list, int num) {
        for(int i=0; i<list.length; i++) {
            if(list[i] == num) {
                return true;
            }
        }
        return false;
    }

    //includes function for int[][] and int[]
    private static boolean containsCoord(int[][] list, int[] coord) {
        boolean equals = false;
        //check each coordinate in list
        for(int i = 0; i < list.length; i++) {
            //check each element of coordinate
            equals = true;
            for(int j = 0; j < coord.length; j++) {
                if(list[i][j] != coord[j]) {
                    equals = false;
                }
            }
            if(equals) {
                return equals;
            }
        }
        return equals;
    }


    //TODO: remove or fix this. bombs near the top left are not being counted correctly
    //not used?
    private void countNeighbors(int[] coord) {
        /*
        int row = coord[0];
        int col = coord[1];
        int[][] locations = {
                {row-1, col-1}, {row-1, col}, {row-1, col+1},
                {row, col-1}, {row, col+1},
                {row+1, col-1}, {row+1, col}, {row+1, col+1}
        };

        int sum = 0;
        for(int[] check : locations) {
            if(check[0] >= 0 && check[0] < rows && check[1] >= 0 && check[1] < cols) {
                //System.out.println(check[0]);
                //System.out.println(check[1]);
                //System.out.println(newBoard[0][1]);
                if(newBoard[check[0]][check[1]].getBomb()) {
                    sum++;
                }
            }
        }
        //return sum;
        newBoard[row][col].setNumBombs(sum);
        */



        int sum = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                //check each neighbor
                sum = 0;
                for(int a = -1; a <= 1; a++) {
                    for(int b = -1; b <= 1; b++) {
                        //valid locations only:
                        //row number cannot be < 0 or > num rows
                        //col number cannot be < 0 or > num cols
                        //don't want to check itself, so a&b cannot both be 0
                        //System.out.println(i+a);
                        //System.out.println(j+b);
                        //System.out.println();
                        if(i+a > 0 && i+a < rows &&
                                j+b > 0 && j+b < cols &&
                                !(a == 0 && b == 0)) {
                            //System.out.println(newBoard.length);
                            //System.out.println(newBoard[i].length);
                            //System.out.println(i+a);
                            //System.out.println(j+b);
                            //System.out.println(newBoard[i+a][j+b]);
                            if(newBoard[i+a][j+b].getBomb()) {
                                sum++;
                            }
                        }
                    }
                }
                newBoard[i][j].setNumBombs(sum);
            }
        }
    }


    //likely not used
    private static int[] getFirst(int[][] arr) {
        return arr[0];
    }

    private static int[][] removeFirst(int[][] arr) {
        return Arrays.copyOfRange(arr, 1, arr.length);
    }

    private static int[][] removeLast(int[][] arr) {
        return Arrays.copyOfRange(arr, 0, arr.length - 1);
    }

    private static int[][] push(int[][] arr, int[] val) {
        int[][] toReturn = Arrays.copyOf(arr, arr.length + 1);
        toReturn[arr.length] = val;
        return toReturn;
    }

    //this function opens tiles, as well as opening any neighboring tiles which have zero bombs.
    public static void openTiles(int rowIn, int colIn) {

        if(!gameStart) {
            gameStart = true;
            timer.start();
        }
        //i'm really not happy with this implementation, but I don't think I fully understand how Array.includes works.
        //maybe it doesn't

        /*
        if(bombs[i][j]) {
            gameOver = 1;
            clicked[i][j] = 1;
            return;
        }
         */
        if(newBoard[rowIn][colIn].getBomb()) {
            //gameOver
            timer.stop();
            timeLabel.setText("Game Over!");

            //reveal all tiles
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    newBoard[i][j].reveal();
                }
            }

            return;
        }

        //list of all tiles that have already been opened
        int[][] opened = {};
        //list of tiles to check 1)whether it should open 2)for neighbors to open
        int[][] toCheck = {{rowIn, colIn}};

        //safety to avoid infinite loops
        //TODO: remove this
        int count = 0;

        //procedure:

        //if the tile does not contain a number, add all of its neighbors to a list to check.
        //do not add those neighbors if they are already in either list
        while(toCheck.length != 0) {
            //safety
            //TODO: remove this
            if(count > 1000) {
                System.out.println("possible infinite loop");
                System.out.println(toCheck);
                break;
            }
            count++;


            //return/remove first element
            int[] current = toCheck[0];
            toCheck = removeFirst(toCheck);

            //renaming variables to make it more convenient
            int row = current[0];
            int col = current[1];

            //we only want to open and check the tiles with no bombs surrounding them
            if(newBoard[row][col].getNumBombs() == 0) {
                int[][] neighbors = {};

                //check the neighbors: row and col -1 up to row and col +1
                for(int a=-1; a<2; a++) {
                    for(int b=-1; b<2; b++) {
                        //current neighbor we're considering
                        int[] check = {current[0]+a, current[1]+b};
                        //ensuring we don't try to access out of bounds
                        if(check[0] < rows && check[0] >= 0 && check[1] < cols && check[1] >= 0 && !(a==0 && b==0)) {
                            //if it's a valid neighbor, add it to the list to check
                            neighbors = push(neighbors, check);
                        }
                    }
                }

                /*
                System.out.println("neighbors are");
                for(int i = 0; i < neighbors.length; i++) {
                    for(int j = 0; j < neighbors[i].length; j++) {
                        System.out.println(neighbors[i][j]);
                    }
                }
                System.out.println(neighbors);
                */

                while(neighbors.length > 0) {
                    //test = neighbors.pop();
                    int[] test = neighbors[neighbors.length - 1];
                    neighbors = removeLast(neighbors);



                    boolean inToCheck = false;
                    boolean inOpened = false;

                    if(toCheck.length > 0) {
                        if(containsCoord(toCheck, test)) {
                            inToCheck = true;
                        }
                        /*
                        for(int k=0; k<toCheck.length; k++) {
                            if(toCheck[k][0] == test[0] && toCheck[k][1] == test[1]) {
                                inToCheck = true;
                            }
                        }

                         */
                    }

                    if(containsCoord(opened, test)) {
                        inOpened = true;
                    }

                    /*
                    for(int k=0; k<opened.length; k++) {
                        if(opened[k][0] == test[0] && opened[k][1] == test[1]) {
                            inOpened = true;
                        }
                    }

                     */

                    if(!inToCheck && !inOpened) {
                        toCheck = push(toCheck, test);
                    }

                }
            }
            //clicked[current[0]][current[1]] = 1;
            newBoard[row][col].reveal();
            opened = push(opened, current);

        }


    }

    private static JLabel timeLabel;
    private static JLabel flagLabel;
    private static int flagged = 0;

    private JMenuItem beginner;
    private JMenuItem intermediate;
    private JMenuItem expert;
    private JMenuItem custom;
    private static JPanel main;
    private static JFrame frame;

    private static JPanel board;

    public static void init(int customRows, int customCols) {
        if(main.getComponentCount() > 1){
            main.removeAll();
            main.revalidate();
        }

        frame.getContentPane().removeAll();
        frame.invalidate();

        //if anything is in main, clear it



        //adding reset button to menu





        //main = new JPanel();

        menu.add(reset);
        menu.add(timeLabel);
        main.add(menu);




        rows = customRows;
        cols = customCols;


        board = new JPanel();
        int hgap = 0;
        int vgap = 0;
        board.setLayout(new GridLayout(rows, cols, hgap, vgap));

        newBoard = new Cell[rows][cols];

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                newBoard[i][j] = new Cell(i, j, cellSize);
                //System.out.println(newBoard[i][j]);
                board.add(newBoard[i][j].getButton());
                //set bombs
            }
        } //set neighbors





        //generating bombs
        //create numBombs sets of row,col pairs
        //of course checking that that isn't already a bomb
        Random rand = new Random();
        bombs = new int[numBombs][2];
        for(int i = 0; i < numBombs; i++) {
            int[] coord = new int[2];
            do {
                coord[0] = rand.nextInt(rows);
                coord[1] = rand.nextInt(cols);
            } while (containsCoord(bombs, coord));
            bombs[i] = coord;
            newBoard[coord[0]][coord[1]].setBomb();

            for(int a = -1; a <= 1; a++) {
                for(int b = -1; b <= 1; b++) {
                    if(a+coord[0] >= 0 && a+coord[0] < rows && b+coord[1] >= 0 && b+coord[1] < cols) {
                        newBoard[coord[0]+a][coord[1]+b].incBombs();
                    }
                }
            }
        }

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                int[] coord = {i, j};
                //countNeighbors(coord);
            }
        }

        main.add(board);


        frame.add(main);

        //frame.invalidate();

        frame.revalidate();
        frame.pack();
        frame.repaint();
    }

    public static void reset() {
        flagged = 0;
        flagLabel.setText("Bombs flagged: " + Integer.toString(flagged));

        timer.stop();
        time = 0;
        timeLabel.setText(Integer.toString(time));

        frame.invalidate();
        main.removeAll();
        frame.getContentPane().removeAll();
        frame.revalidate();
        //frame.pack();
        //frame.repaint();
    }


    //initializing things that never need to change,
    //but add them in init()?
    public Minesweeper() {
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setJMenuBar()

        frame.setVisible(false);
        JMenuBar menuBar = new JMenuBar();
        JMenu jmenu = new JMenu("Difficulty");

        beginner = new JMenuItem("Beginner");
        beginner.addActionListener(this);
        intermediate = new JMenuItem("Intermediate");
        intermediate.addActionListener(this);
        expert = new JMenuItem("Expert");
        expert.addActionListener(this);
        custom = new JMenuItem("Custom");
        custom.addActionListener(this);
        jmenu.add(beginner);
        jmenu.add(intermediate);
        jmenu.add(expert);
        jmenu.add(custom);

        JMenu game = new JMenu("Game");
        //JMenuItem reset = new JMenuItem("Reset");
        JMenuItem help = new JMenuItem("Help");



        menuBar.add(jmenu);
        frame.setJMenuBar(menuBar);

        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

        menu = new JPanel();
        reset = new JButton("Reset");
        reset.addActionListener(this);

        timer = new Timer(1000, this);
        timeLabel = new JLabel();
        timeLabel.setText(Integer.toString(time));

        flagLabel = new JLabel();
        flagLabel.setText("Bombs flagged: " + Integer.toString(flagged));
        //menu.add(timeLabel);

        //main.add(menu);

        //TODO: clean up, enumerate, use constants?
        init(9, 9);

        /*
        JPanel board = new JPanel();
        int hgap = 0;
        int vgap = 0;
        board.setLayout(new GridLayout(rows, cols, hgap, vgap));

        newBoard = new Cell[rows][cols];

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                newBoard[i][j] = new Cell(i, j);
                //System.out.println(newBoard[i][j]);
                board.add(newBoard[i][j].getButton());
                //set bombs
            }
        } //set neighbors





        //generating bombs
        //create numBombs sets of row,col pairs
        //of course checking that that isn't already a bomb
        Random rand = new Random();
        bombs = new int[numBombs][2];
        for(int i = 0; i < numBombs; i++) {
            int[] coord = new int[2];
            do {
                coord[0] = rand.nextInt(rows);
                coord[1] = rand.nextInt(cols);
            } while (containsCoord(bombs, coord));
            bombs[i] = coord;
            newBoard[coord[0]][coord[1]].setBomb();

            for(int a = -1; a <= 1; a++) {
                for(int b = -1; b <= 1; b++) {
                    if(a+coord[0] >= 0 && a+coord[0] < rows && b+coord[1] >= 0 && b+coord[1] < cols) {
                        newBoard[coord[0]+a][coord[1]+b].incBombs();
                    }
                }
            }
        }

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                int[] coord = {i, j};
                //countNeighbors(coord);
            }
        }

        main.add(board);
        */

        //frame.add(main);

        //frame.revalidate();
        //frame.repaint();
        //frame.pack();
        //frame.revalidate();
        //frame.repaint();

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Minesweeper m = new Minesweeper();
        System.out.println("main");
    }

    private int availableCells = rows * cols;

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == reset) {
            System.out.println("reset");
            //TODO: add reset
            //these commands will probably go into a reset function
            gameStart = false;
            reset();
            init(rows, cols);
        }
        else if(e.getSource() == timer) {
            System.out.println("tick");
            time++;
            timeLabel.setText(Integer.toString(time));

            menu.repaint();
        }
        else if(e.getSource() == beginner) {
            System.out.println("beginner");
            //TODO: add beginner mode
            //reset and resize
            numBombs = 10;
            reset();
            init(9, 9);
        }
        else if(e.getSource() == intermediate) {
            System.out.println("intermediate");
            //TODO: add intermediate mode
            numBombs = 40;
            reset();
            init(16, 16);
        }
        else if(e.getSource() == expert) {
            System.out.println("expert");
            //TODO: add expert mode
            numBombs = 99;
            reset();
            init(20, 24);
        }
        else if(e.getSource() == custom) {
            JOptionPane optionPane = new JOptionPane();
            JSlider rowSlider = new JSlider(9, 24);
            JSlider colSlider = new JSlider(9, 30);
            rowSlider.setValue(16);
            colSlider.setValue(16);
            int availableCells =
            JSlider mines = new JSlider
        }
        //Custom:  rows [9-30], columns [9-24], and mines from 10% to 25% of available cells for a given setting

    }
}
