package TicTacToe;

import java.sql.*;

import java.util.Scanner;

public class TicTacToe {
    private static final int SIZE = 3;
    private static final char[][] board = new char[SIZE][SIZE];
    private static char currentPlayer = 'X';

    static final String JDBC_URL = "jdbc:mysql://localhost:3306/tictactoe";
    static final String USERNAME = "root";
    static final String PASSWORD = "wnoesaid";


    public static void main(String[] args) {
        initializeBoard();
        printBoard();

        Scanner scanner = new Scanner(System.in);





        while (!isGameOver()) {
            System.out.println("Player " + currentPlayer + "'s turn. Enter row and column (e.g., 1 1): ");
            int row = scanner.nextInt() - 1;
            int col = scanner.nextInt() - 1;

            if (isValidMove(row, col)) {
                board[row][col] = currentPlayer;
                printBoard();
                if (isWinner()) {
                    System.out.println("Player " + currentPlayer + " wins!");
                    break;
                } else if (isBoardFull()) {
                    System.out.println("It's a draw!");
                    break;
                }
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            } else {
                System.out.println("Invalid move. Please try again.");
            }
        }
        scanner.close();











    }

    private static boolean isGameOver() {
        return false;
    }

    private static void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = '-';
            }
        }
    }

    private static void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean isValidMove(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == '-';
    }

    private static boolean isWinner() {
        // Check rows, columns, and diagonals for a winner
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] != '-' && board[i][0] == board[i][1] && board[i][0] == board[i][2]) {
                return true;
            }
            if (board[0][i] != '-' && board[0][i] == board[1][i] && board[0][i] == board[2][i]) {
                return true;
            }
        }
        if (board[0][0] != '-' && board[0][0] == board[1][1] && board[0][0] == board[2][2]) {
            return true;
        }
        if (board[0][2] != '-' && board[0][2] == board[1][1] && board[0][2] == board[2][0]) {
            return true;
        }
        return false;
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; i++) {
                if (board[i][j] == '-') return false;
            }
        }
        return true;
    }






    private static void registerPlayer(Connection connection, String playerName) {
        try {
            PreparedStatement insertPlayerStmt = connection.prepareStatement("INSERT INTO Players (Player_Name) VALUES (?)");
            insertPlayerStmt.setString(1, playerName);
            insertPlayerStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void playGame(String player1Name, String player2Name, Connection connection) {

        System.out.println("It's a draw!");


        updateHighScores(connection, player1Name);
        updateHighScores(connection, player2Name);


        displayHighScores(connection);
    }


        private static void updateHighScores(Connection connection, String playerName) {
            try {
                PreparedStatement updateHighScoresStmt = connection.prepareStatement("INSERT INTO HighScores (Player_ID, HighScore) " +
                        "VALUES ((SELECT Player_ID FROM Players WHERE Player_Name = ?), 1) " +
                        "ON DUPLICATE KEY UPDATE HighScore = HighScore + 1");
                updateHighScoresStmt.setString(1, playerName);
                updateHighScoresStmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        private static void displayHighScores(Connection connection) {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT Player_Name, HighScore FROM Players JOIN HighScores " +
                        "ON Players.Player_ID = HighScores.Player_ID ORDER BY HighScore DESC");
                System.out.println("High Scores:");
                while (resultSet.next()) {
                    String playerName = resultSet.getString("Player_Name");
                    int highScore = resultSet.getInt("HighScore");
                    System.out.println(playerName + ": " + highScore);
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

