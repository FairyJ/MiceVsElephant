package src;

import java.util.*;

public class Mouse extends Thread {

    private final int steps = 2;
    private String name;
    private GameBoard board; 
    private Square square;  
    private int x;
    private int y;
    private int turn = 0;

    public Mouse (int x, int y, String name, GameBoard board) {
        this.x = x;
        this.y = y;
        this.name = name;
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
    
    public void setSquare(Square square){
        this.square = square;
    }

    public Square getSquare(){
        return this.square;
    }

    public int getSteps(){
        return this.steps;
    }

    public boolean move() {
        List<Elephant> el = board.mouseStrikeZone(this);
        //random direction
        List<Integer> directions = new ArrayList<Integer>(8);
        for (int i = 0; i < 8; i++){
            directions.add(i);
        }
        Collections.shuffle(directions);

        //if mouse is not within striking distance of elephant then move randomly to RU RD LU LD
        if(el.size() == 0){
            for (int i = 0; i < 8; i++){
                directions.add(i*2);
            }
        Collections.shuffle(directions);
        return true;
        }else{
            //if this elephant is in striking zone is in striking zone of another mouse  then move toward elephant
            if(board.AmIAlone(this, el).size() >= 2){
                for(Elephant elephant : el){
                    for(int i = 0 ; i < 8 ; i++){
                        if(this.board.miceGotCloser(directions.get(i), this, elephant)){
                            return true;
                        }
                    }  
                }
            } 
        }
        return false;
    }
    

    public String toString() {
        String result =  this.name + " to " + this.x + " " + this.y ;
        return result;
    }



}
