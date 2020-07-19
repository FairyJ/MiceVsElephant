/*@author Farzaneh Jahani
  @data July/16/2020
  this is my board class
 */
package micVsElephent;

public class GameBoard {
    //declare 2D array of my animal class
    private int squaresWide;
    private int squaresTall;
    private Square[][] board;
    private Animal animal;


    public GameBoard(int squaresWide, int squaresTall, Animal animal) {
        this.squaresWide = squaresWide;
        this.squaresTall = squaresTall;
        board = new Square[squaresWide][squaresTall];
        this.animal = animal;
    }

    public void setSquaresWide(int squaresWide) {
        this.squaresWide = squaresWide;
    }

    public void setSquaresTall(int squaresTall) {
        this.squaresWide = squaresWide;
    }

    public void setBoard(Square[][] board) {
        this.board = board;
    }

    public void setAnimal(Animal animal) {
        Animal mic = new Animal();
        Animal elephant = new Animal();
        mic.NumberOfAnimals = 10;
        elephant.NumberOfAnimals = 5;
    }

    public boolean isWall(){
        //if(board || board || board || board){
//            return ture;
//        }

            return false;
    }





}




