//package graphics;

//import graphics.sieveHistogram;
import javax.swing.*;

public class primeCalc {
    public static void main(String[] args) {
        System.out.printf("Hello, world!\n");
        String out = "hello!";

        JTextArea outputArea = new JTextArea(11,10);
        outputArea.setText(out);

        JOptionPane.showMessageDialog(null, outputArea, "some text", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }


}