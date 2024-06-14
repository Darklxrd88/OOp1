package TicTacToe;

import java.sql.*;
import java.util.Scanner;
public class MySQLconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/database";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "wnoesaid";
    public static Connection connection = null;

    public static Connection connectDb() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
        return connection;
    }

    public static void executeQueryAndPrintResults() {
        try {
            String query = "SELECT username, COUNT(winner_id) AS wins " +
                    "FROM users " +
                    "INNER JOIN scores ON users.user_id = scores.winner_id " +
                    "GROUP BY scores.winner_id";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int wins = resultSet.getInt("wins");
                System.out.printf("Username: %s, Wins: %d%n", username, wins);
            }

        } catch (SQLException e) {
            System.out.println("Error executing the query: " + e.getMessage());
        }
    }

    public static void registeredUsers() {
        try {
            String query = "SELECT user_id, username from users";

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int user_id = resultSet.getInt("user_id");
                String username = resultSet.getString("username");
                System.out.printf("UserID %d, Username: %s%n", user_id, username);
            }

        } catch (SQLException e) {
            System.out.println("Error executing the query: " + e.getMessage());
        }
    }

    public static void registerUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        try {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            int rowsAffected = statement.executeUpdate();

            System.out.println("User registered successfully. Rows affected: " + rowsAffected);


        } catch (SQLException e) {
            System.out.println("Error executing the query: " + e.getMessage());
        }
    }

    public static String loginUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        try {
            String query = "SELECT username FROM users WHERE username = ? AND password = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("username");
            } else {
                System.out.println("Invalid username or password.");
                return null;
            }

        } catch (SQLException e) {
            System.out.println("Error executing the query: " + e.getMessage());
            return null;
        }
    }
    public static int getUserID(String username) {
        try {
            String query = "SELECT user_id from users where username = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            } else {
                System.out.println("Invalid username.");
                return 0;
            }

        } catch (SQLException e) {
            System.out.println("Error executing the query: " + e.getMessage());
        }
        return 0;
    }

    public static void registerWinner(int playerOneId, int playerTwoId, int winnerId) {
        try {
            String query = "INSERT INTO scores (player_one_id, player_two_id, winner_id) VALUES (?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt (1, playerOneId);
            statement.setInt (2, playerTwoId);
            statement.setInt (3, winnerId);
            int rowsAffected = statement.executeUpdate();

            System.out.println("Winner registered successfully. Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            System.out.println("Error executing the query: " + e.getMessage());
        }
    }
}