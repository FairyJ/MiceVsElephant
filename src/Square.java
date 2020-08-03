package src;
/*
  square knows about position and list of animals
*/
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class Square {
    //square have x and y and also can hold animal inside
    private final int x;
    private final int y;
    private List<Animal> mice = new ArrayList<>();;
    private List<Animal> elephants = new ArrayList<>();
    private boolean hasElephant;
    private boolean hasMouse;
    private int numMic = 0;
    private int numElephant = 0;

    public Square(Point position) {
        this.x = position.x;
        this.y = position.y;
        this.hasElephant = false;
        this.hasMouse = false;
    }
    
    public int getNumMic(){
        return this.numMic;
    }
    public Point getPosition(){
        return new Point(this.x, this.y);
    }

    public Animal getMouse(int i) {
        return this.mice.get(i);
    }
    public boolean addAnimal(Animal animal){
        if (animal.getType() == Animal.aType.ELEPHANT) {
            this.elephants.add(animal);
            this.hasElephant = true;
            this.numElephant++;
        } else if (animal.getType() == Animal.aType.MOUSE) {
            this.mice.add(animal);
            this.hasMouse = true;
            this.numMic++; 
        } else {
            return false;
        }
        
        animal.setSquare(this);
        return true;
    }

    public boolean removeAnimal(Animal animal){
        if (animal.getType() == Animal.aType.ELEPHANT) {
            this.elephants.remove(animal);
            this.numElephant--;
        } else if (animal.getType() == Animal.aType.MOUSE) {
            this.mice.remove(animal);
            this.numMic--; 
        } else {
            return false;
        }
        
        if(this.numElephant == 0){
            this.hasElephant = false;
        }
        if(this.numMic == 0){
            this.hasMouse = false;
        } 
        return true;
    }

    public boolean isEmpty(){
        return !this.hasElephant  && !this.hasMouse;
    }

    public boolean mouseIsHere(){
        return this.hasMouse;
    }
    public boolean elephantIsHere(){
        return this.hasElephant;
    }
    

}
