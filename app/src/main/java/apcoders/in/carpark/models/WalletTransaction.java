package apcoders.in.carpark.models;

import java.util.Date;

public class WalletTransaction {
    private String transactionId;
    private double amount;
    private String type; // "credit" or "debit"
    private String description;
    private Date transactionDate;

    // Empty constructor for Firestore serialization
    public WalletTransaction() {}

    // Full constructor
    public WalletTransaction(String transactionId, double amount, String type, String description, Date transactionDate) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
