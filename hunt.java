package src;

public class hunt {

    public static void main(String[] args) {
        if (args.length != 5) {
            System.out.println("Wrong number of inputs.\nPlease Follow the format below:\n\tjava Main.java Width Height StrikingDistance NumElephants NumMice\n");
        } else {

            GameBoard board = new GameBoard(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
            // System.out.println(board);
            System.out.println("=====>        Start        <====");
            board.play();
            System.out.println("<=====         End         ====>");
        }
    }
    
}