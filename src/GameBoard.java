/*@author Farzaneh Jahani
  @data July/16/2020
  this is my board class, and also my start point
 */
package src;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


public class GameBoard {
    // board dimensions
    private int squaresWide;
    private int squaresTall;

    private int numMice;
    private int numElpahnet;
    private int strinkingDistance;
    private Square[][] board;
    

    public GameBoard(int squaresWide, int squaresTall, int strinkingDistance, int numMice, int numElpahnet) {
        this.squaresWide = squaresWide;
        this.squaresTall = squaresTall;
        this.strinkingDistance = strinkingDistance;
        this.numMice = numMice;
        this.numElpahnet = numElpahnet;
        this.board = new Square[this.squaresWide][this.squaresTall];
    }

    public boolean play(){

        return true;

    }

    public boolean moveMouse(int dir, Mouse m){
        int[] position = this.move(dir , m.getX() , m.getX() , m.getSteps() );
        if(position[0] < 0 || position[0] > squaresWide-1 || position[1] < 0 || position[1] > squaresTall-1){
            return false;
        }
        this.board[m.getX()][m.getY()].removeMouse(m);
        m.setX(position[0]); 
        m.setY(position[1]);
        if (this.board[position[0]][position[1]] != null) {
            this.board[position[0]][position[1]].addMouse(m);
        } else {
            this.board[position[0]][position[1]] = new Square(position[0], position[1]);
            this.board[position[0]][position[1]].addMouse(m);
        }
        
        return true;
    }

    public boolean moveElephant(int dir, Elephant el){
        int[] position = this.move(dir , el.getX() , el.getY() , el.getSteps() );
        if(position[0] < 0 || position[0] > squaresWide-1 || position[1] < 0 || position[1] > squaresTall-1){
            return false;
        }
        this.board[el.getX()][el.getY()].removeElephant(el);
        el.setX(position[0]); 
        el.setY(position[1]);
        if (this.board[position[0]][position[1]] != null) {
            this.board[position[0]][position[1]].addElephant(el);
        } else {
            this.board[position[0]][position[1]] = new Square(position[0], position[1]);
            this.board[position[0]][position[1]].addElephant(el);
        }
        return true;
    }

    private int[] move(int dir, int x , int y , int steps){
       
        int[] result = {x, y};

        switch (dir) {
            //Up
            case 1:
                result [1] = y-1; 
                break;
            //Right
            case 3:
                result[0] =  x+1;
                break;
            //Down
            case 5:
                result[1] = y+1; 
                break;
            //Left
            case 7:
                result[0] = x-1;
                break; 
            //RIGHT UP
            case 2:
                result[0] = x+1;
                result[1] = y-1; 
                break; 
            //RIGHT DOWN    
            case 4:
                result[0] = x+1; 
                result[1] = y+1;
                break;
            //LEFT DOWN
            case 6:
                result[0] = x-1;
                result[1] = y+1;  
                break; 
            //LEFT UP
            case 8:
                result[0] = x-1;
                result[1] = y-1; 
                break;  
            
        }
        return result;
    }

    public static void main(String[] args) {
        GameBoard board = new GameBoard(100, 100, 6, 10, 5);
        Elephant el = new Elephant(20 , 85, "el1" ,board );
        board.board[el.getX()][el.getY()] = new Square(el.getX(), el.getY());
        board.board[el.getX()][el.getY()].addElephant(el);
        System.out.println(el);
        while (el.getX() != el.getY()) {
            el.move();
            System.out.println(el);
        }
        // board.moveElephant(3, el);
        // System.out.println(el);
        // board.moveElephant(1, el);
        // System.out.println(el);
        // board.moveElephant(8, el);
        // System.out.println(el);

        // System.out.println(el);
        boolean success = board.play();
        if(success)
            System.out.println("everythread successfully finished");

        else 
            System.out.println("everythread was not successfully finished");

    }

}




