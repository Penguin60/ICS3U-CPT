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
    public static final Scanner scanner = new Scanner(System.in);
    public static boolean gameRunning = true;
    public static boolean shouldContinue = true;
    public static char[][] gameBoard = new char[7][6];
    public static final String[] boardTemplate = {
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
        /*
         * TODO:
         *  Implement a menu system and add welcome message,
         *  also include rules of the game
         */

        awaitEnter();

        // As long as the player wants to play again, repeat the game.
        while (shouldContinue) {
            resetGame();
            // While the game is still running
            while (gameRunning) {
                printGameboard();
                // Initializes the column the user wishes to place the token in
                int column = getAndValidateColumn(
                    YELLOW + "Yellow" + RESET + ", on which column do you want to place your token (1-7)? "
                );

                // Puts the token 'Y' at the bottom of the column that is chosen
                modifyGameboard(column - 1, 'Y');
                if (!gameRunning) break;

                printGameboard();
                column = getAndValidateColumn(
                    RED + "Red" + RESET + ", on which column do you want to place your token (1-7)? "
                );

                // Puts the token 'R' at the bottom of the column that is chosen
                modifyGameboard(column - 1, 'R');
                if (!gameRunning) break;
            }
            String playAgain = getAndValidateInput(
                "Do you want to play again (y/n)? ",
                "[ynYN]"
            );
            if (playAgain.equalsIgnoreCase("n")) {
                // If the player doesn't want to play again, set shouldContinue to false
                shouldContinue = false;
            }
        }
    }

    public static void resetGame() {
        gameRunning = true;

        // Initializes the gameboard with empty spaces so that program to check to see what spots are taken
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                gameBoard[i][j] = ' ';
            }
        }
    }

    public static void printGameboard() {
        /*
         * Initializes the y value for accessing the gameboard
         * y starts at the max index and decrements to 0
         * this is because the board is being printed from top down
         */
        int y = 5;
        /*
         * Iterate through the gameboard template, replacing the '*' with the tokens
         * and the '#' with the column numbers
         */
        for (int i = 0; i < boardTemplate.length; i++) {
            String row = boardTemplate[i];
            /*
             * If the row is on an even row, no processing is necessary, as all
             * dividers have an even row index
             */
            if (i % 2 == 0) {
                System.out.println(row);
                continue;
            }
            // If the row is on the second last row, replace the '#' with the column numbers
            if (i == boardTemplate.length - 2) {
                // Iterate through the columns, replacing each occurence of '#' with the column number
                for (int columnNum = 1; columnNum <= 7; columnNum++) {
                    /*
                     * Replace the first occurrence of '#' with the column number, along
                     * with additional formatting
                     */
                    row = row.replaceFirst("[#]", RESET + columnNum + BLUE);
                }
                System.out.println(row);
                continue;
            }
            // Iterate through the row, replacing each occurrence of '*' with the
            // token in the corresponding position on the gameboard
            for (char[] column : gameBoard) {
                /*
                 * Replace the first occurrence of '*' with the intended token, along
                 * with additional formatting
                 */
                row = row.replaceFirst("[*]", getToken(column[y]));
            }
            System.out.println(row);
            y--;
        }
        // Reset the color of the text to default
        System.out.print(RESET);
    }

    public static String getToken(char player) {
        // Apply formatting to the token based on the player
        switch (player) {
            case 'Y':
                return YELLOW + "●" + BLUE;
            case 'R':
                return RED + "●" + BLUE;
            default:
                return " ";
        }
    }

    public static int getAndValidateColumn(String prompt) {
        /*
         * Checks if the column that the user inputs is valid from columns 1 to 7
         * Checks if the top row at said column is empty
         */
        int column = Integer.parseInt(getAndValidateInput(
            prompt,
            "[1-7]"
        ));
        while (gameBoard[column - 1][gameBoard[column - 1].length - 1] != ' ') {
            column = Integer.parseInt(getAndValidateInput(
                RED + "Column " + column + " is full, please choose another column: " + RESET,
                "[1-7]"
            ));
        }

        return column;
    }

    public static String getAndValidateInput(String prompt, String regex) {
        String input;
        /*
         * Loops until the user inputs a valid input
         * The input is valid if it matches the regex
         */
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
        }
        while (!input.matches(regex));

        return input;
    }

    public static void modifyGameboard(int column, char player) {
        /*
         * Checks to see which team the player is on
         * Will attempt to place a token at the bottom of a column to simulate how gravity would impact a token in real life
         */
        int row = 0;
        if (player == 'Y') {
            for (int y = 0; y < 7; y++) {
                if (gameBoard[column][y] == ' ') {
                    gameBoard[column][y] = 'Y';
                    row = y;
                    break;
                }
            }
            if (checkGameboard(row, column, player)) {
                printGameboard();
                System.out.println(YELLOW + "Yellow" + RESET + " wins!");
                gameRunning = false;
            }
        }
        else if (player == 'R') {
            for (int y = 0; y < 7; y++) {
                if (gameBoard[column][y] == ' ') {
                    gameBoard[column][y] = 'R';
                    row = y;
                    break;
                }
            }
            if (checkGameboard(row, column, player)) {
                printGameboard();
                System.out.println(RED + "Red" + RESET + " wins!");
                gameRunning = false;
            }
        }
    }

    public static boolean checkGameboard(int row, int column, char player) {
        /*
         * Checks to see if the player has won the game
         * Checks to see if the player has won by checking row, column, left diagonal, and right diagonal
         */
        return checkRow(row, column, player)
            || checkColumn(row, column, player)
            || checkLeftDiagonal(row, column, player)
            || checkRightDiagonal(row, column, player);
    }

    public static boolean checkRow(int rowIndex, int columnIndex, char player) {
        /*
         * Checks to see if the player has won by checking the rowIndex
         * If the player has 4 tokens in a rowIndex, the player wins
         */
        int counter = 0;
        for (int x = 0; x < 7; x++) {
            if (gameBoard[x][rowIndex] == player) {
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

    public static boolean checkColumn(int rowIndex, int columnIndex, char player) {
        /*
         * Checks to see if the player has won by checking the column
         * If the player has 4 tokens in a column, the player wins
         */
        int counter = 0;
        for (int y = 0; y < 6; y++) {
            if (gameBoard[columnIndex][y] == player) {
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
        while (currentRow < 5 && currentColumn > 0) {
            currentRow++;
            currentColumn--;
        }
        // check the diagonal
        while (currentRow >= 0 && currentColumn < 7) {
            if (gameBoard[currentColumn][currentRow] == player) {
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
        while (currentRow < 5 && currentColumn < 6) {
            currentRow++;
            currentColumn++;
        }
        // check the diagonal
        while (currentRow >= 0 && currentColumn >= 0) {
            if (gameBoard[currentColumn][currentRow] == player) {
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

    /**
     * Waits for the user to press enter before continuing.
     */
    public static void awaitEnter() {
        System.out.println();
        System.out.print(WHITE + "Press Enter to continue...");
        scanner.nextLine();
    }
}