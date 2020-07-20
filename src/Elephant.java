package src;
import java.util.Random;

public class Elephant extends Thread{

    private final int steps = 1;
    private String name;
    private GameBoard board;    
    private int x;
    private int y;
    private Random rand;
    

    public Elephant (int x, int y, String name , GameBoard board) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.board = board;
        this.rand = new Random();
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
        int randMove = this.rand.nextInt(8) + 1;
        while (!this.board.moveElephant(randMove , this)) {
            randMove = this.rand.nextInt(8) + 1;
        }
        return true;
    
        //if in my strike zone is one mouse run 
        
        //if in my strike zone is two mouse freeze

        //if around me is empty move randomly
    }

    public String toString() {
        String result = "I am a nice Elephant and my name is " + this.name + " I live in (" + this.x + ", " + this.y + ")";
        return result;
    }

}

