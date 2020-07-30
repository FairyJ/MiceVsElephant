/*
  @data July/16/2020
  this is my board class, and also my start point
 */
package src;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;



public class GameBoard {
    //[left up, up, right up, right, down right, down, left down, left]
    //(0,0)UL,0      (1,0)up,1      (2,0)UR,2         
    //(0,1)left,7    (1,1)          (2,1)right,3
    //(0,2)DL,6      (1,2)down,5    (2,2)DR,4 
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
    private int strikingDistance;
    private Square[][] board;
    private Random randomNumber; 
    private List<Mouse> mice;
    private List<Elephant> elephants;
    

    public GameBoard(int squaresWide, int squaresTall, int strikingDistance,  int numElephant, int numMice) {
        this.squaresWide = squaresWide;
        this.squaresTall = squaresTall;
        this.strikingDistance = strikingDistance;
        this.numMice = numMice;
        this.numElephant = numElephant;
        this.board = new Square[this.squaresWide][this.squaresTall];
        this.randomNumber = new Random(); 
        this.mice = new LinkedList<>();
        this.elephants = new LinkedList<>();
    }
    /*
    to start the game I need to make all my elephants and mice first 
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
                /*
                if in this position square exist check if it is empty or mice is there put elephant inside this square
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

    /*

    */
    public boolean moveElephant(int dir, Elephant el){
        int[] position = this.move(dir , el.getX() , el.getY() , el.getSteps() );
        //if is out of board boundary
        if(position[0] < 0 || position[0] > squaresWide-1 || position[1] < 0 || position[1] > squaresTall-1){
            return false;
        }
        //check if there is square and elephant is there can not move into this square
        if(this.board[position[0]][position[1]] != null && this.board[el.getX()][el.getY()].elephantIsHere()){
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

    public boolean moveMouse(int dir, Mouse m){
        int[] position = this.move(dir , m.getX() , m.getY() , m.getSteps() );
        if(position[0] < 0 || position[0] > squaresWide-1 || position[1] < 0 || position[1] > squaresTall-1){
            return false;
        } 
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
   

    /*
    calculate distance between two animals
    */
    public double distance( Square a, Square b) {
        return Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2));
    }

    /*
    return list of all mouse around this elephant in striking distance
    */
    public List<Mouse> elephantStrikeZone(Elephant el){
        List<Mouse> miceAroundMe = new LinkedList<>();
        for (Mouse m: this.mice) {
            if (distance(m.getSquare(), el.getSquare()) <= this.strikingDistance) {
                miceAroundMe.add(m);
            }
        }
        return miceAroundMe;
    }

    /*
    return list of all elephants around this mouse in striking distance
    */
    public List<Elephant> mouseStrikeZone(Mouse m){
        List<Elephant> elephantAroundMe = new LinkedList<>();
        for(Elephant el : this.elephants){
            if(distance(el.getSquare(), m.getSquare()) <= this.strikingDistance){
                elephantAroundMe.add(el);
            }
        }
        return elephantAroundMe;
    }

    /*
    if move to these mice around itself will get farther or not
    */
    public boolean elephantGotFurther(int dir, Elephant el, List<Mouse> mice) {
        int[] newPosition = this.move(dir, el.getX(), el.getY(), el.getSteps());
        for(Mouse m :this.mice){
            double currentDistance = this.distance(el.getSquare(), m.getSquare());
            double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), m.getSquare());
            if(newDistance < currentDistance){
                return false;
            }
        }
        return true; 
    }

    /*
    if move toward this elephant will get closer or not
    */
    public boolean miceGotCloser(int dir, Mouse m, Elephant el) {
        double currentDistance = this.distance(m.getSquare(), el.getSquare());
        int[] newPosition = this.move(dir, m.getX(), m.getY(), m.getSteps());
        double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), el.getSquare());

        return (newDistance < currentDistance);
    }

    /*
    check all elephants striking distance around this mouse to see is it alone or not
    */
    public List<Elephant> AmIAlone(Mouse m, List<Elephant> elephant){
        List<Elephant> elephantAroundMeWithMice = new LinkedList<>();
        for(Elephant el : elephant){
            if (this.elephantStrikeZone(el).size() > 1){
                elephantAroundMeWithMice.add(el);
            }
        }
        return elephantAroundMeWithMice;
    }

    public List<Elephant> mouseBackup(List<Elephant> elList){
        List<Elephant> goCloserTo = new ArrayList<>();
        for(Elephant el : this.elephants){
            List<Mouse> mice = this.elephantStrikeZone(el);
            for(Mouse m : mice){
                double currentDistanceElMice = this.distance(m.getSquare(), el.getSquare());
                for(int i = 0 ; i < 8 ; i++){
                    int[] newPosition = this.move(i, m.getX(), m.getY(), m.getSteps());
                    double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), el.getSquare());
                    if(newDistance < currentDistanceElMice){
                        goCloserTo.add(el);
                    }

                }

            }

        }
        return goCloserTo;
    }


    public List<Integer> closestElephantToMe(Mouse m , List<Elephant> elephants){
        List<Integer> distanceToElephant = new ArrayList<>();

        
        for(Elephant el : this.elephants){
            double currentDistance = this.distance(m.getSquare(), el.getSquare());
            for(int i = 0 ; i < 8 ; i++){
                int[] newPosition = this.move(i, m.getX(), m.getY(), m.getSteps());
                double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), el.getSquare());
                if(newDistance < currentDistance ){
                    distanceToElephant.add(i);
                }
            }    
        }
        return distanceToElephant;
    }

    public void snort (Mouse m ){
        //random direction 
        List<Integer> directions = new ArrayList<Integer>(8);
        for (int i = 0; i < 8; i++) {
            directions.add(i);
        }
        Collections.shuffle(directions);
        for (int dir = 0; dir < 8; dir++) {
        int [] newSquare = this.move(dir, m.getX(), m.getY(), 2*strikingDistance);


        }  
    }

    public List<Elephant> killElephant(Elephant el){

        return elephants;
    }
    public List<Mouse> killMouse(Mouse m){

        return mice;
    }

    // * One elephant and multiple mice
    // V one elephant and one mouse
    // S multiple mice no elephant
    // M one mouse no elephant
    // E one elephant no mouse
    public String toString() {
        String result = "";
        for (int i = 0; i < this.squaresTall; i++ ) {
            int j;
            for ( j = 0; j < this.squaresWide; j ++) {
                result += "+---";
            }
            result += "+\n";
            for (j = 0; j < this.squaresWide; j ++) {
                if (board[j][i] == null || board[j][i].isEmpty()) {
                    result += "|   ";
                }else if (board[j][i].mouseIsHere() && board[j][i].elephantIsHere()) {
                    if (board[j][i].getNumMic() > 1) {
                        result += "| * ";
                    } else {
                        result += "| V ";
                    }
                } else if (board[j][i].mouseIsHere()) {
                    if (board[j][i].getNumMic() > 1) {
                        result += "| S ";
                    } else {
                        result += "| M ";
                    }
                } else {
                    result += "| E ";
                }       
            }
            result += "|\n";
        }
        for (int j = 0; j < this.squaresWide; j ++) {
            result += "+---";
        }    
        result += "+\n";
        return result;
    }
    

    public static void main(String[] args) {
        //ask user
        // Scanner input = new Scanner(System.in);
        // System.out.println("Enter dimensions of the grid first wide then tall ,striking distance, ");

        GameBoard board = new GameBoard(6, 6, 1, 2, 5);
        board.play();
        System.out.println(board);
        System.out.println("move animals");

        for(Elephant el : board.elephants){
            el.move();
            System.out.println(board);
            //System.out.println(el.toString() + "\n");
        }
        for(Mouse m : board.mice){
            m.move();
            System.out.println(board);
            //System.out.println(m.toString() + "\n");
           
        }
        

        
        //for (i = 0; i < 5; i++) {
        //    System.out.println(board.elephants.get(i%board.numElephant).move());
        //}
        // board.moveElephant(3, el);
        // System.out.println(el);
        
    }


}




