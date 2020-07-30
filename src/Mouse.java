package src;

import java.util.*;
import java.util.Collections;

public class Mouse extends Thread {

    private final int steps = 2;
    private String name;
    private GameBoard board;
    private Square square;
    private int x;
    private int y;
    private int turn = 0;

    public Mouse(int x, int y, String name, GameBoard board) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.board = board;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public Square getSquare() {
        return this.square;
    }

    public int getSteps() {
        return this.steps;
    }

    public boolean move() {
        List<Elephant> el = board.mouseStrikeZone(this);
        List<Integer> directions;
        // if mouse is not within striking distance of elephant then move randomly to 
        // RU RD LU LD
        if (el.size() == 0) {
            directions= new ArrayList<Integer>(4);
            for (int i = 0; i < 4; i++) {
                directions.add(i * 2);
            }
            Collections.shuffle(directions);
            for (int i = 0; i < 4; i++) {
                if(this.board.moveMouse(directions.get(i), this)){
                    break;
                }  
            }
            turn++;
                return true;
            
        } else {
            List<Elephant> newEl = board.AmIAlone(this, el);
            if (newEl.size() > 0) {//you are not alone
                Elephant closestOnes = board.closestElephantToMe(this,newEl);
                
                turn++;
                return true;   
            } 
            
        }
        turn++;
        return false;
    }

    public String toString() {
        String result = this.name + " to " + this.x + " " + this.y;
        return result;
    }
    

}
