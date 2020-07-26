package src;
import java.util.*;

public class Elephant extends Thread{

    private final int steps = 1;
    private String name;
    private GameBoard board;    
    private Square square;
    private int x;
    private int y;
    private Random rand;
    private int turn = 0;
    

    public Elephant (int x, int y, String name, GameBoard board) {
        this.x = x;
        this.y = y;
        this.name = name;
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

    public void setSquare(Square square){
        this.square = square;
    }

    public Square getSquare() {
        return this.square;
    }

    public boolean move() {
        List<Integer> directions = new ArrayList<Integer>(8);
        for (int i = 0; i < 8; i++){
            directions.add(i);
        }
        Collections.shuffle(directions);

        List<Mouse> mice = board.elephantStrikeZone(this);
        // if there is mouse in my strike zone 
        if (!mice.isEmpty()) {
            // check how many mouse 
           if( mice.size() > 1){
                for (int dir = 0; dir < 8; dir++) {
                    if (this.board.elephantGotFurther(directions.get(dir), mice) && this.board.moveElephant(directions.get(dir), this)) {
                        return true;
                    }
                }
                return false;
            }  
        }
        // Choose a random direction 
        for (int dir = 0; dir < 8; dir++) {
            if (this.board.moveElephant(directions.get(dir), this)) {
                return true;
            }
        }
        return false;
    
    }

    public String toString() {
        String result =  this.name + " to " + this.x + " " + this.y ;
        return result;
    }


    public static void main(String[] args) {
        
    }

}

