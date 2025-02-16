package apcoders.in.carpark.models;


public class SubscriptionModel {
    private String plan;
    private String startDate;
    private String expiryDate;
    private double price;
    private String status;
    private  String userId;

    public SubscriptionModel() { } // Empty constructor required for Firebase

    public SubscriptionModel(String plan, String startDate, String expiryDate, double price, String status, String userId) {
        this.plan = plan;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.price = price;
        this.status = status;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getters
    public String getPlan() { return plan; }
    public String getStartDate() { return startDate; }
    public String getExpiryDate() { return expiryDate; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }

    // Setters
    public void setPlan(String plan) { this.plan = plan; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public void setPrice(double price) { this.price = price; }
    public void setStatus(String status) { this.status = status; }
}
