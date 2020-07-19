package micVsElephent;

import java.util.ArrayList;


public class Square {

    //my square have x and y and also can hold animal inside
    private int x;
    private int y;
    private Animal animals;

    //constructor
    public Square(int x, int y, Animal animals) {
        this.x = x;
        this.y = y;
        this.animals = animals;
    }

    public void setAnimals(Animal animals) {
        this.animals = null;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }


}
