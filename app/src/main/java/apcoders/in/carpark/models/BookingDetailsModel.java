package apcoders.in.carpark.models;

public class BookingDetailsModel {
    private String bookingId;
    private String userId;
    private String vehicleNumber;
    private String parkingLotId;
    private String slotNumber;
    private String bookingTime;
    private String startTime;
    private String endTime;
    private double amountPaid;
    private String paymentStatus;
    private String qrCode;
    String ParkingAreaName;
    private String status;

    // Empty constructor for Firebase
    public BookingDetailsModel() {
    }

    // Constructor with parameters
    public BookingDetailsModel(String bookingId, String userId, String vehicleNumber, String parkingLotId,
                               String slotNumber, String bookingTime, String startTime, String endTime,
                               double amountPaid, String paymentStatus, String qrCode, String ParkingAreaName, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.vehicleNumber = vehicleNumber;
        this.parkingLotId = parkingLotId;
        this.slotNumber = slotNumber;
        this.bookingTime = bookingTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.amountPaid = amountPaid;
        this.paymentStatus = paymentStatus;
        this.qrCode = qrCode;
        this.ParkingAreaName = ParkingAreaName;
        this.status = status;
    }

    // Getters and Setters
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
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

    public String getParkingLotId() {
        return parkingLotId;
    }

    public void setParkingLotId(String parkingLotId) {
        this.parkingLotId = parkingLotId;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getStatus() {
        return status;
    }

    public String getParkingAreaName() {
        return ParkingAreaName;
    }

    public void setParkingAreaName(String parkingAreaName) {
        ParkingAreaName = parkingAreaName;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
