/*
  @data July/16/2020
  this is my board class, and also my start point
 */
package src;

import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.Iterator;
import java.util.LinkedList;
import java.awt.Point;
import java.util.concurrent.CountDownLatch;


public class GameBoard {

    private CountDownLatch latch = new CountDownLatch(1);

    /*  Direction Reference
        [Left Up, UP, Right Up, RIGHT, Right Down, DOWN, Left Down, LEFT]

                (0,0)LU,0      (1,0)UP,1      (2,0)RU,2         
                (0,1)LEFT,7    (1,1)          (2,1)RIGHT,3
                (0,2)LD,6      (1,2)DOWN,5    (2,2)RD,4 
    */
    private static final int LU     = 0;
    private static final int UP     = 1;
    private static final int RU     = 2;
    private static final int RIGHT  = 3;
    private static final int RD     = 4;
    private static final int DOWN   = 5;
    private static final int LD     = 6;
    private static final int LEFT   = 7;

    
    // board dimensions
    private int squaresWide;
    private int squaresTall;
    private int numMice;
    private int numElephant;
    private int strikingDistance;
    private Square[][] board;
    private Random randomNumber = new Random(); 
    private List<Animal> mice = new LinkedList<>();
    private List<Animal> elephants = new LinkedList<>();
    

    public GameBoard(int squaresWide, int squaresTall, int strikingDistance,  int numElephant, int numMice) {
        this.squaresWide = squaresWide;
        this.squaresTall = squaresTall;
        this.strikingDistance = strikingDistance;
        this.numMice = numMice;
        this.numElephant = numElephant;
        this.board = new Square[this.squaresWide][this.squaresTall];
    }

    public int getWidth() {
        return this.squaresWide;
    }

    public int getHeight() {
        return this.squaresTall;
    }

    public int getStrikingDistance() {
        return this.strikingDistance;
    }

    public Square getSquare(Point p) {
        return this.board[p.x][p.y];
    }

    // Returns new position based on the given direction and position
    public Point newLocation(int dir, Point curPosition , int steps){
              
        Point result = new Point(curPosition.x, curPosition.y);
        switch (dir) {
            case UP:
                result.y = curPosition.y - steps; 
                break;
            case RIGHT:
                result.x = curPosition.x + steps;
                break;
            case DOWN:
                result.y = curPosition.y + steps; 
                break;
            case LEFT:
                result.x = curPosition.x - steps;
                break; 
            case RU:
                result.x = curPosition.x + steps;
                result.y = curPosition.y - steps; 
                break; 
            case RD:
                result.x = curPosition.x + steps; 
                result.y = curPosition.y + steps;
                break;
            case LD:
                result.x = curPosition.x - steps;
                result.y = curPosition.y + steps;  
                break; 
            case LU:
                result.x = curPosition.x - steps;
                result.y = curPosition.y - steps; 
                break;  
            }

        return result;
    }

    public void move(Animal animal, Point newHome) {
        Square curSquare = animal.removeSquare();
        if (curSquare.isEmpty()) {
            this.board[curSquare.getPosition().x][curSquare.getPosition().y] = null;
        }
        
        if (this.board[newHome.x][newHome.y] == null) {
            this.board[newHome.x][newHome.y] = new Square(newHome);
        } 
        this.board[newHome.x][newHome.y].addAnimal(animal);

    }

