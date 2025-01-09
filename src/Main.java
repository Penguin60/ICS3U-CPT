import java.util.Scanner;

public class Main {
    // ANSI escape codes for text formatting
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BOLD = "\033[0;1m";
    public static final String RESET = "\u001B[0m";
    // Game state constants
    public static final byte CONTINUE = 0;
    public static final byte RED_WINS = 1;
    public static final byte YELLOW_WINS = 2;
    public static final byte DRAW = 3;
    // Global scanner object for user input
    public static final Scanner scanner = new Scanner(System.in);
    // Initial game board layout 7 x 6
    public static char[][] gameBoard = new char[7][6];
    /*
     * The template for displaying the gameboard
     * The '*' characters will be replaced by the tokens
     * The '#' characters will be replaced by the column numbers
     * Allows for dynamic coloring
     */
    public static final String[] boardTemplate = {
        BLUE + "-----------------------------",
        BLUE + "| * | * | * | * | * | * | * |",
        BLUE + "-----------------------------",
        BLUE + "| * | * | * | * | * | * | * |",
        BLUE + "-----------------------------",
        BLUE + "| * | * | * | * | * | * | * |",
        BLUE + "-----------------------------",
        BLUE + "| * | * | * | * | * | * | * |",
        BLUE + "-----------------------------",
        BLUE + "| * | * | * | * | * | * | * |",
        BLUE + "-----------------------------",
        BLUE + "| * | * | * | * | * | * | * |",
        BLUE + "-----------------------------",
        BLUE + "| # | # | # | # | # | # | # |",
        BLUE + "-----------------------------",
    };
    // Wins for each player
    public static int redWins = 0;
    public static int yellowWins = 0;

    public static void main(String[] args) {
        // Welcome message
        System.out.println();
        System.out.println("                                           __ __ ");
        System.out.println("  _________  _  ___  _________________    / // / ");
        System.out.println(" / ___/ __ \\/ |/ / |/ / __/ ___/_  __/   / // /_");
        System.out.println("/ /__/ /_/ /    /    / _// /__  / /     /__  __/ ");
        System.out.println("\\___/\\____/_/|_/_/|_/___/\\___/ /_/        /_/ ");
        System.out.println();
        System.out.println();

        // Continually repeat the program until the player quits.
        while (true) {
            String input;

            do {
                // Print the menu
                System.out.println(BOLD);
                System.out.println("╔══════════════════════════╗");
                System.out.println("║ Welcome to Connect Four! ║");
                System.out.println("╠══════════════════════════╣");
                System.out.println("║ 1. Play                  ║");
                System.out.println("║ 2. Rules                 ║");
                System.out.println("║ 3. Stats                 ║");
                System.out.println("║ 4. Quit                  ║");
                System.out.println("╚══════════════════════════╝");
                System.out.println(RESET);

                // Get the user's input, using regex to restrict it to 1-4
                input = getAndValidateInput(
                    "Please select an option (1-4): ",
                    "[1-4]"
                );
                System.out.println();

                // Menu options (default case is not needed because the input is restricted to 1-4)
                switch (input) {
                    case "1":
                        // Reset the gameboard
                        resetGameboard();
                        break;
                    case "2": // Prints the rules of Connect 4
                        System.out.println("The Connect 4 game is a classic strategy game in which 2 players go head-to-head in a battle to own the grid!" +
                            "\nPlayers choose yellow or red tokens. They drop the discs into the grid," +
                            "\nstarting in the middle or at the edge to stack their colored discs upwards, horizontally, or diagonally." +
                            "\nUse strategy to block opponents while aiming to be the first player to get 4 in a row to win!" +
                            "\nCourtesy of Hasbro Instructions."
                        );

                        System.out.println();
                        /*
                         * Wait for the user to press enter before continuing
                         * Gives them time to read through the rules
                         */
                        awaitEnter();
                        break;
                    case "3": // Prints the stats
                        System.out.println(BOLD + "Wins:" + RESET +
                            YELLOW + "\nYellow: " + RESET + yellowWins +
                            RED + "\nRed: " + RESET + redWins
                        );
                        System.out.println();
                        /*
                         * Wait for the user to press enter before continuing
                         * Gives them time to read the stats
                         */
                        awaitEnter();
                        break;
                    case "4": // Exits the program
                        System.out.println("Thanks for playing!");
                        return;
                }
            }
            // Continue to display the menu until the user chooses to play
            while (!input.equals("1"));

            // The status of the game, determining whether to continue,
            // whether a player has won, or if a tie has been reached
            byte gameStatus;
            printGameboard();

            // The game should constantly run until one of the exit statements is reached
            while (true) {
                // Gets the column the user wishes to place the token in
                // 1 is subtracted from the column to account for the 0-based index
                int columnIndex = getAndValidateColumn(
                    YELLOW + "Yellow" + RESET + ", on which column do you want to place your token (1-7)? "
                ) - 1;

                // Puts the token 'Y' at the bottom of the column that is chosen
                int rowIndex = modifyGameboard(columnIndex, 'Y');

                printGameboard();

                gameStatus = getGameboardStatus(rowIndex, columnIndex, 'Y');
                // Checks if the game has ended
                if (gameStatus != CONTINUE) {
                    break;
                }
                // Gets the column the user wishes to place the token in
                // 1 is subtracted from the column to account for the 0-based index
                columnIndex = getAndValidateColumn(
                    RED + "Red" + RESET + ", on which column do you want to place your token (1-7)? "
                ) - 1;

                // Puts the token 'R' at the bottom of the column that is chosen
                rowIndex = modifyGameboard(columnIndex, 'R');

                printGameboard();

                gameStatus = getGameboardStatus(rowIndex, columnIndex, 'R');
                // Checks if the game has ended
                if (gameStatus != CONTINUE) {
                    break;
                }
            }
            printResults(gameStatus);
        }
    }

