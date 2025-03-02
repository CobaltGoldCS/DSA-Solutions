import java.io.File;
import java.util.Scanner;

public class Assignment6Driver {
    public static void main(String[] args) {

        //testGame();
        playGame("moves1.txt");
        System.out.println();
        playGame("moves2.txt");
    }

    private static void playGame(String filename) {
        File file = new File(filename);
        try (Scanner input = new Scanner(file)) {
            int row = 0;
            HexGame game = new HexGame(11);
            while (input.hasNextInt()) {
                int position = input.nextInt();

                if (row % 2 == 0 && game.playBlue(position, false)) {
                    System.out.printf("Blue wins with move at position %d!!\n\n", position);
                    break;
                } else if (game.playRed(position, false)) {
                    System.out.printf("Red wins with move at position %d!!\n\n", position);
                    break;
                }

                row++;
            }

            printGrid(game);
        }
        catch (java.io.IOException ex) {
            System.out.println("An error occurred trying to read the moves file: " + ex);
        }
    }

    private static void testGame() {
        HexGame game = new HexGame(11);

        System.out.println("--- red ---");
        game.playRed(1, true);
        game.playRed(11, true);
        game.playRed(122 - 12, true);
        game.playRed(122 - 11, true);
        game.playRed(122 - 10, true);
        game.playRed(121, true);
        game.playRed(61, true);

        System.out.println("--- blue ---");
        game.playBlue(1, true);
        game.playBlue(2, true);
        game.playBlue(11, true);
        game.playBlue(12, true);
        game.playBlue(121, true);
        game.playBlue(122 - 11, true);
        game.playBlue(62, true);

        printGrid(game);
    }

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_RESET = "\u001B[0m";
    private static void printGrid(HexGame game) {

        HexGame.Color[] board = game.getGrid();

        int currentCol = 1;
        int currentRow = 1;

        System.out.print(" ");
        for (HexGame.Color color : board) {
            String colorString = switch (color) {
                case NONE -> ANSI_WHITE + "0" + ANSI_RESET;
                case RED -> ANSI_RED + "R" + ANSI_RESET;
                case BLUE -> ANSI_BLUE + "B" + ANSI_RESET;
            };

            System.out.printf("%s ", colorString);

            if (currentCol == game.GRID_SIZE) {
                currentCol = 0;
                currentRow++;
                System.out.printf("\n%s", " ".repeat(currentRow));
            }

            currentCol++;
        }
    }

}
