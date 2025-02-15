package apcoders.in.carpark;
import android.view.animation.AlphaAnimation;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import apcoders.in.carpark.Utils.QRCodeManagement;
import apcoders.in.carpark.Utils.VehicleManagement;
import apcoders.in.carpark.models.BookingDetailsModel;
import apcoders.in.carpark.models.VehicleModel;
import es.dmoral.toasty.Toasty;

public class BookingSlotActivity extends AppCompatActivity {
    Button confirmBookingBtn;
    String bookingId, userId, vehicleNumber, parkingLotId, slotNumber, bookingTime, startTime, endTime, paymentStatus, qrCodeUrl, status;
    double amountPaid;
    FirebaseAuth firebaseAuth;
    private View blurView;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    SpinKitView spin_kit ;
    private RelativeLayout mainContent;
    StorageReference storageReference;
    String parkingName, AvailableSlots, Amount;
    BookingDetailsModel booking;
    TextView parkingAreaNameTextView, checkinTimeTextView, AvailableSlotsTextView, checkoutTimeTextView, amountPaidTextView;
    Spinner VehicleNumberSpinner;
    String selected_Vehicle_number_spinner = "Select Vehicle";
    private int checkInHour, checkInMinute, checkOutHour, checkOutMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_slot);

        setVehicleNumberSelectBox();
        spin_kit = findViewById(R.id.spin_kit);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("QRCODES");

        parkingName = getIntent().getStringExtra("parkingName");
        AvailableSlots = getIntent().getStringExtra("AvailableSlots");
        Amount = getIntent().getStringExtra("Amount");

        VehicleNumberSpinner = findViewById(R.id.VehicleNumberSpinner);
        parkingAreaNameTextView = findViewById(R.id.parkingAreaNameTextView);
        checkinTimeTextView = findViewById(R.id.textview_checkin);
        checkoutTimeTextView = findViewById(R.id.textview_checkout);
        amountPaidTextView = findViewById(R.id.textview_amount_paid);
        AvailableSlotsTextView = findViewById(R.id.AvailableSlotsTextView);

        AvailableSlotsTextView.setText(AvailableSlots);
        parkingAreaNameTextView.setText(parkingName);
        amountPaidTextView.setText(Amount);
        confirmBookingBtn = findViewById(R.id.confirmBookingBtn);




        checkinTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(true);

                if (!checkinTimeTextView.getText().toString().isEmpty() &&
                        !checkoutTimeTextView.getText().toString().isEmpty()) {
                    amountPaid = calculateParkingAmount(Double.parseDouble(Amount),
                            checkinTimeTextView.getText().toString(),
                            checkoutTimeTextView.getText().toString());

                    amountPaidTextView.setText(String.valueOf(amountPaid));
                }
            }
        });
        checkoutTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(false);

                if (!checkinTimeTextView.getText().toString().isEmpty() &&
                        !checkoutTimeTextView.getText().toString().isEmpty()) {
                    amountPaid = calculateParkingAmount(Double.parseDouble(Amount),
                            checkinTimeTextView.getText().toString(),
                            checkoutTimeTextView.getText().toString());

                    amountPaidTextView.setText(String.valueOf(amountPaid));
                }
            }

        });
        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spin_kit.setVisibility(VISIBLE);
                confirmBookingBtn.setActivated(false);
                bookingId = "PKG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                userId = firebaseAuth.getCurrentUser().getUid();
                vehicleNumber = selected_Vehicle_number_spinner;
                parkingLotId = "1";
                slotNumber = "1";
                bookingTime = new java.util.Date().toString();
                startTime = checkInHour + ":" + checkInMinute;
                endTime = checkOutHour + ":" + checkOutMinute;
                paymentStatus = "Paid";
                status = "Active";
                qrCodeUrl = "";

                booking = new BookingDetailsModel(bookingId, userId, vehicleNumber, parkingLotId, slotNumber, bookingTime, startTime, endTime, amountPaid, paymentStatus, qrCodeUrl, "Parking Area Name", status);

                String QRCodeData = QRCodeManagement.convertBookingToJson(booking);

                if (TextUtils.isEmpty(vehicleNumber) || startTime.isEmpty() || endTime.isEmpty() || amountPaid < 0) {
                    Toasty.error(BookingSlotActivity.this, "Please fill all the fields", Toasty.LENGTH_SHORT).show();
                    spin_kit.setVisibility(GONE);
                    confirmBookingBtn.setActivated(true);
                } else {
                    // Generate QR Code Bitmap
                    Bitmap qrBitmap = QRCodeManagement.generateQRCodeBitmap(QRCodeData);

                    if (qrBitmap != null) {
                        uploadQRCodeToFirebase(qrBitmap, bookingId);
                    } else {
                        spin_kit.setVisibility(GONE);
                        confirmBookingBtn.setActivated(true);
                        Toasty.error(BookingSlotActivity.this, "QR Code generation failed", Toasty.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void uploadQRCodeToFirebase(Bitmap qrBitmap, String bookingId) {

        StorageReference qrRef = storageReference.child("qrcodes/" + bookingId + ".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] qrBytes = baos.toByteArray();

        UploadTask uploadTask = qrRef.putBytes(qrBytes);
        uploadTask.addOnSuccessListener(taskSnapshot -> qrRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String qrCodeUrl = uri.toString();
            storeBookingDataToFirestore(bookingId, qrCodeUrl);
        })).addOnFailureListener(e ->
                Toasty.error(BookingSlotActivity.this, "Failed to upload QR Code", Toasty.LENGTH_SHORT).show()
        );
    }


    private void storeBookingDataToFirestore(String bookingId, String qrCodeUrl) {
        BookingDetailsModel booking = new BookingDetailsModel(bookingId, userId, vehicleNumber, parkingLotId, slotNumber, bookingTime, startTime, endTime, amountPaid, paymentStatus, qrCodeUrl, "Parking Area Name", status);

        firestore.collection("Bookings").document(bookingId)
                .set(booking)
                .addOnSuccessListener(aVoid -> {
                    Toasty.success(BookingSlotActivity.this, "Booking Confirmed!", Toasty.LENGTH_SHORT).show();

                    Intent i = new Intent(BookingSlotActivity.this, BookingCompleteActivity.class);
                    i.putExtra("BookingId", bookingId);
                    i.putExtra("ParkAreaName", parkingName);
                    startActivity(i);

                })
                .addOnFailureListener(e -> Toasty.error(BookingSlotActivity.this, "Firestore upload failed", Toasty.LENGTH_SHORT).show());
    }

    private void setVehicleNumberSelectBox() {

        VehicleManagement.getAllVehicles(new VehicleManagement.onVehicleListReceived() {
            @Override
            public void onCallback(ArrayList<VehicleModel> vehicleList) {
                if (vehicleList.isEmpty()) {
                    Toasty.error(BookingSlotActivity.this, "No Vehicles Found", Toasty.LENGTH_SHORT).show();
                    return;
                } else {
                    ArrayList<String> vehicleNumbers = new ArrayList<>();
                    vehicleNumbers.add("Select Vehicle");
                    for (VehicleModel vehicle : vehicleList) {
                        vehicleNumbers.add(vehicle.getVehicleNumber());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            BookingSlotActivity.this,
                            R.layout.dropdown_layout,
                            vehicleNumbers
                    );

                    VehicleNumberSpinner.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    // Optionally, you can set an item selected listener
                    VehicleNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selected_Vehicle_number_spinner = (String) parent.getItemAtPosition(position);
                            Log.d("TAG", "onItemSelected: " + selected_Vehicle_number_spinner);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Handle the case when nothing is selected
                            selected_Vehicle_number_spinner = "Select Vehicle";
                        }
                    });
                }
            }
        });
    }

    private void showTimePickerDialog(boolean isCheckIn) {
        // Get current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    @SuppressLint("DefaultLocale")
                    String formattedTime = String.format("%02d:%02d %s",
                            (selectedHour == 0 || selectedHour == 12) ? 12 : selectedHour % 12,
                            selectedMinute,
                            (selectedHour < 12) ? "AM" : "PM");

                    if (isCheckIn) {
                        checkInHour = selectedHour;
                        checkInMinute = selectedMinute;
                        checkinTimeTextView.setText(formattedTime);
                    } else {
                        checkOutHour = selectedHour;
                        checkOutMinute = selectedMinute;
                        checkoutTimeTextView.setText(formattedTime);
                    }

                    if (!checkinTimeTextView.getText().toString().isEmpty() &&
                            !checkoutTimeTextView.getText().toString().isEmpty()) {

                        amountPaid = calculateParkingAmount(
                                Double.parseDouble(Amount),
                                checkinTimeTextView.getText().toString(),
                                checkoutTimeTextView.getText().toString()
                        );

                        amountPaidTextView.setText(String.valueOf(Math.abs(amountPaid))); // Ensures the value is always positive
                    }
                    // ✅ Calculate only after both times are set

                }, hour, minute, false); // false for 12-hour format

        timePickerDialog.show();
    }

    public double calculateParkingAmount(double amountPerHour, String checkIn, String checkOut) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a"); // Time format (12-hour with AM/PM)

        try {
            Date checkInTime = sdf.parse(checkIn);
            Date checkOutTime = sdf.parse(checkOut);

            if (checkInTime == null || checkOutTime == null) {
                return 0;
            }

            if (checkOutTime.before(checkInTime)) {
                // If checkout time is earlier than check-in, assume it's the next day
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(checkOutTime);
                calendar.add(Calendar.DATE, 1); // Add one day to checkout time
                checkOutTime = calendar.getTime();
            }

            if (checkOutTime.before(checkInTime)) {
                Toast.makeText(BookingSlotActivity.this, "Invalid Checkout Time! Must be after Check-In.", Toast.LENGTH_SHORT).show();
                return 0;
            }

            // Ensure checkout time is always after check-in
            long differenceMillis = Math.abs(checkOutTime.getTime() - checkInTime.getTime()); // Ensure positive difference

            // Convert milliseconds to hours (round up to the next hour if any minutes exist)
            double hours = Math.ceil((double) differenceMillis / (1000 * 60 * 60));

            return hours * amountPerHour;

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void fadeOutLoadingScreen() {
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(1000); // 1 second fade-out animation
        fadeOut.setFillAfter(true);
        mainContent.startAnimation(fadeOut);
    }


    @Override
    public void onBackPressed() {

        Intent i = new Intent(BookingSlotActivity.this, MainActivity.class);
        i.putExtra("LoadFragment", "Location");
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
