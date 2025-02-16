package apcoders.in.carpark.models;


import java.util.Date;

public class SubscriptionModel {
    private String plan;
    private Date startDate;
    private Date expiryDate;
    private double price;
    private String status;
    private String userId;
    String qrCodeUrl;

    public SubscriptionModel() {
    } // Empty constructor required for Firebase

    public SubscriptionModel(String plan, Date startDate, Date expiryDate, double price, String status, String userId) {
        this.plan = plan;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.price = price;
        this.status = status;
        this.userId = userId;
    }

    public SubscriptionModel(String plan, Date startDate, Date expiryDate, double price, String status, String userId, String qrCodeUrl) {
        this.plan = plan;
        this.startDate = startDate;
        this.expiryDate = expiryDate;
        this.price = price;
        this.status = status;
        this.userId = userId;
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}