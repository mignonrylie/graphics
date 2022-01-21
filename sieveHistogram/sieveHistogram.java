public class sieveHistogram {
    public static void main(String[] args) {
        //declare array of one million booleans
        boolean[] primes = new boolean[1000000];

        //initialize array to all true
        for(int i=0; i<1000000; i++) {
            primes[i] = true;
        }

        int index = 2;
        do {
            //set false all elements whose index is a multiple of the current number
            for(int i=index+1; i<1000000; i++) {
                if(i%index == 0) {
                    primes[i] = false;
                }
            }

            //set index to be the next true element
            do {
                index++;
            } while(index < 1000000 && !primes[index]); //evaluate the index first. if not it'll throw out of bounds
        } while(index < 1000000);

        int sum = 0;
        for(int i=2; i<=99999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("2-99,999: %d\n", sum);

        sum = 0;
        for(int i=100000; i<=199999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("100,000-199,999: %d\n", sum);

        sum = 0;
        for(int i=200000; i<=299999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("200,000-299,999: %d\n", sum);

        sum = 0;
        for(int i=300000; i<=399999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("300,000-399,999: %d\n", sum);

        sum = 0;
        for(int i=400000; i<=499999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("400,000-499,999: %d\n", sum);

        sum = 0;
        for(int i=500000; i<=599999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("500,000-599,999: %d\n", sum);

        sum = 0;
        for(int i=600000; i<=699999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("600,000-699,999: %d\n", sum);

        sum = 0;
        for(int i=700000; i<=799999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("700,000-799,999: %d\n", sum);

        sum = 0;
        for(int i=800000; i<=899999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("800,000-899,999: %d\n", sum);

        sum = 0;
        for(int i=900000; i<=999999; i++) {
            if(primes[i]) sum++;
        }
        System.out.printf("900,000-999,999: %d\n", sum);
    }
}