     /*
       This method will randomly position every animal 
       Will start them all abd wait for them to finish and die
     */
    public void play() {
        Point p;
        //create all elephants
        for (int i = 0; i < this.numElephant; i++) {
            p = new Point(randomNumber.nextInt(squaresWide), randomNumber.nextInt(squaresWide));
            Animal el = new Animal(this, Animal.aType.ELEPHANT, this.latch);
            while(true){       
                //if in this position square exist check if it is empty or if elephant is there
                if(this.board[p.x][p.y] != null){
                    if(this.board[p.x][p.y].isEmpty()){
                        board[p.x][p.y].addAnimal(el);
                        this.elephants.add(el);
                        break;
                    }else{//there is elephant create another random numbers.
                        p = new Point(randomNumber.nextInt(squaresWide), randomNumber.nextInt(squaresWide));
                    }             
                }else{//create new square and put the elephant in side.
                    board[p.x][p.y] = new Square(p);
                    board[p.x][p.y].addAnimal(el);
                    this.elephants.add(el);
                    break;
                }
            }
            System.out.println(el);
            el.start();
        }

        //create all mice
        for (int i = 0; i < this.numMice; i++) { 
            p = new Point(randomNumber.nextInt(squaresWide), randomNumber.nextInt(squaresWide));
            Animal m = new Animal(this, Animal.aType.MOUSE, latch);
            while(true){        
                if(this.board[p.x][p.y] != null){
                    if(!this.board[p.x][p.y].elephantIsHere()){
                        board[p.x][p.y].addAnimal(m);
                        this.mice.add(m);
                        break;
                    }else{
                        p = new Point(randomNumber.nextInt(squaresWide), randomNumber.nextInt(squaresWide));
                    }             
                }else{//create new square and put the mouse inside.
                    board[p.x][p.y] = new Square(p);
                    board[p.x][p.y].addAnimal(m);
                    this.mice.add(m);
                    break;
                }
            }
            System.out.println(m);
            m.start();
        }

        System.out.println(this);
        System.out.println("Started all Animals. Mice size: " + this.mice.size() + " Elephants size: " + this.elephants.size());
        latch.countDown();
        
        // Waiting for threads to finis
        // remove them from the list and squares and clean up
        synchronized(this) {
            while (!this.elephants.isEmpty() || !this.mice.isEmpty()) {
                boolean aboutToLeave = false;
                // Wait for animals to finish and Notify the board before leave the game
                while (!aboutToLeave) {
                    try {
                        wait();
                        aboutToLeave = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Traverse both Animal lists and remove them from the Game if dead
                // Elephants Fist
                Iterator<Animal> elephantsItr = this.elephants.iterator();
                while (elephantsItr.hasNext()) {
                    Animal el = elephantsItr.next();
                    if (el.isDead()) {
                        boolean joined = false;
                        while (!joined) {
                            try {
                                el.join();
                                System.out.println("Elephant " + el.getMyID() + " with ThreadId: " + el.getId() + " joined");
                                joined = true;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Square curSquare = el.removeSquare();
                        if (curSquare.isEmpty()) {
                            this.board[curSquare.getPosition().x][curSquare.getPosition().y] = null;
                        }
                        elephantsItr.remove();
                    }
                } 

                // Mice
                Iterator<Animal> miceItr = this.mice.iterator();
                while (miceItr.hasNext()) {
                    Animal m = miceItr.next();
                    if (m.isDead()) {
                        boolean joined = false;
                        while (!joined) {
                            try {
                                m.join();
                                System.out.println("Mouse " + m.getMyID() + " with ThreadId: " + m.getId() + " joined");
                                joined = true;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Square curSquare = m.removeSquare();
                        if (curSquare.isEmpty()) {
                            this.board[curSquare.getPosition().x][curSquare.getPosition().y] = null;
                        }
                        miceItr.remove();
                    }
                }          
            }
        }

        System.out.println("Elephants list size: " + this.elephants.size() + " mice list size: " + this.mice.size());

    }
    
    public int numElephant() {
        synchronized (this.elephants) {
            return this.elephants.size();
        }
    }

    /*
    return list of animal around this animal in its striking distance
    */
    public List<Animal> strikeZone(Animal animal){
        List<Animal> animalsAroundMe = new LinkedList<>();
        if (animal.getType() == Animal.aType.ELEPHANT) {
            for (Animal m: this.mice) {
                if (animal.distance(m.getSquare()) <= this.strikingDistance) {
                    animalsAroundMe.add(m);
                }
            }
        } else if (animal.getType() == Animal.aType.MOUSE) {
            for(Animal el : this.elephants){
                if(animal.distance(el.getSquare()) <= this.strikingDistance){
                    animalsAroundMe.add(el);
                }
            }
        }
        return animalsAroundMe;
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
    

    // public static void main(String[] args) {

    //     if (args.length != 5) {
    //         System.out.println("Wrong number of inputs.\nPlease Follow the format below:\n\tjava Main.java Width Height StrikingDistance NumElephants NumMice\n");
    //     } else {

    //         GameBoard board = new GameBoard(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
    //         // System.out.println(board);
    //         System.out.println("=====>        Start        <====");
    //         board.play();
    //         System.out.println("<=====         End         ====>");
    //     }

        
    // }


}




