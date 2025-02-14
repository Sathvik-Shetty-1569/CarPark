package apcoders.in.carpark.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WalletModel {
    private double currentBalance;
    private ArrayList<WalletTransaction> transactions;

    // Empty constructor needed for Firestore serialization
    public WalletModel() {
        this.transactions = new ArrayList<>();
    }

    // Constructor with initial balance
    public WalletModel(double currentBalance) {
        this.currentBalance = currentBalance;
        this.transactions = new ArrayList<>();
    }

    // Getters and Setters
    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public ArrayList<WalletTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<WalletTransaction> transactions) {
        this.transactions = transactions;
    }

    // Method to add a transaction and update balance
    public void addTransaction(String transactionId, double amount, String type, String description) {
        WalletTransaction transaction = new WalletTransaction(transactionId, amount, type, description, new Date());
        transactions.add(transaction);

        // Update the balance based on the transaction type
        if ("credit".equalsIgnoreCase(type)) {
            currentBalance += amount;
        } else if ("debit".equalsIgnoreCase(type)) {
            currentBalance -= amount;
        }
    }
}
