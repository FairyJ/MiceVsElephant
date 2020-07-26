package src;

import java.util.ArrayList;
import java.util.List;

public class Square {
    //my square have x and y and also can hold animal inside
    //square position
    private final int x;
    private final int y;
    private List<Mouse> mice;
    private List<Elephant> elephants;
    private boolean hasElephant;
    private boolean hasMouse;
    private int numMic = 0;
    private int numElephant = 0;
    private final int limitNumElphant = 1;//can only hold one elephant 
    

    //constructor
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        this.elephants = new ArrayList<>();
        this.mice = new ArrayList<>();;
        this.hasElephant = false;
        this.hasMouse = false;
    }
    
    public int getNumMic(){
        return this.numMic;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public boolean addElephant(Elephant el){
        this.elephants.add(el);
        el.setSquare(this);
        this.hasElephant = true;
        this.numElephant++;
        return true;
    }

    public boolean addMouse(Mouse m){
        this.mice.add(m);
        m.setSquare(this);
        this.hasMouse = true;
        this.numElephant++; 
        return true;
    }
    public void removeElephant(Elephant el){
        this.elephants.remove(el);
        this.numElephant--;
        if(numElephant==0){
            this.hasElephant = false;
        }
    }
    public void removeMouse(Mouse m){
        this.mice.remove(m);
        numMic--;
        if(this.numMic == 0){
            this.hasMouse = false;
        }      
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
