/*@author Farzaneh Jahani
  @data July/16/2020
  this is my board class, and also my start point
 */
package src;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.LinkedList;



public class GameBoard {
    // [left up, up, right up, right, down right, down, left down, left]
    //(0,0)          (1,0)up,1      (2,0)         
    //(0,1)left,7    (1,1),0        (2,1)right,3
    //(0,2)          (1,2)down,5    (2,2) 
    public final static int LEFT = 7;
    public final static int RIGHT = 3;
    public final static int UP = 1;
    public final static int DOWN = 5;
    public final static int LU = 0;
    public final static int LD = 6;
    public final static int RU = 2;
    public final static int RD = 4;
    // board dimensions
    private int squaresWide;
    private int squaresTall;
    private int numMice;
    private int numElpahnet;
    private int strinkingDistance;
    private Square[][] board;
    private Random randomNumber; 
    private List<Mouse> mice;
    private List<Elephant> elephants;
    
    
    public GameBoard(int squaresWide, int squaresTall, int strikingDistance, int numMice, int numElephant) {
        this.squaresWide = squaresWide;
        this.squaresTall = squaresTall;
        this.strinkingDistance = strikingDistance;
        this.numMice = numMice;
        this.numElpahnet = numElephant;
        this.board = new Square[this.squaresWide][this.squaresTall];
        this.randomNumber = new Random(); 
        this.mice = new ArrayList<>();
        this.elephants = new ArrayList<>();


    }
    /*to start the game I need to make all my elephants and mice first 
    then start the game
    */
    public boolean play(){
        //generate number of elephants and mice
        int elephantCounter = 0;
        int mouseCounter = 0;
        int randomNumberOfX; 
        int randomNumberOfY; 

        //create all elephants
        int elephantIndex = 1;
        while(elephantCounter != this.numElpahnet){ 
            randomNumberOfX = this.randomNumber.nextInt(squaresWide) ;
            randomNumberOfY = this.randomNumber.nextInt(squaresTall) ;
            while(true){        
                //if in this position square exist check if it is empty or if elephant is there
                if(this.board[randomNumberOfX][randomNumberOfY] != null){
                    if(this.board[randomNumberOfX][randomNumberOfY].isEmpty()){
                        Elephant el = new Elephant(randomNumberOfX, randomNumberOfY, elephantIndex  , this);
                        board[randomNumberOfX][randomNumberOfY].addElephant(el);
                        this.elephants.add(el);
                        elephantIndex++;
                        break;
                    }else{//there is elephant create another random numbers.
                            randomNumberOfX = this.randomNumber.nextInt(squaresWide);
                            randomNumberOfY = this.randomNumber.nextInt(squaresTall); 
                    }             
                }else{//create new square and put the elephant in side.
                    board[randomNumberOfX][randomNumberOfY] = new Square(randomNumberOfX, randomNumberOfY);
                    Elephant el = new Elephant(randomNumberOfX, randomNumberOfY, elephantIndex, this);
                    board[randomNumberOfX][randomNumberOfY].addElephant(el);
                    this.elephants.add(el);
                    elephantIndex++;
                    break;
                }
            }
        elephantCounter++;
        }
        //create all mice
        int mouseIndex = 1;
        while(mouseCounter != this.numMice){ 
            randomNumberOfX = this.randomNumber.nextInt(squaresWide) ;
            randomNumberOfY = this.randomNumber.nextInt(squaresTall) ;
            while(true){        
                /*if in this position square exist check if it is empty or mice is there put elephant inside this square
                if elephant is there we generate another random number and check the square in that new position
                */
                if(this.board[randomNumberOfX][randomNumberOfY] != null){
                    if(!this.board[randomNumberOfX][randomNumberOfY].elephantIsHere()){
                        Mouse m = new Mouse(randomNumberOfX, randomNumberOfY, mouseIndex , this);
                        board[randomNumberOfX][randomNumberOfY].addMouse(m);
                        this.mice.add(m);
                        mouseIndex++;
                        break;
                    }else{
                        randomNumberOfX = this.randomNumber.nextInt(squaresWide) ;
                        randomNumberOfY = this.randomNumber.nextInt(squaresTall) ;    
                    }             
                }else{ //create new square and put the mouse inside.
                    board[randomNumberOfX][randomNumberOfY] = new Square(randomNumberOfX, randomNumberOfY);
                    Mouse m = new Mouse(randomNumberOfX, randomNumberOfY, mouseIndex , this);
                    board[randomNumberOfX][randomNumberOfY].addMouse(m);
                    this.mice.add(m);
                    mouseIndex++;
                    break;
                }
            }
        mouseCounter++;
        }
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
        GameBoard board = new GameBoard(2, 2, 1, 10, 3);
        board.play();
        int i ;
        for(i = 0 ; i < board.numElpahnet ; i++){
            
            System.out.println(board.elephants.get(i));
        }
        for(i = 0 ; i < board.numMice ; i ++){
            System.out.println(board.mice.get(i));
        }
    
        // while (el.getX() != el.getY()) {
        //     el.move();
        //     System.out.println(el);
        // }
        // board.moveElephant(3, el);
        // System.out.println(el);

        
     }


}




