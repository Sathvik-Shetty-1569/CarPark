package apcoders.in.carpark.models;

public class Session {
    private String username;
    private String parkingArea;
    private String date;
    private String totalTime;
    private String amount;
    private String timeRemaining;
    private boolean isActive;

    public Session(String username, String parkingArea, String date, String totalTime, String amount, String timeRemaining, boolean isActive) {
        this.username = username;
        this.parkingArea = parkingArea;
        this.date = date;
        this.totalTime = totalTime;
        this.amount = amount;
        this.timeRemaining = timeRemaining;
        this.isActive = isActive;
    }

    public String getUsername() { return username; }
    public String getParkingArea() { return parkingArea; }
    public String getDate() { return date; }
    public String getTotalTime() { return totalTime; }
    public String getAmount() { return amount; }
    public String getTimeRemaining() { return timeRemaining; }
    public boolean isActive() { return isActive; }
}
