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
    
    //constructor
    public Square(int x, int y) {
        this.x = x;
        this.y = y;
        this.elephants = new ArrayList<>();
        this.mice = new ArrayList<>();;
        this.hasElephant = false;
        this.hasMouse = false;
    }
    
    public boolean addElephant(Elephant el){
        this.elephants.add(el);
        this.hasElephant = true;
        return true;
    }

    public void removeElephant(Elephant el){
        this.elephants.remove(el);
        if(this.elephants.isEmpty())
            this.hasElephant = false;
    }

    public boolean addMouse(Mouse m){
        this.mice.add(m);
        this.hasMouse = true;
        return true;
    }

    public void removeMouse(Mouse m){
        this.mice.remove(m);
        if(this.mice.isEmpty())
            this.hasMouse = false;
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
