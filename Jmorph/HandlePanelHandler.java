public class HandlePanelHandler {
    HandlePanel h1;
    HandlePanel h2;

    public HandlePanelHandler() {
        h1 = new HandlePanel();
        h2 = new HandlePanel();
    }

    public HandlePanel getChild(int which) {
        if(which == 1)
            return h1;
        else if(which == 2)
            return h2;
        else
            System.out.println("Error: this child doesn't exist");
    }
}