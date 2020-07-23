package src;

public class Mouse extends Thread {

    private final int steps = 2;
    private int nameIndex;
    private GameBoard board;    
    private int x;
    private int y;
    private int turn = 0;

    
    public Mouse (int x, int y, int nameIndex, GameBoard board) {
        this.x = x;
        this.y = y;
        this.nameIndex = nameIndex;
        this.board = board;
    }

    public int getX(){

        return this.x;
    }
    public int getY(){

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
        String result = "Mouse " + nameIndex + " to " + this.x + " " + this.y ;
        return result;
    }

}
