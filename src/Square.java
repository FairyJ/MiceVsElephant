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
    
    public boolean addElephant(Elephant el){
        this.elephants.add(el);
        this.hasElephant = true;
        System.out.println("added elephant to this square with position (" + this.x + ", " + this.y + ")");
        numElephant++;
        return true;
    }

    public void removeElephant(Elephant el){
        this.elephants.remove(el);
        if(this.elephants.isEmpty())
            this.hasElephant = false;
        numElephant--;
    }

    public boolean addMouse(Mouse m){
        this.mice.add(m);
        this.hasMouse = true;
        System.out.println("added mouse to this square with position (" + this.x + ", " + this.y + ")");
        numMic++; 
        return true;
    }

    public void removeMouse(Mouse m){
        this.mice.remove(m);
        if(this.mice.isEmpty())
            this.hasMouse = false;
            numMic--;
    }

    public boolean isEmpty(){
        System.out.println("suare is empty");
        return !this.hasElephant  && !this.hasMouse;
    }

    public boolean mouseIsHere(){
        //System.out.println("mouse is here in (" + this.x + ", " + this.y + ")");
        return this.hasMouse;
    }
    public boolean elephantIsHere(){
       // System.out.println("elephant is here(" + this.x + ", " + this.y + ")");
        return this.hasElephant;
    }
    

}
