public class sieveHistogram {
    public static void main(String[] args) {
        //can you see this change?
        /*The Sieve of Eratosthenes.  A prime number is any integer greater than one that is evenly
        divisible only by itself and 1.  The Sieve of Eratosthenes is an ancient method of finding prime
        numbers.  We could apply it using rocks, but we have computers, so we will use them.  The
        “Sieve” operates as follows:

        1. Create an array of type boolean with all elements initialized to true.  Array elements
        in Java begin at 0.  The sieve is constructed to ignore element 0 and 1 (those indices are
        not prime numbers) and to generate an array where any index that is a prime number will
        contain a value of true in that array position.  All other array elements will eventually
        be set to false.

        2. Starting with array index 2, look through the remainder of the array and set to false
        every element whose index is a multiple of two (except 2).  Continue the process with the
        next element with value true (which will be 3) – set to false array positions at
        multiples of 3.  For array index 2, all elements beyond element 2 in the array that have
        indices which are multiples of 2 (4, 6, 8, 10, etc.) will be set to false;  for array index 3,
        all elements beyond element 3 in the array that have indices which are multiples of 3
        (indices 6, 9, 12, 15, etc.) will be set to false;  and so on.

        When this process completes, the array elements that are still true indicate that the index is a
        prime number. */

        //BE PATIENT. This program takes about 4 minutes to run (on my machine).

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
