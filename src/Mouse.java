package src;

public class Mouse extends Thread {

    private String name;
    private GameBoard board;    
    private int x;
    private int y;


    public Mouse (String name , GameBoard board) {
        this.name = name;
        this.board = board;
    }

    public int getX(){
        //generate random number for x

        return this.x;
    }
    public int getY(){
        //generate random number for y
        
        return this.y;
    }
    
    public class micMove {

    }

}
