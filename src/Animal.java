package src;

import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
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
            // First they need to check their type
            if (this.myType == aType.ELEPHANT) {
                // 2 ore more mice on me, time to die
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
                        if (this.randomMove(allDirections, this.numSteps)) {
                            System.out.println("Random Move with success");
                        } else {
                            System.out.println("Unsuccessful Random Move");
                        }
    
                    //  Run Away
                    } else {
                        System.out.println("I need to move smartly!");
                    }
                }

                System.out.println(this);
                // System.out.println(this.board);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            } else if (this.myType == aType.MOUSE) {
                // System.out.println(this);
                break;
            }

        }



        synchronized(this.board) {
            this.dead = true;
            this.board.notify();
        }

        // Before they exit run() method, they have to delete themselves from the GameBoard and Square
        // if (this.myType == aType.ELEPHANT) {
        //     synchronized(this.board) {
        //         this.board.killMe(this);
        //     }
        //}


        // synchronized (board) {
        //     System.out.println( "I am a " + aType.ELEPHANT.name() + " my id is " + this.getId());
        // } 
    }

    private boolean randomMove(List<Integer> directions, int steps) {
        Point p;
        for (int i = 0; i < directions.size(); i++) {
            p = this.board.newLocation(directions.get(i), this.getSquare().getPosition(), steps);
            if (p.x < 0 || p.x > this.board.getWidth() - 1 || p.y < 0 || p.y > this.board.getHeight() - 1) {
                continue;
            } else {
                Square destSquare = this.board.getSquare(p);
                if (destSquare == null || !destSquare.elephantIsHere()) {
                    this.board.move(this, p);
                    return true;
                } 
            }
        }
        return false;
    }
    
    private void snort (int dir, Animal m ){
        //random direction 
        Point p = this.board.newLocation(dir, m.getSquare().getPosition(), 2 * this.board.getStrikingDistance());

        p.x = (p.x < 0) ? 0 : (p.x > this.board.getWidth() - 1) ? this.board.getWidth() - 1 : p.x;
        p.y = (p.y < 0) ? 0 : (p.y > this.board.getHeight() - 1) ? this.board.getHeight() - 1 : p.y;

        this.board.move(m, p);

    }

    /*
        Calculates the distance between me and given square in the board
    */
    public double distance( Square b) {
        return Math.sqrt(Math.pow((this.mySquare.getPosition().x - b.getPosition().x), 2) + Math.pow((this.mySquare.getPosition().y - b.getPosition().y), 2));
    }
    

    public String toString() {
        String result = "Turn " + this.turnNum + ": ";
        result +=  (this.myType == aType.ELEPHANT) ? "Elephant " : "Mouse ";
        result +=   this.myId + " to " + this.mySquare.getPosition().x + " " + this.mySquare.getPosition().y;
        return result;
    }

    
}
