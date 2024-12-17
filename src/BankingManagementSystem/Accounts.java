package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long openAccount(String email) {
        if(!accountExist(email)) {
            String open_account_query = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES(?, ?, ?, ?, ?)";
            scanner.nextLine();
            System.out.print("Enter Full Name: ");
            String fullName = scanner.nextLine();
            System.out.print("Enter Initial Amount: ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter Security Pin: ");
            String securityPin = scanner.nextLine();

            try {
                long account_number = generateAccountNumber();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, fullName);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5, securityPin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return account_number;
                } else {
                    throw new RuntimeException("Account creation failed!!");
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Account Already Exist");
    }


    public long getAccountNumber(String email) {
        String query = "SELECT account_number FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account Number Doesn't Exist");
    }


    private long generateAccountNumber() {
        try {
            String query = "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                long lastAccountNumber = resultSet.getLong("account_number");
                return lastAccountNumber + 1;
            } else {
                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10000100;
    }


    public boolean accountExist(String email) {
        String query = "SELECT account_number from accounts Where email = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
