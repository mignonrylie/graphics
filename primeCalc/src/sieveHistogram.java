public class sieveHistogram {
    static boolean[] primes;

    static {
        primes = new boolean[1000000];
        for(int i=0; i<1000000; i++) {
            primes[i] = true;
        }

        int index = 2;
        do {
            //set false all elements whose index is a multiple of the current number
            for(int i=index*2; i<1000000; i += index) {
                primes[i] = false;
            }

            //set index to be the next true element
            do {
                index++;
            } while(index < 1000000 && !primes[index]); //evaluate the index first. if not it'll throw out of bounds
        } while(index < 1000000);
    }

    public static boolean isPrime(int num) {
        return(primes[num]);
    }
}
