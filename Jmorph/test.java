public class test {
    public static void main(String[] args) {
        int x = 5;
        int y = x;

        System.out.println(x);
        System.out.println(y);

        x = 6;

        System.out.println(x);
        System.out.println(y);


        testObject a = new testObject();
        testObject b = a;

        System.out.println(a.myInt);
        System.out.println(b.myInt);

        a.myInt = 5;
        System.out.println(a.myInt);
        System.out.println(b.myInt);

    }

    public class testObject {
        public int myInt = 1;

        public testObject() {

        }
    }
}