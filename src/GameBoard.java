/*@author Farzaneh Jahani
  @data July/16/2020
  this is my board class
 */
package src;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;


public class GameBoard {
    //declare 2D array of my animal class
    //board dimantions
    private int squaresWide;
    private int squaresTall;
    private int numMice;
    private int numElpahnet;
    private int strinkingDistance;
    private Square[][] board;



    public GameBoard(int squaresWide, int squaresTall, int strinkingDistance, int numMice, int numElpahnet) {
        this.squaresWide = squaresWide;
        this.squaresTall = squaresTall;
        this.strinkingDistance = strinkingDistance;
        this.numMice = numMice;
        this.numElpahnet = numElpahnet;
        this.board = new Square[this.squaresWide][this.squaresTall];
    }
    
    public boolean play(){

        return true;

    }

    //public void setAnimal(Animal animal) {
        // Animal mic = new Animal();
        // Animal elephant = new Animal();
        // mic.NumberOfAnimals = 10;
        // elephant.NumberOfAnimals = 5;
    //}

    //public boolean isWall(){
        //if(board || board || board || board){
         //   return ture;
        //}

            //return false;
    //}

    public static void main(String[] args) {
        GameBoard board = new GameBoard(100, 100, 6, 10, 5);
        boolean success = board.play();
        if(success)
            System.out.println("everythread successfully finished");

        else 
            System.out.println("everythread was not successfully finished");

        
    }
}




