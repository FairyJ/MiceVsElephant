package src;

public class Elephant extends Animal{

    private final int steps = 1;
    private String name;
    private GameBoard board;    
    private int x;
    private int y;
    

    public Elephant (int x, int y, String name , GameBoard board) {
        this.x = x;
        this.y = y;
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
        if(this.board.moveElephant(1 , this))
            return true;
        else if (this.board.moveElephant(3 , this))
            return true;
        else if(this.board.moveElephant(5 , this))
            return true;
        else if(this.board.moveElephant(7, this))
            return true;
        else
            return false;
    
        //if in my strike zone is one mouse run 
        
        //if in my strike zone is two mouse freez

        //if around me is empty move randomle
    }

    public String toString() {
        String result = "I am a nice Elephant and my name is " + this.name + "I live in (" + this.x + ", " + this.y + ")";
        return result;
    }

}

