import javax.swing.*;

public class primeCalc {
    public static void main(String[] args) {
        JTextArea outputArea = new JTextArea();
        //outputArea.setText("Hello!");

        //JOptionPane.getRootFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //JOptionPane.showMessageDialog(null, outputArea, "some text", JOptionPane.INFORMATION_MESSAGE);
        //check out of bounds
        String out = "";
        int num = 0, status = JOptionPane.DEFAULT_OPTION;

        boolean success = true;
        do {
            do {
                out = JOptionPane.showInputDialog("Enter an integer to see if it's a prime.");
                if (out != null) {
                    try {
                        num = Integer.parseInt(out);
                        success = true;
                    } catch (NumberFormatException e) {
                        outputArea.setText("Please enter an integer!");
                        JOptionPane.showMessageDialog(null, outputArea, "NumberFormatException", JOptionPane.WARNING_MESSAGE);
                        success = false;
                        out = "";
                    }
                }
            } while (!success);

            outputArea.setText(Boolean.toString(sieveHistogram.isPrime(num)));
            JOptionPane.showMessageDialog(null, outputArea, "Result", JOptionPane.INFORMATION_MESSAGE);
            System.out.println(status);
            System.out.println(JOptionPane.CLOSED_OPTION);
            System.out.println(JOptionPane.DEFAULT_OPTION);
        } while (status != 1);

        //System.out.println(sieveHistogram.test());

        //System.out.println(sieveHistogram.isPrime(num));

        System.exit(0);

    }
}
