package BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {

    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "Pranavtomar1627";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long accountNumber;

            while (true) {
                System.out.println("*** Welcome to BANKING SYSTEM ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1) {
                    case 1 -> {
                        user.register();
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                    }
                    case 2 -> {
                        email = user.login();
                        if(email != null) {
                            System.out.println();
                            System.out.println("User logged In!");
                            if(!accounts.accountExist(email)) {
                                System.out.println();
                                System.out.println("1. Open a new bank Account");
                                System.out.println("2. Exit");
                                if(scanner.nextInt() == 1) {
                                    accountNumber = accounts.openAccount(email);
                                    System.out.println("Account Created Successfully!");
                                    System.out.println("Your Account Number is: " + accountNumber);
                                } else {
                                    break;
                                }
                            }
                            accountNumber = accounts.getAccountNumber(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1 -> accountManager.debitMoney(accountNumber);
                                    case 2 -> accountManager.creditMoney(accountNumber);
                                    case 3 -> accountManager.transferMoney(accountNumber);
                                    case 4 -> accountManager.getBalance(accountNumber);
                                    case 5 -> System.out.println("Logging Out...");
                                    default -> System.out.println("Enter Valid Choice!");
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email or Password!");
                        }
                    }
                    case 3 -> {
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    }
                    default -> System.out.println("Enter Valid Choice!!");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }

}
