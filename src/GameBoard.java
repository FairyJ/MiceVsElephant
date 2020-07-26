/*@author Farzaneh Jahani
  @data July/16/2020
  this is my board class, and also my start point
 */
package src;

import java.util.List;
import java.util.Random;
import java.lang.Math; 
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
    private int numElephant;
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
        this.numElephant = numElephant;
        this.board = new Square[this.squaresWide][this.squaresTall];
        this.randomNumber = new Random(); 
        this.mice = new LinkedList<>();
        this.elephants = new LinkedList<>();


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
        while(elephantCounter != this.numElephant){ 
            randomNumberOfX = this.randomNumber.nextInt(squaresWide) ;
            randomNumberOfY = this.randomNumber.nextInt(squaresTall) ;
            while(true){        
                //if in this position square exist check if it is empty or if elephant is there
                if(this.board[randomNumberOfX][randomNumberOfY] != null){
                    if(this.board[randomNumberOfX][randomNumberOfY].isEmpty()){
                        Elephant el = new Elephant(randomNumberOfX, randomNumberOfY, "Elephant " + elephantIndex  , this);
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
                    Elephant el = new Elephant(randomNumberOfX, randomNumberOfY, "Elephant " + elephantIndex, this);
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
                        Mouse m = new Mouse(randomNumberOfX, randomNumberOfY, "Mouse " + mouseIndex , this);
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
                    Mouse m = new Mouse(randomNumberOfX, randomNumberOfY, "Mouse " + mouseIndex , this);
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
        int[] position = this.move(dir , m.getX() , m.getY() , m.getSteps() );
        if(position[0] < 0 || position[0] > squaresWide-1 || position[1] < 0 || position[1] > squaresTall-1){
            return false;
        }

        // move
        this.board[m.getX()][m.getY()].removeMouse(m);
        m.setX(position[0]); 
        m.setY(position[1]);
        if (this.board[m.getX()][m.getY()] != null) {
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
        



        //move
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
            case UP:
                result [1] = y - steps; 
                break;
            case RIGHT:
                result[0] = x + steps;
                break;
            case DOWN:
                result[1] = y + steps; 
                break;
            case LEFT:
                result[0] = x - steps;
                break; 
            case RU:
                result[0] = x + steps;
                result[1] = y - steps; 
                break; 
            case RD:
                result[0] = x + steps; 
                result[1] = y + steps;
                break;
            case LD:
                result[0] = x - steps;
                result[1] = y + steps;  
                break; 
            case LU:
                result[0] = x - steps;
                result[1] = y - steps; 
                break;  
            
        }
        return result;
    }

    public double distance( Square a, Square b) {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }


    public List<Mouse> elephantStrikeZone(Elephant el){
        List<Mouse> miceAroundMe = new LinkedList<>();
        for (Mouse m: this.mice) {
            if (distance(m.getSquare(), el.getSquare()) <= this.strinkingDistance) {
                miceAroundMe.add(m);
            }
        }
        return miceAroundMe;
    }


    public boolean mouseStrikeZone(Mouse m){
        
    
        return true;
    }

    
    public boolean elephantGotFurther(int dir, Elephant el, List<Mouse> mice) {
        return true;
    }


    public boolean miceGotCloser(int dir, Mouse m, Elephant el) {
        double currentDistance = this.distance(m.getSquare(), el.getSquare());
        int[] newPosition = this.move(dir, m.getX(), m.getY(), m.getSteps());
        double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), el.getSquare());

        return (newDistance < currentDistance);
    }

    public static void main(String[] args) {
        GameBoard board = new GameBoard(2, 2, 1, 10, 3);
        board.play();
        int i ;
        for(i = 0 ; i < board.numElephant ; i++){
            
            System.out.println(board.elephants.get(i));
        }
        for(i = 0 ; i < board.numMice ; i ++){
            System.out.println(board.mice.get(i));
        }
        board.elephants.get(0).move();

        //for (i = 0; i < 5; i++) {
        //    System.out.println(board.elephants.get(i%board.numElephant).move());
        //}
        // board.moveElephant(3, el);
        // System.out.println(el);
        
     }


}