    /**
     * Prints the results of the game, including the winner or if the game ended in a draw
     *
     * @param gameStatus the status of the game. 1 for red wins, 2 for yellow wins, 3 for draw. Will never be 0
     */
    public static void printResults(int gameStatus) {
        switch (gameStatus) {
            case RED_WINS:
                System.out.println(RED + "Red" + RESET + " wins!");
                redWins++;
                return;
            case YELLOW_WINS:
                System.out.println(YELLOW + "Yellow" + RESET + " wins!");
                yellowWins++;
                return;
            default:
                System.out.println("The game has ended in a draw!");
        }
    }

    /**
     * Resets gameboard by setting all values within it to the default in preparation for a new game
     */
    public static void resetGameboard() {
        // Fills the gameboard with empty spaces (clears the board)
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                gameBoard[i][j] = ' ';
            }
        }
    }

    /**
     * Prints the current status of the gameboard after a change has been made
     */
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
                // Iterate through the columns, replacing each occurrence of '#' with the column number
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

    /**
     * Applies formatting to the player discs, adding the appropriate colours to them
     *
     * @param player the team the player is on (yellow or red)
     * @return a string that contains a token coloured in accordance with the player team, or an empty space
     */
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

    /**
     * Asks the user to input where they want to place their disk and validates the input
     *
     * @param prompt the cue asking the user where they would like to go
     * @return the validated column that the user has chosen to place their disc in
     */
    public static int getAndValidateColumn(String prompt) {
        // Checks if the column that the user inputs is valid from columns 1 to 7
        // 1 is subtracted from the column to account for the 0-based index
        int columnIndex = Integer.parseInt(getAndValidateInput(
            prompt,
            "[1-7]"
        )) - 1;

        // Checks if the top row at said column is empty
        while (gameBoard[columnIndex][gameBoard[columnIndex].length - 1] != ' ') {
            columnIndex = Integer.parseInt(getAndValidateInput(
                RED + "Column " + columnIndex + " is full, please choose another column: " + RESET,
                "[1-7]"
            ));
        }

        return columnIndex;
    }

    /**
     * Ensures input is of the proper type and fits within the correct bounds
     *
     * @param prompt the cue asking the user where they would like to go
     * @param regex  the correct parameters for the input
     * @return the user input once it has been validated
     */
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

    /**
     * Adds discs that have been inputted by the user to their appropriate spot in the gameboard
     *
     * @param column the column in which the new disc should go
     * @param player the player colour that the disc belongs to
     * @return the row (y coordinate) in which the disc was placed,
     * or -1 if the player is invalid or the column is full
     */
    public static int modifyGameboard(int column, char player) {
        /*
         * Checks to see which team the player is on
         * Will attempt to place a token at the bottom of a column to simulate how gravity would impact a token in real life
         * Iterates from bottom to top of the specified column to find the first empty space
         */
        switch (player) {
            case 'Y':
                for (int y = 0; y < 7; y++) {
                    if (gameBoard[column][y] == ' ') {
                        gameBoard[column][y] = 'Y';
                        return y;
                    }
                }
                break;
            case 'R':
                for (int y = 0; y < 7; y++) {
                    if (gameBoard[column][y] == ' ') {
                        gameBoard[column][y] = 'R';
                        return y;
                    }
                }
                break;
        }
        return -1;
    }

    /**
     * Checks to determine if the game has ended,
     * whether through a player victory or through a draw
     *
     * @param rowIndex    the row in which the latest disc was placed
     * @param columnIndex the column in which the latest disc was placed
     * @param player      the colour of the player who placed the latest disc
     * @return a number based on the game's current state. 0 for continue, 1 for red wins, 2 for yellow wins, 3 for draw
     */
    public static byte getGameboardStatus(int rowIndex, int columnIndex, char player) {
        /*
         * Checks to see if the player has won the game, or if the game has ended in a draw
         * Set the gameboard to full by default, because
         * the gameboard is empty as long as one column is not full
         */
        boolean gameBoardFull = true;
        for (char[] column : gameBoard) {
            // Check if the top of the column is empty
            // If it is, the game board is not full and the loop can exit
            if (column[column.length - 1] == ' ') {
                gameBoardFull = false;
                break;
            }
        }
        if (gameBoardFull) {
            return DRAW;
        }
        // Checks to see if the player has won by searching row, column, left diagonal, and right diagonal
        if (checkRow(rowIndex, player)
            || checkColumn(columnIndex, player)
            || checkLeftDiagonal(rowIndex, columnIndex, player)
            || checkRightDiagonal(rowIndex, columnIndex, player)
        ) {
            return player == 'Y' ? YELLOW_WINS : RED_WINS;
        }
        return CONTINUE;
    }

    /**
     * Checks the row of the latest disc placed to see if there are four consecutive ones
     *
     * @param rowIndex the row in which the latest disc was placed
     * @param player   the colour of the player who placed the latest disc
     * @return true if player has won, false otherwise
     */
    public static boolean checkRow(int rowIndex, char player) {
        /*
         * Checks to see if the player has won by checking the rowIndex
         * If the player has 4 tokens in a rowIndex, the player wins
         */
        int counter = 0;
        for (int x = 0; x < 7; x++) {
            // increment counter if the current game piece is the same color as the current player
            if (gameBoard[x][rowIndex] == player) {
                counter++;
            }
            // reset counter whenever the current game piece is not the same color as the current player
            else {
                counter = 0;
            }
            // return true if the counter reaches four consecutive game pieces of the same color as the current player
            if (counter == 4) {
                return true;
            }
        }
        // return false if the loop exits; the player has not won
        return false;
    }

    /**
     * Checks the column of the latest disc placed to see if there are four consecutive discs
     *
     * @param columnIndex the column in which the latest disc was placed
     * @param player      the colour of the player who placed the latest disc
     * @return true if the player has won, false otherwise
     */
    public static boolean checkColumn(int columnIndex, char player) {
        /*
         * Checks to see if the player has won by checking the column
         * If the player has 4 tokens in a column, the player wins
         */
        int counter = 0;
        // check the column
        for (int y = 0; y < 6; y++) {
            // increment counter if the current game piece is the same color as the current player
            if (gameBoard[columnIndex][y] == player) {
                counter++;
            }
            // reset counter whenever the current game piece is not the same color as the current player
            else {
                counter = 0;
            }
            // return true if the counter reaches four consecutive game pieces of the same color as the current player
            if (counter == 4) {
                return true;
            }
        }
        // return false if the loop exits; the player has not won
        return false;
    }

    /**
     * Checks the diagonal from the top right to the bottom left along the latest disc that was placed
     *
     * @param row    the row in which the latest disc was placed
     * @param column the column in which the latest disc was placed
     * @param player the colour of the player who placed the latest disc
     * @return true if the player has won, false otherwise
     */
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
            // increment counter if the current game piece is the same color as the current player
            if (gameBoard[currentColumn][currentRow] == player) {
                counter++;
            }
            // reset counter whenever the current game piece is not the same color as the current player
            else {
                counter = 0;
            }
            // return true if the counter reaches four consecutive game pieces of the same color as the current player
            if (counter == 4) {
                return true;
            }
            // move to the next piece on the grid
            currentRow--;
            currentColumn++;
        }
        // return false if the loop exits; the player has not won
        return false;
    }

    /**
     * Checks the diagonal from the top left to the bottom right along the latest disc that was placed
     *
     * @param row    the row in which the latest disc was placed
     * @param column the column in which the latest disc was placed
     * @param player the colour of the player who placed the latest disc
     * @return true if the player has won, false otherwise
     */
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
            // increment counter if the current game piece is the same color as the current player
            if (gameBoard[currentColumn][currentRow] == player) {
                counter++;
            }
            // reset counter whenever the current game piece is not the same color as the current player
            else {
                counter = 0;
            }
            // return true if the counter reaches four consecutive game pieces of the same color as the current player
            if (counter == 4) {
                return true;
            }
            // move to the next piece on the grid
            currentRow--;
            currentColumn--;
        }
        // return false if the loop exits; the player has not won
        return false;
    }

    /**
     * Waits for the user to press enter before continuing.
     */
    public static void awaitEnter() {
        System.out.println();
        System.out.print(WHITE + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }
}
