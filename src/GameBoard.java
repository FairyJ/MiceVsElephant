/*
  @data July/16/2020
  this is my board class, and also my start point
 */
package src;

import java.util.List;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collections;
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

     /*
        Calculates the distance between two point 
    */
    public double distance( Square a, Square b) {
        return Math.sqrt(Math.pow((a.getPosition().x - b.getPosition().x), 2) + Math.pow((a.getPosition().y - b.getPosition().y), 2));
    }

    public synchronized void move(Animal animal, Point newHome) {
        Square curSquare = animal.removeSquare();
        if (curSquare.isEmpty()) {
            this.board[curSquare.getPosition().x][curSquare.getPosition().y] = null;
        }
        
        if (this.board[newHome.x][newHome.y] == null) {
            this.board[newHome.x][newHome.y] = new Square(newHome);
        } 
        this.board[newHome.x][newHome.y].addAnimal(animal);

    }

    // /*
    // This method will randomly position every animal 
    // Will start them all abd wait for them to finish and die
    // */
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
                    }else{  //there is elephant create another random numbers.
                        p = new Point(randomNumber.nextInt(squaresWide), randomNumber.nextInt(squaresWide));
                    }             
                }else{//create new square and put the elephant in side.
                    board[p.x][p.y] = new Square(p);
                    board[p.x][p.y].addAnimal(el);
                    this.elephants.add(el);
                    break;
                }
            }
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
                }else{ //create new square and put the mouse inside.
                    board[p.x][p.y] = new Square(p);
                    board[p.x][p.y].addAnimal(m);
                    this.mice.add(m);
                    break;
                }
            }
            m.start();
        }

        System.out.println(this);
        latch.countDown();
        System.out.println("Started all Animals. Mice size: " + this.mice.size() + " Elephants size: " + this.elephants.size());
        
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
                if (distance(m.getSquare(), animal.getSquare()) <= this.strikingDistance) {
                    animalsAroundMe.add(m);
                }
            }
        } else if (animal.getType() == Animal.aType.MOUSE) {
            for(Animal el : this.elephants){
                if(distance(el.getSquare(), animal.getSquare()) <= this.strikingDistance){
                    animalsAroundMe.add(el);
                }
            }
        }
        return animalsAroundMe;
    }

    /*
    return list of all elephants around this mouse in striking distance
    */
    // public List<Elephant> mouseStrikeZone(Mouse m){
    //     List<Elephant> elephantAroundMe = new LinkedList<>();
    //     for(Elephant el : this.elephants){
    //         if(distance(el.getSquare(), m.getSquare()) <= this.strikingDistance){
    //             elephantAroundMe.add(el);
    //         }
    //     }
    //     return elephantAroundMe;
    // }

    /*

    */
    // public boolean moveElephant(int dir, Elephant el){
    //     int[] position = this.move(dir , el.getX() , el.getY() , el.getSteps() );
    //     //if is out of board boundary
    //     if(position[0] < 0 || position[0] > squaresWide-1 || position[1] < 0 || position[1] > squaresTall-1){
    //         return false;
    //     }
    //     //check if there is square and elephant is there can not move into this square
    //     if(this.board[position[0]][position[1]] != null && this.board[el.getX()][el.getY()].elephantIsHere()){
    //         return false; 
    //     }
    //     //move
    //     this.board[el.getX()][el.getY()].removeElephant(el);
    //     el.setX(position[0]); 
    //     el.setY(position[1]);
    //     if (this.board[position[0]][position[1]] != null) {
    //         this.board[position[0]][position[1]].addElephant(el);
    //     } else {
    //         this.board[position[0]][position[1]] = new Square(position[0], position[1]);
    //         this.board[position[0]][position[1]].addElephant(el);
    //     }
    //     return true;
    // }

    // public boolean moveMouse(int dir, Mouse m){
    //     int[] position = this.move(dir , m.getX() , m.getY() , m.getSteps() );
    //     if(position[0] < 0 || position[0] > squaresWide-1 || position[1] < 0 || position[1] > squaresTall-1){
    //         return false;
    //     } 
    //     this.board[m.getX()][m.getY()].removeMouse(m);
    //     m.setX(position[0]); 
    //     m.setY(position[1]);
    //     if (this.board[m.getX()][m.getY()] != null) {
    //         this.board[position[0]][position[1]].addMouse(m);
    //     } else {
    //         this.board[position[0]][position[1]] = new Square(position[0], position[1]);
    //         this.board[position[0]][position[1]].addMouse(m);
    //     } 
    //     return true;
    // }
   


    /*
    if move to these mice around itself will get farther or not
    */
    // public boolean elephantGotFurther(int dir, Elephant el, List<Mouse> mice) {
    //     int[] newPosition = this.move(dir, el.getX(), el.getY(), el.getSteps());
    //     for(Mouse m :this.mice){
    //         double currentDistance = this.distance(el.getSquare(), m.getSquare());
    //         double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), m.getSquare());
    //         if(newDistance < currentDistance){
    //             return false;
    //         }
    //     }
    //     return true; 
    // }

    /*
    if move toward this elephant will get closer or not
    */
    // public boolean miceGotCloser(int dir, Mouse m, Elephant el) {
    //     double currentDistance = this.distance(m.getSquare(), el.getSquare());
    //     int[] newPosition = this.move(dir, m.getX(), m.getY(), m.getSteps());
    //     double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), el.getSquare());

    //     return (newDistance < currentDistance);
    // }

    /*
    check all elephants striking distance around this mouse to see is it alone or not
    */
    // public List<Elephant> AmIAlone(Mouse m, List<Elephant> elephant){
    //     List<Elephant> elephantAroundMeWithMice = new LinkedList<>();
    //     for(Elephant el : elephant){
    //         if (this.elephantStrikeZone(el).size() > 1){
    //             elephantAroundMeWithMice.add(el);
    //         }
    //     }
    //     return elephantAroundMeWithMice;
    // }

    // public List<Elephant> mouseBackup(List<Elephant> elList){
    //     List<Elephant> goCloserTo = new ArrayList<>();
    //     for(Elephant el : this.elephants){
    //         List<Mouse> mice = this.elephantStrikeZone(el);
    //         for(Mouse m : mice){
    //             double currentDistanceElMice = this.distance(m.getSquare(), el.getSquare());
    //             for(int i = 0 ; i < 8 ; i++){
    //                 int[] newPosition = this.move(i, m.getX(), m.getY(), m.getSteps());
    //                 double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), el.getSquare());
    //                 if(newDistance < currentDistanceElMice){
    //                     goCloserTo.add(el);
    //                 }

    //             }

    //         }

    //     }
    //     return goCloserTo;
    // }


    // public List<Integer> closestElephantToMe(Mouse m , List<Elephant> elephants){
    //     List<Integer> distanceToElephant = new ArrayList<>();

        
    //     for(Elephant el : this.elephants){
    //         double currentDistance = this.distance(m.getSquare(), el.getSquare());
    //         for(int i = 0 ; i < 8 ; i++){
    //             int[] newPosition = this.move(i, m.getX(), m.getY(), m.getSteps());
    //             double newDistance = this.distance(new Square(newPosition[0], newPosition[1]), el.getSquare());
    //             if(newDistance < currentDistance ){
    //                 distanceToElephant.add(i);
    //             }
    //         }    
    //     }
    //     return distanceToElephant;
    // }

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

        // CountDownLatch latch = new CountDownLatch(1);

        GameBoard board = new GameBoard(10, 10, 2, 10, 10);
        // System.out.println(board);
        System.out.println("=====> Start creating Animal Threads <====");
        board.play();
        System.out.println("<==== Finished creating Animal Threads ====>");

        // latch.countDown();


        // System.out.println(board);
        // for(Elephant el : board.elephants){
        //     System.out.println(el.toString());
        // }
        // for(Mouse m : board.mice){
        //     System.out.println(m.toString());
        // }
        // System.out.println(board);

        // for(Elephant el : board.elephants){
        //     el.move();
        //     System.out.println(el.toString());
        // }
        // for(Mouse m : board.mice){
        //     m.move();
        //     System.out.println(m.toString());
        // }
        // System.out.println(board);
        // for(Elephant el : board.elephants){
        //     el.move();
        //     System.out.println(el.toString());
        // }
        // for(Mouse m : board.mice){
        //     m.move();
        //     System.out.println(m.toString());
        // }
        // System.out.println(board);
        // for(Elephant el : board.elephants){
        //     el.move();
        //     System.out.println(el.toString());
        // }
        // for(Mouse m : board.mice){
        //     m.move();
        //     System.out.println(m.toString());
        // }
        // System.out.println(board);

        
        //for (i = 0; i < 5; i++) {
        //    System.out.println(board.elephants.get(i%board.numElephant).move());
        //}
        // board.moveElephant(3, el);
        // System.out.println(el);
        
    }


}




