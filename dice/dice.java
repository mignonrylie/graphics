public class dice {
    private static int roll() {
        return (int)(Math.random()*6+1); //roll between 1 and 6
        //changing this to 2 worked.
    }

    private static int snakeEyes(int num) {
        int sum = 0;
        for(int i=0; i<num; i++) {
            if(roll() + roll() + roll() + roll() + roll() + roll() == 6) {
                sum++;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        /*Write a Java program that simulates the simultaneous rolling of three pairs of six-sided dice (six
        dice).  Run the simulation 100 times, 1000 times, 10000 times, and 100000 times.  For each run,
        print the number of times that “snake eyes” occurs, i.e., all three pairs of dice (six dice total)
        show a combined minimum value of six (i.e., all the dice show “1”).  Print the results in a table
        like this (your numbers will vary):

        100: 2 snake eyes
        1000:  28 snake eyes
        10000:  312 snake eyes
        100000:  1205 snake eyes*/

        System.out.printf("100: %d snake eyes\n", snakeEyes(100));
        System.out.printf("1000: %d snake eyes\n", snakeEyes(1000));
        System.out.printf("10000: %d snake eyes\n", snakeEyes(10000));
        System.out.printf("100000: %d snake eyes\n", snakeEyes(100000));
    }
}
