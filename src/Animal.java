package src;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

public class Animal extends Thread {

    static enum aType {
        ELEPHANT,
        MOUSE
    }

    private static int elCounts = 1;
    private static int mCounts = 1;
    private final List<Integer> allDirections = new ArrayList<Integer>(8);
    private final List<Integer> diagonalDirections = new ArrayList<Integer>(4);

    private CountDownLatch latch;

    
    private int myId;
    private aType myType;
    private Square mySquare;
    private int turnNum;
    private int numSteps;
    private boolean dead = true;

    // There is one board shared between all Animals (Elephants and Mice)
    private final GameBoard board;
    
    // public Animal (Point position, GameBoard board, aType type, CountDownLatch latch) {
    public Animal (GameBoard board, aType type, CountDownLatch latch) {
        this.latch = latch;
        this.myType = type;
        this.dead = false;
        this.board = board;
        this.turnNum = 0;
        if (type == aType.ELEPHANT) {
            this.numSteps = 1;
            this.myId = elCounts++;
        } else if (type == aType.MOUSE) {
            this.numSteps = 2;
            this.myId = mCounts++;
        }
        for (int i = 0; i < 4; i++) {
            this.diagonalDirections.add(i * 2);
        }
        for (int i = 0; i < 8; i++) {
            this.allDirections.add(i);
        }
    }

    public void setSquare(Square square) {
        this.mySquare = square;
    }

    public Square removeSquare() {
        Square mySquare = this.mySquare;
        this.mySquare.removeAnimal(this);
        this.mySquare = null;
        return mySquare;
    }

    public Square getSquare() {
        return this.mySquare;
    }

    public int getSteps() {
        return this.numSteps;
    }

    public aType getType() {
        return this.myType;
    }

    public int getMyID() {
        return this.myId;
    }

    public boolean isDead() {
        return this.dead;
    }

