public class Jmorph {
    const int MIN_TWEEN = 1;
    const int MAX_TWEEN = 60;

    JFrame frame;
    JSpinner tweens;



    //user controls
    //image 1
    //image 2
    //5x5 (generalize) grid of control points (for each image) - correspondance via numbers?
        //nx2 array (25x2) for start and end?
        //25 parametric equations
    //number of tweens
    //preview button (new window?)
    private static void createLayout() {
        frame = new JFrame();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openFileStart = new JMenuItem("Open Start Image");
        JMenuItem openFileEnd = new JMenuItem("Open End Image");
        JPanel imagePanel = new JPanel();
        JPanel controlPanel = new JPanel();
        tweens = new JSpinner(new SpinnerNumberModel(MAX_TWEEN/2, MIN_TWEEN, MAX_TWEEN, 1));

        openFileStart.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //TODO: set open file start
                    }
                }
        );

        openFileEnd.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //TODO: set open file end
                    }
                }
        );

        tweens.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(changeEvent e) {
                        //TODO: set tween value
                    }
                }
        );


    }

    public Jmorph() {
        createLayout();


    }

    public static void main(String[] args) {
        Jmorph morph = new Jmorph();
    }
}
