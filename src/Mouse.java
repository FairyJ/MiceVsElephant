package src;

public class Mouse extends Thread {

    private final int steps = 2;
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
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    
    public int getSteps(){
        return this.steps;
    }
    public boolean move() {
        if(this.board.moveMouse(1 , this))
            return true;
        else if(this.board.moveMouse(5 , this))
            return true;
        else if (this.board.moveMouse(3 , this))           
            return true;
        else if(this.board.moveMouse(7, this))
            return true;
        else
            return false;
    
    }


    public String toString() {
        String result = "I am a nice mouse and my name is " + this.name + "I live in (" + this.x + ", " + this.y + ")";
        return result;
    }

}
