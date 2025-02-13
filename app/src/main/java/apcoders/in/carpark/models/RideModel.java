package apcoders.in.carpark.models;

public class RideModel {
    private String username, parkingArea, uniqueId, checkIn, checkOut, specifications;

    public RideModel(String username, String parkingArea, String uniqueId, String checkIn, String checkOut, String specifications) {
        this.username = username;
        this.parkingArea = parkingArea;
        this.uniqueId = uniqueId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.specifications = specifications;
    }

    public String getUsername() { return username; }
    public String getParkingArea() { return parkingArea; }
    public String getUniqueId() { return uniqueId; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
    public String getSpecifications() { return specifications; }
}
