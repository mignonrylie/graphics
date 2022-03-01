import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;

public class Minesweeper implements ActionListener {
    //game variables
    private static int cellSize = 30; //pixel size of icons
    private static int rows = 9;
    private static int cols = 9;
    private static int numCells = rows * cols;
    private static int numBombs = 10;
    private static int time = 0; //number of seconds elapsed
    private static int flagged = 0; //number of tiles flagged
    private static int bombsFlagged = 0; //number of bombs flagged (tiles correctly flagged)
    private static int numOpened = 0;
    private static boolean gameStart = false; //whether game is running or not
    public static boolean lock = false; //to prevent user interaction e.g. when game has been won

    //game objects
    private static Cell[][] board = new Cell[rows][cols];
    private static int[][] bombs = new int[numBombs][2];
    private static JFrame frame; //window to hold everything
    private static JPanel main; //panel inside window that holds the other panels
    private static JPanel gameBoard; //panel to hold cells
    private static JPanel menu; //panel to hold reset button, number flagged, and timer
    private static Timer timer;
    private static JButton reset;
    private static JLabel timeLabel; //holds text for timer
    private static JLabel flagLabel; //holds text for number flagged
    private static JButton submit; //submit custom game mode
    private JMenuItem beginner; //menu items corresponding to the game modes
    private JMenuItem intermediate;
    private JMenuItem expert;
    private JMenuItem custom;

