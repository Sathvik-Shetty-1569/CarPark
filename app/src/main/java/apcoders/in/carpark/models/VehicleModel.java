package apcoders.in.carpark.models;

public class VehicleModel {
    private String vehicleId;        // Unique ID for the vehicle
    private String userId;           // User who owns the vehicle
    private String vehicleNumber;    // License plate number (e.g., MH12AB1234)
    private String vehicleType;      // Car, Bike, SUV, etc.
    private String vehicleBrand;     // Brand (e.g., Honda, Toyota)
    private String vehicleModel;     // Model (e.g., Civic, Fortuner)
    private String color;            // Vehicle color (optional)


    // Empty Constructor (Required for Firebase)
    public VehicleModel() {
    }

    // Constructor
    public VehicleModel(String vehicleId, String userId, String vehicleNumber,
                        String vehicleType, String vehicleBrand, String vehicleModel,
                        String color) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.color = color;
    }

    // Getters and Setters
    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return color;
    }

    public void setVehicleColor(String color) {
        this.color = color;
    }
}
