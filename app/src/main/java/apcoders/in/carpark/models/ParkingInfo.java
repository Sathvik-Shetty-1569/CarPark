package apcoders.in.carpark.models;

import java.util.List;

public class ParkingInfo {
    private String name;
    private String slots;
    private String amount;
    String UserId;
    Double lat,log;
    String locaddress;
    private List<String> ParkingAreaImagesUrl;

    public ParkingInfo() {
    }

    public ParkingInfo(String name, String slots, String amount, String userId, Double lat, Double log, String locaddress, List<String> parkingAreaImagesUrl) {
        this.name = name;
        this.slots = slots;
        this.amount = amount;
        UserId = userId;
        this.lat = lat;
        this.log = log;
        this.locaddress = locaddress;
        this.ParkingAreaImagesUrl = parkingAreaImagesUrl;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLog() {
        return log;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    public String getLocaddress() {
        return locaddress;
    }

    public void setLocaddress(String locaddress) {
        this.locaddress = locaddress;
    }

    public List<String> getParkingAreaImagesUrl() {
        return ParkingAreaImagesUrl;
    }

    public void setParkingAreaImagesUrl(List<String> parkingAreaImagesUrl) {
        ParkingAreaImagesUrl = parkingAreaImagesUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlots() {
        return slots;
    }

    public void setSlots(String slots) {
        this.slots = slots;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
