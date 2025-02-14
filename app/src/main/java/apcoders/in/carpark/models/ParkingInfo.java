package apcoders.in.carpark.models;

public class ParkingInfo {
    private String name;
    private int slots;
    private String amount;

    public ParkingInfo(String name, int slots, String amount) {
        this.name = name;
        this.slots = slots;
        this.amount = amount;
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
}
