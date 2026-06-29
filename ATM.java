import java.util.Scanner;

class BankAccount {
    private double balance;

    public BankAccount(double initialBalance) {
        balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        }
        return false;
    }
}

class ATMMachine {
    private BankAccount account;

    public ATMMachine(BankAccount bankAccount) {
        account = bankAccount;
    }

    public void displayMenu() {
        System.out.println("\n========== ATM MENU ==========");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Exit");
        System.out.println("==============================");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenu();
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Current Balance: Rs. " + account.getBalance());
                    break;

                case 2:
                    System.out.print("Enter amount to deposit: Rs. ");
                    double depositAmount = scanner.nextDouble();

                    if (depositAmount > 0) {
                        account.deposit(depositAmount);
                        System.out.println("Deposit Successful!");
                        System.out.println("Updated Balance: Rs. " + account.getBalance());
                    } else {
                        System.out.println("Invalid amount!");
                    }
                    break;

                case 3:
                    System.out.print("Enter amount to withdraw: Rs. ");
                    double withdrawAmount = scanner.nextDouble();

                    if (withdrawAmount > 0) {
                        if (account.withdraw(withdrawAmount)) {
                            System.out.println("Withdrawal Successful!");
                            System.out.println("Remaining Balance: Rs. " + account.getBalance());
                        } else {
                            System.out.println("Insufficient Balance!");
                        }
                    } else {
                        System.out.println("Invalid amount!");
                    }
                    break;

                case 4:
                    System.out.println("Thank you for using the ATM!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}

public class ATM {
    public static void main(String[] args) {
        BankAccount userAccount = new BankAccount(1000);
        ATMMachine atm = new ATMMachine(userAccount);
        atm.run();
    }
}