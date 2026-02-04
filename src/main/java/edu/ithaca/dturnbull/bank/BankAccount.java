package edu.ithaca.dturnbull.bank;

public class BankAccount {

    private final String email;
    private double balance;

    /**
     * @throws IllegalArgumentException if email is invalid
     */
    public BankAccount(String email, double startingBalance) {
        if (isEmailValid(email)) {
            this.email = email;
            this.balance = startingBalance;
        } else {
            throw new IllegalArgumentException("Email address: " + email + " is invalid, cannot create account");
        }
    }

    /**
     * @return The user's current bank account balance
     */
    public double getBalance() {
        return balance;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Withdraw some money from an account, usually for a purchase.
     * @post reduces the balance by amount if amount is non-negative and smaller than balance
     */
    public void withdraw (double amount) throws InsufficientFundsException {
        if (amount < 0) {
            throw new InsufficientFundsException("Cannot withdraw negative amount");
        } else if (amount <= balance) {
            balance -= amount;
        } else {
            throw new InsufficientFundsException("Not enough money");
        }
    }


    public static boolean isEmailValid(String email) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'*+/=?^_`{|}~- .@\"\\";
        char[] emailArray = email.toCharArray();
        
        if (email.indexOf('@') == -1){
            return false;
        }

        if (emailArray[0] == '.' || emailArray[emailArray.length - 1] == '.'){
            return false;
        }

        for (int i = 0; i < emailArray.length - 1; i++) {
            if (allowedChars.indexOf(emailArray[i]) == -1) {
                return false;
            }
            if (emailArray[i] == '.' && emailArray[i + 1] == '.') {
                return false;
            }
            if (emailArray[i] == '@' && emailArray[i + 1] == '.') {
                return false;
            }
        }
        
        return true;
    }
}