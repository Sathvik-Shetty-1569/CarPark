package apcoders.in.carpark.models;

public class ParkingInfo {
    private String name;
    private int slots;
    private String amount;
    private String userId;

    public ParkingInfo(String name, int slots, String amount,String userId) {
        this.name = name;
        this.slots = slots;
        this.amount = amount;
        this.userId = userId;

    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public int getSlots() {
        return slots;
    }

    public String getAmount() {
        return amount;
    }

    public String getUserId() {
        return userId;
    }

}
