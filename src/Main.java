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
    static final String[] boardTemplate = {
        BLUE + "-------------------------------------------",
        BLUE + "|  *  |  *  |  *  |  *  |  *  |  *  |  *  |",
        BLUE + "-------------------------------------------",
        BLUE + "|  *  |  *  |  *  |  *  |  *  |  *  |  *  |",
        BLUE + "-------------------------------------------",
        BLUE + "|  *  |  *  |  *  |  *  |  *  |  *  |  *  |",
        BLUE + "-------------------------------------------",
        BLUE + "|  *  |  *  |  *  |  *  |  *  |  *  |  *  |",
        BLUE + "-------------------------------------------",
        BLUE + "|  *  |  *  |  *  |  *  |  *  |  *  |  *  |",
        BLUE + "-------------------------------------------",
        BLUE + "|  *  |  *  |  *  |  *  |  *  |  *  |  *  |",
        BLUE + "-------------------------------------------",
        BLUE + "|  #  |  #  |  #  |  #  |  #  |  #  |  #  |",
        BLUE + "-------------------------------------------",
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(GREEN + "Press enter to continue..." + RESET);
        scanner.nextLine();

        // Initializes the gameboard with empty spaces so that program to check to see what spots are taken
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                gameBoard[i][j] = ' ';
            }
        }
        // While the users wish to continue
        while (shouldContinue) {
            // While the game is still running
            while (gameRunning) {
                printGameboard();
                // Initializes the column the user wishes to place the token in
                int column;
                while (true) {
                    System.out.println("Yellow, on which column do you want to place your token? (1-7)");
                    // Takes in the input as a temporary value
                    int tempColumn = scanner.nextInt();
                    if (isValidInput(tempColumn)) {
                        // If the temporary value is valid, make it the column
                        column = tempColumn;
                        break; // Exits out of loop
                    }
                    System.out.println("Yellow, that is not a valid input.");
                }

                // Puts the token 'Y' at the bottom of the column that is chosen
                modifyGameboard(column - 1, 'Y');

                printGameboard();
                while (true) {
                    System.out.println("Red, on which column do you want to place your token? (1-7)");
                    // Takes in the input as a temporary value
                    int tempColumn = scanner.nextInt();
                    if (isValidInput(tempColumn)) {
                        // If the temporary value is valid, make it the column
                        column = tempColumn;
                        break; // Exits out of loop
                    }
                    System.out.println("Red, that is not a valid input.");
                }

                // Puts the token 'R' at the bottom of the column that is chosen
                modifyGameboard(column - 1, 'R');
            }
        }
    }

    public static void printGameboard() {
        int x = 0;
        int y = 5;
        for (int i = 0; i < boardTemplate.length; i++) {
            String row = boardTemplate[i];
            if (i > 12) {
                int displayNum = 1;
                for (int j = 0; j < row.length(); j++) {
                    if (row.charAt(j) != '#') {
                        continue;
                    }
                    row = row.replaceFirst("[#]", RESET + displayNum + BLUE);
                    displayNum++;
                }
                System.out.println(row);
                continue;
            }
            if (i % 2 == 0) {
                System.out.println(row);
                continue;
            }
            for (int j = 0; j < row.length(); j++) {
                if (row.charAt(j) != '*') {
                    continue;
                }
                row = row.replaceFirst("[*]", getTile(gameBoard[x][y]));
                x++;
            }
            System.out.println(row);
            x = 0;
            y--;
        }
        System.out.print(RESET);
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
         * Checks if the column that the user inputs is valid from columns 1 to 7
         * Checks if the top row at said column is empty
         */

        if (column > 0 && column < 8) {
            if (gameBoard[column - 1][5] == ' ') {
                return true;
            }
        }
        return false; // If it does not return true, return false
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

    public static boolean checkGameboard(int row, int column, char player) {
        /*
         * Checks to see if the player has won the game
         * Checks to see if the player has won by checking row, column, left diagonal, and right diagonal
         */
        if (checkRow(row, column, player) || checkColumn(row, column, player) || checkLeftDiagonal(row, column, player) || checkRightDiagonal(row, column, player)) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean checkRow(int row, int column, char player) {
        /*
         * Checks to see if the player has won by checking the row
         * If the player has 4 tokens in a row, the player wins
         */
        int counter = 0;
        for (int i = 0; i < 7; i++) {
            if (gameBoard[i][column] == player) {
                counter++;
            }
            else {
                counter = 0;
            }
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkColumn(int row, int column, char player) {
        /*
         * Checks to see if the player has won by checking the column
         * If the player has 4 tokens in a column, the player wins
         */
        int counter = 0;
        for (int i = 0; i < 6; i++) {
            if (gameBoard[row][i] == player) {
                counter++;
            }
            else {
                counter = 0;
            }
            if (counter == 4) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkLeftDiagonal(int row, int column, char player) {
        /*
         * Checks to see if the player has won by checking the left diagonal
         * If the player has 4 tokens in a diagonal, the player wins
         */
        int counter = 0;
        int currentRow = row;
        int currentColumn = column;
        // locate the top left corner of the diagonal
        while (currentRow < 7 && currentColumn >= 0) {
            currentRow++;
            currentColumn--;
        }

        // check the diagonal
        while (currentRow >= 0 && currentColumn < 6) {
            if (gameBoard[currentRow][currentColumn] == player) {
                counter++;
            }
            else {
                counter = 0;
            }
            if (counter == 4) {
                return true;
            }
            currentRow--;
            currentColumn++;
        }
        return false;
    }

    public static boolean checkRightDiagonal(int row, int column, char player) {
        /*
         * Checks to see if the player has won by checking the right diagonal
         * If the player has 4 tokens in a diagonal, the player wins
         */
        int counter = 0;
        int currentRow = row;
        int currentColumn = column;
        // locate the top right corner of the diagonal
        while (currentRow < 7 && currentColumn < 6) {
            currentRow++;
            currentColumn--;
        }

        // check the diagonal
        while (currentRow >= 0 && currentColumn >= 0) {
            if (gameBoard[currentRow][currentColumn] == player) {
                counter++;
            }
            else {
                counter = 0;
            }
            if (counter == 4) {
                return true;
            }
            currentRow--;
            currentColumn--;
        }
        return false;
    }
}