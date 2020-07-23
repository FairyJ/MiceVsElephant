package src;
import java.util.Random;

public class Elephant extends Thread{

    private final int steps = 1;
    private int nameIndex;
    private GameBoard board;    
    private int x;
    private int y;
    private Random rand;
    private int turn = 0;
    

    public Elephant (int x, int y, int nameIndex , GameBoard board) {
        this.x = x;
        this.y = y;
        this.nameIndex = nameIndex;
        this.board = board;
        this.rand = new Random();
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
        String result = "Elephant " + this.nameIndex + " to " + this.x + " " + this.y;
        return result;
    }

}

