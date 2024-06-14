package TicTacToe;



import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MySQLconnection.connectDb();

        System.out.println("Welcome to the TicTacToe Game!");

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = getMenuChoice(scanner);

            switch (choice) {
                case 1:
                    showWinners();
                    break;
                case 2:
                    registerUser();
                    break;
                case 3:
                    showRegisteredUsers();
                    break;
                case 4:
                    startGame();
                    break;
                case 5:
                    System.out.println("Exiting program...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\nPlease choose an option:");
        System.out.println("1. Show winners");
        System.out.println("2. Register a user");
        System.out.println("3. Show registered users");
        System.out.println("4. Start the game");
        System.out.println("5. Exit");
    }

    private static int getMenuChoice(Scanner scanner) {
        System.out.print("Enter your choice (1-5): ");
        return scanner.nextInt();
    }

    private static void showWinners(){
        System.out.println("--- Winners ---");
        MySQLconnection.executeQueryAndPrintResults();
    }

    private static void showRegisteredUsers(){
        System.out.println("--- Users ---");
        MySQLconnection.registeredUsers();
    }


    private static void registerUser() {
        System.out.println("--- Register User ---");
        MySQLconnection.registerUser();
    }

    private static void startGame() {
        // login as X
        System.out.println("Player X, please login now");
        String playerX = MySQLconnection.loginUser();
        System.out.println("Player X: " + playerX + " is logged in\n");

        // login as O
        System.out.println("Player O, please login now");
        String playerO = MySQLconnection.loginUser();
        System.out.println("Player O: " + playerO + " is logged in\n");

        TicTacToeConsole ticTacToeConsole = new TicTacToeConsole(playerX, playerO);
    }
}