    @Override
    public void run() {
        // All Threads are waiting until Latch is released
        try {
            this.latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (true) {
            this.turnNum++;
            // First they need to check their type
            if (this.myType == aType.ELEPHANT) {
                // 2 or more mice on me, time to die
                if (this.mySquare.getNumMic() > 1) {
                    System.out.println("Elephant " + this.myId + " thread id: " + this.getId() + " is dead");
                    break;
                // only 1 mouse on me, have to snort and eject 
                } else if (this.mySquare.getNumMic() == 1) {
                    Collections.shuffle(allDirections);
                    this.snort(allDirections.get(0), this.mySquare.getMouse(0));
                } 
                // Now time to make a move
                synchronized (this.board) {
                    List<Animal> mice = this.board.strikeZone(this);
                    // Random Move
                    if (mice.isEmpty()) {
                        Collections.shuffle(allDirections);
                        if (this.randomMove(allDirections)) {
                            System.out.println("Elephant " + this.myId + " Random Move with success");
                        } else {
                            System.out.println("Elephant " + this.myId + " Can't move randomly.");
                        }
                    } else {  //  Run Away
                        System.out.println("Elephant " + this.myId + " have to running Away!");
                        if (this.runAway(mice)) {
                            System.out.println("Elephant " + this.myId + " Running away!");
                        } else {
                            System.out.println("Elephant " + this.myId + " Frozen. In trouble!");
                        }
                    }
                    System.out.println(this);
                }

                // System.out.println(this.board);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (this.myType == aType.MOUSE) {
                if (this.board.numElephant() == 0) {
                    System.out.println("Mouse " + this.myId + " Nothing to eat. I am done!");
                    break;
                } else {
                    synchronized(this.board) {
                        List<Animal> elephants = this.board.strikeZone(this);
                        if (elephants.isEmpty()) {
                            Collections.shuffle(diagonalDirections);
                            if (this.randomMove(diagonalDirections)) {
                                System.out.println("Mouse " + this.myId + " Random Move with success.");
                            } else {
                                System.out.println("Mouse " + this.myId + " Can't move randomly.");
                            }
                        } else {
                            elephants = this.amIAlone(elephants);
                            if (!elephants.isEmpty()) {
                                System.out.println("Mouse " + this.myId + " Attack!");
                                if (attack(elephants.get(0))) {
                                    System.out.println("Mouse " + this.myId + " got closer.");
                                } else {
                                    System.out.println("Mouse " + this.myId + " didn't move.");
                                }
                            } else {
                                System.out.println("Mouse " + this.myId + " Frozen. In trouble!");
                            }
                        }
                        System.out.println(this);
                    }
                }
                
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        // Exit 
        synchronized(this.board) {
            this.dead = true;
            this.board.notify();
        }
    }
    /*
      this function givin random position to animal and check if is not out of boundary  check if in the square is not other elephant 
      will move the animal to new position since we can have more then one mouse in square and not more than one elephant 
    */

    private boolean randomMove(List<Integer> directions) {
        Point p;
        for (int i = 0; i < directions.size(); i++) {
            p = this.board.newLocation(directions.get(i), this.getSquare().getPosition(), this.numSteps);
            if (p.x < 0 || p.x > this.board.getWidth() - 1 || p.y < 0 || p.y > this.board.getHeight() - 1) {
                continue;
            } else {
                Square destSquare = this.board.getSquare(p);
                if (destSquare == null || this.myType == Animal.aType.MOUSE || (this.myType == Animal.aType.ELEPHANT && !destSquare.elephantIsHere())) {
                    this.board.move(this, p);
                    return true;
                } 
            }
        }
        return false;
    }
    /*

    */

    private boolean runAway(List<Animal> mice) {
        if (this.myType == Animal.aType.ELEPHANT) {
            Point p;
            Animal closestMouse = mice.get(0);
            if (mice.size() > 1) {
                this.closest(mice);
            }
            double currDistance = this.distance(closestMouse.getSquare());
            Collections.shuffle(allDirections);
            for (int i = 0; i < allDirections.size(); i++) {
                p = this.board.newLocation(allDirections.get(i), this.getSquare().getPosition(), this.numSteps);
                if (p.x < 0 || p.x > this.board.getWidth() - 1 || p.y < 0 || p.y > this.board.getHeight() - 1) {
                    continue;
                } else {
                    Square destSquare = this.board.getSquare(p);
                    if (destSquare == null || (this.myType == Animal.aType.ELEPHANT && !destSquare.elephantIsHere())) {
                        if (this.distance(new Square(p), closestMouse.getSquare()) > currDistance) {
                            this.board.move(this, p);
                            return true;
                        } 
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /*
    
    */
    private boolean attack(Animal el) {
        if (this.myType == Animal.aType.MOUSE) {
            double currDistance = this.distance(el.getSquare());
            if ((int)currDistance > this.numSteps) {
                Collections.shuffle(allDirections);
                for (int i = 0; i < allDirections.size(); i++) {
                    Point p = this.board.newLocation(allDirections.get(i), this.getSquare().getPosition(), this.numSteps);
                    if (p.x < 0 || p.x > this.board.getWidth() - 1 || p.y < 0 || p.y > this.board.getHeight() - 1) {
                        continue;
                    } else {
                        if (this.distance(new Square(p), el.getSquare()) > currDistance) {
                            this.board.move(this, p);
                            return true;
                        } 
                    }
                }
            } else {
                this.board.move(this, el.getSquare().getPosition());
            }
            return true;
        } else {
            return false;
        }
    }
    
    /*
      mouse find the closest elephant to move toward
    */
    private Animal closest(List<Animal> animals) {
        Animal closest = null;
        double distance = this.board.getStrikingDistance() * 2;
        for (Animal a : animals) {
            double temp = this.distance(a.getSquare());
            if (temp < distance) {
                distance = temp;
                closest = a;
            }
        }
        return closest;
    }

    /*
      when elephant weak up and see one elephant on top of itself will snort the mouse and move to random place
    */
    private void snort (int dir, Animal m ){
        //random direction 
        Point p = this.board.newLocation(dir, m.getSquare().getPosition(), 2 * this.board.getStrikingDistance());

        p.x = (p.x < 0) ? 0 : (p.x > this.board.getWidth() - 1) ? this.board.getWidth() - 1 : p.x;
        p.y = (p.y < 0) ? 0 : (p.y > this.board.getHeight() - 1) ? this.board.getHeight() - 1 : p.y;

        this.board.move(m, p);
    }

    /*
    mouse check the elephants in striking distance and check the mouse around that elephants, to know it has backup to attack or not.
    */
    private List<Animal> amIAlone(List<Animal> elephants) {
        List<Animal> elephantAroundMeWithMice = new LinkedList<>();
        if (this.myType == Animal.aType.MOUSE) {
            for(Animal el : elephants) {
                List<Animal> mice = this.board.strikeZone(el);
                if (mice.size() > 1 && mice.contains(this)) {
                    elephantAroundMeWithMice.add(el);
                }
            }
        }
        return elephantAroundMeWithMice;
    }

    // Calculates the distance between me and given square in the board
    public double distance( Square b) {
        return Math.sqrt(Math.pow((this.mySquare.getPosition().x - b.getPosition().x), 2) + Math.pow((this.mySquare.getPosition().y - b.getPosition().y), 2));
    }

    /*
        Calculates the distance between two point 
    */
    private double distance( Square a, Square b) {
        return Math.sqrt(Math.pow((a.getPosition().x - b.getPosition().x), 2) + Math.pow((a.getPosition().y - b.getPosition().y), 2));
    }
    
    public String toString() {
        String result = "Turn " + this.turnNum + ": ";
        result +=  (this.myType == aType.ELEPHANT) ? "Elephant " : "Mouse ";
        result +=   this.myId + " to " + this.mySquare.getPosition().x + " " + this.mySquare.getPosition().y;
        return result;
    }

    
}
