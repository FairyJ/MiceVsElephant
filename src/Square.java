package src;

import java.util.ArrayList;


public class Square {

    //my square have x and y and also can hold animal inside
    private int x;
    private int y;
    private Animal animals;
    private GameBoard board;

    //constructor
    public Square(int x, int y, Animal animals, GameBoard board) {
        this.x = x;
        this.y = y;
        this.animals = animals;
        this.board = board;
    }
    public boolean isEmpty(){

        return true;
    }
    public boolean mouseIsHere(){

        return true;
    }
    public boolean elephantIsHere(){

        return true;
    }
    public Animal getAnimals() {
        return this.animals;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    


}