    //window that appears to allow user to choose custom parameters
    private static void customPopup() {
        //create window and panel to hold everything
        JFrame parent = new JFrame("Custom Game");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        //min = 10% of cells, max = 25% of cells
        int minBombs = numCells/10;
        int maxBombs = numCells/4;

        //creating labels
        JLabel rowLabel = new JLabel();
        rowLabel.setText("Number of rows: " + rows);
        JLabel colLabel = new JLabel();
        colLabel.setText("Number of columns: " + cols);
        JLabel bombLabel = new JLabel();
        bombLabel.setText("Number of bombs: " + numBombs);

        //creating sliders
        JSlider rowSlider = new JSlider(9, 24);
        JSlider colSlider = new JSlider(9, 30);
        JSlider bombSlider = new JSlider(minBombs, maxBombs);
        rowSlider.setValue(16);
        colSlider.setValue(16);
        bombSlider.setValue((minBombs+maxBombs)/2);

        //behavior of sliders
        rowSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int usrRows = source.getValue();
                Minesweeper.rows = usrRows;
                rowLabel.setText("Number of rows: " + Integer.toString(usrRows));
                Minesweeper.numCells = Minesweeper.rows * Minesweeper.cols;
                bombSlider.setMinimum(Minesweeper.numCells/10);
                bombSlider.setMaximum(Minesweeper.numCells/4);
            }
        });
        colSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int usrCols = source.getValue();
                Minesweeper.cols = usrCols;
                colLabel.setText("Number of columns: " + Integer.toString(usrCols));
                Minesweeper.numCells = Minesweeper.rows * Minesweeper.cols;
                bombSlider.setMinimum(Minesweeper.numCells/10);
                bombSlider.setMaximum(Minesweeper.numCells/4);
            }
        });
        bombSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int usrBombs = source.getValue();
                Minesweeper.numBombs = usrBombs;
                bombLabel.setText("Number of bombs: " + Integer.toString(usrBombs));
            }
        });

        //panel for row slider
        JPanel row = new JPanel();
        row.add(rowLabel);
        row.add(rowSlider);

        //panel for column slider
        JPanel col = new JPanel();
        col.add(colLabel);
        col.add(colSlider);

        //panel for numBombs slider
        JPanel bombs = new JPanel();
        bombs.add(bombLabel);
        bombs.add(bombSlider);

        //panel to hold submit button
        JPanel submitPanel = new JPanel();
        submit = new JButton("Create game");
        //creates the new game and disposes of the input window
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
                init();

                parent.setVisible(false);
                parent.dispose();
            }
        });
        //add submit button to panel
        submitPanel.add(submit);

        //add each panel to main panel
        panel.add(row);
        panel.add(col);
        panel.add(bombs);
        panel.add(submitPanel);

        //add main panel to window
        parent.add(panel);
        parent.pack();
        parent.setVisible(true);
    }

    //check whether an int[] contains an int
    private boolean containsInt(int[] list, int num) {
        for(int i=0; i<list.length; i++) {
            if(list[i] == num) {
                return true;
            }
        }
        return false;
    }

    //check whether an int[][] contains an int[]
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

    //initalize the game; called at first run and whenever parameters are changed
    public static void init() {
        //add each of the menu members to the menu panel
        menu.add(flagLabel);
        menu.add(reset);
        menu.add(timeLabel);
        //add the menu to the main panel
        main.add(menu);

        //initialize game panel
        gameBoard = new JPanel();
        int hgap = 0;
        int vgap = 0;
        gameBoard.setLayout(new GridLayout(rows, cols, hgap, vgap));

        //instantiate Cell array
        board = new Cell[rows][cols];

        //initialize Cell array and add buttons to game panel
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                board[i][j] = new Cell(i, j, cellSize);
                gameBoard.add(board[i][j].getButton());
            }
        }

        //generating bombs
        Random rand = new Random();
        bombs = new int[numBombs][2];
        for(int i = 0; i < numBombs; i++) {
            //make sure coordinate isn't an existing bomb
            int[] coord = new int[2];
            do {
                coord[0] = rand.nextInt(rows);
                coord[1] = rand.nextInt(cols);
            } while (containsCoord(bombs, coord));
            bombs[i] = coord;
            board[coord[0]][coord[1]].setBomb();

            //inform the surrounding cells of the bomb
            for(int a = -1; a <= 1; a++) {
                for(int b = -1; b <= 1; b++) {
                    if(a+coord[0] >= 0 && a+coord[0] < rows && b+coord[1] >= 0 && b+coord[1] < cols) {
                        board[coord[0]+a][coord[1]+b].incBombs();
                    }
                }
            }
        }

        //add the game panel to the main panel
        main.add(gameBoard);

        //add the main panel to the window
        frame.add(main);

        //revalidate to account for the case that reset has been called
        frame.revalidate();
        //size the window
        frame.pack();
        //draw the window
        frame.repaint();
    }

    //reset game; called upon reset or when changing parameters
    public static void reset() {
        //allow the tiles to be clicked
        lock = false;

        //reset variables
        flagged = 0;
        flagLabel.setText("Bombs left: " + Integer.toString(numBombs - flagged));
        timer.stop();
        time = 0;
        timeLabel.setText("Seconds elapsed: " + Integer.toString(time));

        //remove existing components from window
        //start from scratch
        frame.invalidate();
        main.removeAll();
        frame.getContentPane().removeAll();
    }

    //this function opens tiles, as well as opening any neighboring tiles which have zero bombs.
    public static void openTiles(int rowIn, int colIn) {
        if(lock) {
            return;
        }

        if(!gameStart) {
            //if the first click is a bomb, move it somewhere else
            if(board[rowIn][colIn].getBomb()) {
                board[rowIn][colIn].setBomb();
                int newRow = rowIn;
                int newCol = colIn;
                do {
                    newRow++;
                    newCol++;
                }
                while(board[newRow][newCol].getBomb());
                board[newRow][newCol].setBomb();
            }

            gameStart = true;
            timer.start();
        }

        //end the game if a bomb is clicked
        if(board[rowIn][colIn].getBomb()) {
            timer.stop();
            timeLabel.setText("Game Over!");
            lock = true;

            //reveal all tiles
            for(int i = 0; i < rows; i++) {
                for(int j = 0; j < cols; j++) {
                    board[i][j].reveal();
                }
            }

            return;
        }

        //if all bombs have been flagged, game is won
        if(bombsFlagged == numBombs) {
            timer.stop();
            timeLabel.setText("You Won!");
            lock = true;
        }

        //if you open a cell that has been erroneously flagged, remove it from the count
        if(board[rowIn][colIn].getFlag()) {
            //no need to decrement bombsflagged
            //if it's being opened, it's not a bomb
            flagged--;
        }

        //list of all tiles that have already been opened
        int[][] opened = {};
        //list of tiles to check 1)whether it should open 2)for neighbors to open
        int[][] toCheck = {{rowIn, colIn}};

        //procedure:
        //if the tile does not contain a number, add all of its neighbors to a list to check.
        //do not add those neighbors if they are already in either list
        while(toCheck.length != 0) {
            //return/remove first element
            int[] current = toCheck[0];
            toCheck = removeFirst(toCheck);

            //renaming variables to make it more convenient
            int row = current[0];
            int col = current[1];

            //we only want to open and check the tiles with no bombs surrounding them
            if(board[row][col].getNumBombs() == 0) {
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

                //for each neighbor:
                while(neighbors.length > 0) {
                    int[] test = neighbors[neighbors.length - 1];
                    neighbors = removeLast(neighbors);

                    boolean inToCheck = false;
                    boolean inOpened = false;

                    if(toCheck.length > 0) {
                        if(containsCoord(toCheck, test)) {
                            inToCheck = true;
                        }
                    }

                    if(containsCoord(opened, test)) {
                        inOpened = true;
                    }

                    //if we haven't already checked the current neighbor for whether or not it should be opened, check it
                    if(!inToCheck && !inOpened) {
                        toCheck = push(toCheck, test);
                    }

                }
            }
            //open the current cell
            board[row][col].reveal();
            opened = push(opened, current);
        }

        //count the opened tiles
        numOpened = 0;
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j].getOpen()) {
                    numOpened++;
                }
            }
        }

        //if all non-bomb tiles have been opened, game is won
        if(numCells - numOpened == numBombs) {
            timer.stop();
            timeLabel.setText("You Won!");
            lock = true;
        }
    }

    //constructor; initializes constant members
    public Minesweeper() {
        //initialize window
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //create menu bar at top of window
        JMenuBar menuBar = new JMenuBar();
        //the only menu is difficulty
        JMenu jmenu = new JMenu("Difficulty");
        //create the various difficulty opeions
        beginner = new JMenuItem("Beginner");
        beginner.addActionListener(this);
        intermediate = new JMenuItem("Intermediate");
        intermediate.addActionListener(this);
        expert = new JMenuItem("Expert");
        expert.addActionListener(this);
        custom = new JMenuItem("Custom");
        custom.addActionListener(this);
        //add the difficulty options to the menu
        jmenu.add(beginner);
        jmenu.add(intermediate);
        jmenu.add(expert);
        jmenu.add(custom);

        //add the menu to the menu bar and that to the window
        menuBar.add(jmenu);
        frame.setJMenuBar(menuBar);

        //create main panel to hold game contents
        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.PAGE_AXIS));

        //menu panel to include reset button, timer, and num flagged
        menu = new JPanel();
        //reset button
        reset = new JButton("Reset");
        reset.addActionListener(this);
        //create timer and label
        timer = new Timer(1000, this);
        timeLabel = new JLabel();
        timeLabel.setText("Seconds elapsed: " + Integer.toString(time));
        //create label for num flagged
        flagLabel = new JLabel();
        flagLabel.setText("Bombs left: " + Integer.toString(numBombs - flagged));

        //TODO: clean up, enumerate, use constants?
        //initialize game
        //init(rows, cols);
        init();

        frame.setVisible(true);
    }

    //keeps track of user flagged and correctly flagged cells
    public static void numFlagged(int flag, int bombs) {
        flagged += flag;
        flagLabel.setText("Bombs left: " + Integer.toString(numBombs - flagged));

        bombsFlagged += bombs;
    }

    //create instance of class
    public static void main(String[] args) {
        Minesweeper m = new Minesweeper();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == reset) {
            gameStart = false;
            reset();
            init();
        }
        else if(e.getSource() == timer) {
            time++;
            timeLabel.setText("Seconds elapsed: " + Integer.toString(time));
        }
        else if(e.getSource() == beginner) {
            numBombs = 10;
            reset();
            rows = 9;
            cols = 9;
            init();
        }
        else if(e.getSource() == intermediate) {
            numBombs = 40;
            reset();
            rows = 16;
            cols = 16;
            init();
        }
        else if(e.getSource() == expert) {
            numBombs = 99;
            reset();
            rows = 20;
            cols = 24;
            init();
        }
        else if(e.getSource() == custom) { //launch popup window to choose game parameters
            customPopup();
        }
    }
}