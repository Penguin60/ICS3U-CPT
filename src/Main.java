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
        // Welcome message
        System.out.println();
        System.out.println("                                           __ __ ");
        System.out.println("  _________  _  ___  _________________    / // / ");
        System.out.println(" / ___/ __ \\/ |/ / |/ / __/ ___/_  __/   / // /_");
        System.out.println("/ /__/ /_/ /    /    / _// /__  / /     /__  __/ ");
        System.out.println("\\___/\\____/_/|_/_/|_/___/\\___/ /_/        /_/ ");
        System.out.println();
        System.out.println();

        // Continually repeat the game until the player quits.
        while (true) {

            String input;

            do {
                // Print the menu
                System.out.println("+--------------------------+");
                System.out.println("| Welcome to Connect Four! |");
                System.out.println("+--------------------------+");
                System.out.println("| 1. Play                  |");
                System.out.println("| 2. Rules                 |");
                System.out.println("| 3. Quit                  |");
                System.out.println("+--------------------------+");
                System.out.println();

                input = getAndValidateInput(
                    "Please select an option (1-3): ",
                    "[1-3]"
                );
                System.out.println();

                // If the user selects 3, exit the game
                switch (input) {
                    case "1":
                        // Reset the gameboard
                        resetGame();
                        break;
                    case "2":
                        // TODO: Print rules
                        System.out.println("rules rules rules rules rules");
                        System.out.println();
                        /*
                         * Wait for the user to press enter before continuing
                         * Gives them time to read through the rules
                         */
                        scanner.nextLine();
                        awaitEnter();
                        break;
                    case "3":
                        System.out.println("Thanks for playing!");
                        return;
                }
            }
            while (input.equals("2"));

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
        }
    }

    /**
     * Resets gameboard by setting all values within it to the default in preparation for a new game
     */
    public static void resetGame() {
        gameRunning = true;

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

    /**
     * Applies formatting to the player discs, adding the appropriate colours to them
     *
     * @param player the team the player is on (yellow or red)
     * @return a string that contains a token coloured in accordance with the player team
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
     * Asks the user to input where they want to place their disk and validates
     *
     * @param prompt the cue asking the user where they would like to go
     * @return the validated column that the user has chosen to place their disc in
     */
    public static int getAndValidateColumn(String prompt) {
        // Checks if the column that the user inputs is valid from columns 1 to 7
        int column = Integer.parseInt(getAndValidateInput(
            prompt,
            "[1-7]"
        ));

        // Checks if the top row at said column is empty
        while (gameBoard[column - 1][gameBoard[column - 1].length - 1] != ' ') {
            column = Integer.parseInt(getAndValidateInput(
                RED + "Column " + column + " is full, please choose another column: " + RESET,
                "[1-7]"
            ));
        }

        return column;
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
            input = scanner.next();
        }
        while (!input.matches(regex));

        return input;
    }

    /**
     * Adds discs that have been inputted by the user to their appropriate spot in the gameboard
     *
     * @param column the column in which the new disc should go
     * @param player the player colour that the disc belongs to
     */
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

    /**
     * Checks to determine whether a player has won the game yet
     *
     * @param row    the row in which the latest disc was placed
     * @param column the column in which the latest disc was placed
     * @param player the colour of the player who placed the latest disc
     * @return true if player has won, false otherwise
     */
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

    /**
     * Checks the row of the latest disc placed to see if there are four consecutive ones
     *
     * @param rowIndex    the row in which the latest disc was placed
     * @param columnIndex the column in which the latest disc was played
     * @param player      the colour of the player who placed the latest disc
     * @return true if player has won, false otherwise
     */
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

    /**
     * Checks the column of the latest disc placed to see if there are four consecutive discs
     *
     * @param rowIndex    the row in which the latest disc was placed
     * @param columnIndex the column in which the latest disc was placed
     * @param player      the colour of the player who placed the latest disc
     * @return true if the player has won, false otherwise
     */
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
        System.out.print(WHITE + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }
}