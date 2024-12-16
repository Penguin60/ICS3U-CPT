import java.util.Random;
import java.util.Scanner;

public class Main {
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String RESET = "\u001B[0m";
    static boolean gameRunning = true;
    static boolean shouldContinue = true;
    static char[][] gameBoard = new char[7][6];

    // ---------------------------------
    // | ● | ● | ● | ● | ● | ● | ● | ● |
    // ---------------------------------
    // | ● | ● | ● | ● | ● | ● | ● | ● |
    // ---------------------------------
    // | ● | ● | ● | ● | ● | ● | ● | ● |
    // ---------------------------------
    // | ● | ● | ● | ● | ● | ● | ● | ● |
    // ---------------------------------
    // | ● | ● | ● | ● | ● | ● | ● | ● |
    // ---------------------------------

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] boardTemplate = new String[11];
        boardTemplate[0] = (BLUE + "---------------------------------");
        boardTemplate[1] = (BLUE + "| * | * | * | * | * | * | * | * |");
        boardTemplate[2] = (BLUE + "---------------------------------");
        boardTemplate[3] = (BLUE + "| * | * | * | * | * | * | * | * |");
        boardTemplate[4] = (BLUE + "---------------------------------");
        boardTemplate[5] = (BLUE + "| * | * | * | * | * | * | * | * |");
        boardTemplate[6] = (BLUE + "---------------------------------");
        boardTemplate[7] = (BLUE + "| * | * | * | * | * | * | * | * |");
        boardTemplate[8] = (BLUE + "---------------------------------");
        boardTemplate[9] = (BLUE + "| * | * | * | * | * | * | * | * |");
        boardTemplate[10] = (BLUE + "---------------------------------");
        Random random = new Random();
        for (String s : boardTemplate) {
            String row = s;
            for (int j = 0; j < row.length(); j++) {
                if (row.charAt(j) != '*') {
                    continue;
                }
                row = row.replaceFirst("[*]", getTile(random.nextBoolean() ? 'Y' : 'R'));
            }
            System.out.println(row);
        }
        System.out.print(GREEN + "Press enter to continue..." + RESET);
        scanner.nextLine();

        // Initializes the gameboard with empty spaces so that program to check to see what spots are taken
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                gameBoard[i][j] = ' ';
            }
        }

        while (shouldContinue) {
            while (gameRunning) {
                // TODO: Print Gameboard
                int column = 0;
                while (true) {
                    System.out.println("Yellow, on which column do you want to place your token? (1-7)");
                    int tempColumn = scanner.nextInt();
                    if (isValidInput(tempColumn)) {
                        column = tempColumn;
                        break;
                    }
                    System.out.println("Yellow, that is not a valid input.");
                }

                // Adds a value to the gameboard at a certain column
                modifyGameboard(column - 1, 'Y');

                while (true) {
                    System.out.println("Red, on which column do you want to place your token? (1-7)");
                    int tempColumn = scanner.nextInt();
                    if (isValidInput(tempColumn)) {
                        column = tempColumn;
                        break;
                    }
                    System.out.println("Red, that is not a valid input.");
                }

                // Repeats the process for Red
                modifyGameboard(column - 1, 'R');
            }
        }
    }

    public static String getTile(char player) {
        switch (player) {
            case 'Y':
                return YELLOW + "●" + BLUE;
            case 'R':
                return RED + "●" + BLUE;
            default:
                return " ";
        }
    }

    public static boolean isValidInput(int column) {
        /*
        * Checks if the column that the user inputs is valid
        * Checks if there is still space left in said column
         */

        if (column > 0 && column < 8) {
            if (gameBoard[column - 1][5] == ' ') {
                return true;
            }
        }
        return false;
    }

    public static void modifyGameboard(int column, char player) {
        /*
         * Checks to see which team the player is on
         * Will attempt to place a token at the bottom of a column to simulate how gravity would impact a token in real life
         */
        if (player == 'Y') {
            for (int y = 0; y < 7; y++) {
                if (gameBoard[column][y] == ' ') {
                    gameBoard[column][y] = 'Y';
                    break;
                }
            }
        }
        else if (player == 'R') {
            for (int y = 0; y < 7; y++) {
                if (gameBoard[column][y] == ' ') {
                    gameBoard[column][y] = 'R';
                    break;
                }
            }
        }
    }
}