import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;

//as it stands, the game does not account for the user's first click
//i.e., it is possible to click a bomb on the first try
//while inconvenient, this does match the vast majority of Minesweeper implementations
public class Minesweeper implements ActionListener {
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

    //game variables
    private static int cellSize = 40; //pixel size of icons
    private static int rows = 9;
    private static int cols = 9;
    private int numCells = rows * cols;
    private static int numBombs = 10;
    private static int time = 0; //number of seconds elapsed
    private static int flagged = 0; //number of tiles flagged
    private static int userRows = 16; //user-inputted values for custom game mode
    private static int userCols = 16;
    private static int userBombs = 16;
    private static int availableCells = userRows * userCols; //used to calculate possible bombs in custom game mode
    private static boolean gameStart = false; //whether game is running or not

    //game objects
    private static Cell[][] newBoard = new Cell[rows][cols]; //
    private static int[][] bombs = new int[numBombs][2];
    private static JFrame frame; //window to hold everything
    private static JPanel main; //panel inside window that holds the other panels
    private static JPanel board; //panel to hold cells
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

    /*
    //likely not used
    private static int[] getFirst(int[][] arr) {
        return arr[0];
    }

     */

    //used
    private static int[][] removeFirst(int[][] arr) {
        return Arrays.copyOfRange(arr, 1, arr.length);
    }

    //used
    private static int[][] removeLast(int[][] arr) {
        return Arrays.copyOfRange(arr, 0, arr.length - 1);
    }

    //used
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

        //TODO: if all bombs have been flagged, game is won
        //or if all non-bomb tiles have been opened, game is won

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

        menu.add(flagLabel);
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
    }

    public Minesweeper() {
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        //frame.setVisible(false);
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


        //TODO: clean up, enumerate, use constants?
        init(9, 9);


        frame.setVisible(true);
    }

    public static void main(String[] args) {
        Minesweeper m = new Minesweeper();
    }

    public static void numFlagged(int num) {
        flagged += num;
        flagLabel.setText("Bombs flagged: " + Integer.toString(flagged));
        if(flagged == numBombs) {
            timer.stop();
            timeLabel.setText("You Won!");
        }
    }

    private static void customPopup() {
        JFrame parent = new JFrame("Custom Game");
        JPanel panel = new JPanel();


        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JTextField textField = new JTextField();

        final int val = 0;

        /*
        ChangeListener listener = new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent event)
            {
                // update text field when the slider value changes
                JSlider source = (JSlider) event.getSource();

                //val = source.getValue();
                textField.setText("" + source.getValue());
                System.out.println(source.getValue());
            }
        };

         */
        int minBombs = availableCells/10;
        int maxBombs = availableCells/4;

        JLabel rowLabel = new JLabel();
        rowLabel.setText("Number of rows: " + userRows);
        JLabel colLabel = new JLabel();
        colLabel.setText("Number of columns: " + userCols);
        JLabel bombLabel = new JLabel();
        bombLabel.setText("Number of bombs: " + userBombs);

        JSlider rowSlider = new JSlider(9, 24);
        JSlider colSlider = new JSlider(9, 30);
        JSlider bombSlider = new JSlider(minBombs, maxBombs);
        rowSlider.setValue(16);
        colSlider.setValue(16);
        bombSlider.setValue((minBombs+maxBombs)/2);

        rowSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int val = source.getValue();
                Minesweeper.userRows = val;
                rowLabel.setText("Number of rows: " + Integer.toString(val));
                Minesweeper.availableCells = Minesweeper.userRows * Minesweeper.userCols;
                bombSlider.setMinimum(Minesweeper.availableCells/10);
                bombSlider.setMaximum(Minesweeper.availableCells/4);
            }
        });
        //colSlider.addChangeListener(listener);
        colSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int usrCols = source.getValue();
                Minesweeper.userCols = usrCols;
                colLabel.setText("Number of columns: " + Integer.toString(usrCols));
                Minesweeper.availableCells = Minesweeper.userRows * Minesweeper.userCols;
                bombSlider.setMinimum(Minesweeper.availableCells/10);
                bombSlider.setMaximum(Minesweeper.availableCells/4);
            }
        });
        bombSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int usrBombs = source.getValue();
                Minesweeper.userBombs = usrBombs;
                bombLabel.setText("Number of bombs: " + Integer.toString(usrBombs));
            }
        });

        JPanel row = new JPanel();
        row.add(rowLabel);
        row.add(rowSlider);

        JPanel col = new JPanel();
        col.add(colLabel);
        col.add(colSlider);

        JPanel bombs = new JPanel();
        bombs.add(bombLabel);
        bombs.add(bombSlider);

        JPanel submitPanel = new JPanel();
        submit = new JButton("Create game");
        //submit.addActionListener(this);

        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reset();
                init(userRows, userCols);


                parent.setVisible(false);
                parent.dispose();
            }
            //parent.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        });

        submitPanel.add(submit);



        //panel.add(rowLabel);
        //panel.add(rowSlider);
        //panel.add(colLabel);
        //panel.add(colSlider);

        panel.add(row);
        panel.add(col);
        panel.add(bombs);
        panel.add(submitPanel);

        parent.add(panel);
        parent.pack();
        parent.setVisible(true);
    }

    //private static void helpPopup()

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == reset) {
            gameStart = false;
            reset();
            init(rows, cols);
        }
        else if(e.getSource() == timer) {
            time++;
            timeLabel.setText(Integer.toString(time));
        }
        else if(e.getSource() == beginner) {
            numBombs = 10;
            reset();
            init(9, 9);
        }
        else if(e.getSource() == intermediate) {
            numBombs = 40;
            reset();
            init(16, 16);
        }
        else if(e.getSource() == expert) {
            numBombs = 99;
            reset();
            init(20, 24);
        }
        else if(e.getSource() == custom) { //launch popup window to choose game parameters
            customPopup();
        }
        else if(e.getSource() == submit) { //activated when user submits parameters; initializes game
            reset();
        }
    }
}
