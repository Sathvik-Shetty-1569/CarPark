package apcoders.in.carpark;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.UUID;

import apcoders.in.carpark.Utils.QRCodeManagement;
import apcoders.in.carpark.models.BookingDetailsModel;
import es.dmoral.toasty.Toasty;

public class BookingSlotActivity extends AppCompatActivity {
    Button confirmBookingBtn;
    String bookingId, userId, vehicleNumber, parkingLotId, slotNumber, bookingTime, startTime, endTime, paymentStatus, qrCode, status;
    double amountPaid;
    FirebaseAuth firebaseAuth;
    ImageView qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_slot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        firebaseAuth = FirebaseAuth.getInstance();

        qrcode = findViewById(R.id.qrcode);
        confirmBookingBtn = findViewById(R.id.confirmBookingBtn);
        confirmBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingId = "PKG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                userId = firebaseAuth.getCurrentUser().getUid();
                vehicleNumber = "MH 12 AB 2332";
                parkingLotId = "1";
                slotNumber = "1";
                bookingTime = new java.util.Date().toString();
                startTime = "10:00";
                endTime = "11:00";
                amountPaid = 100.0;
                paymentStatus = "Paid";
                qrCode = "https://example.com/qr/" + bookingId;
                status = "Active";

                if (TextUtils.isEmpty(vehicleNumber) || startTime.isEmpty() || endTime.isEmpty() || amountPaid <= 0) {
                    Toasty.error(BookingSlotActivity.this, "Please fill all the fields", Toasty.LENGTH_SHORT).show();
                } else {
                    QRCodeManagement.generateQRCode(userId, qrcode);
                    BookingDetailsModel bookingDetailsModel = new BookingDetailsModel(bookingId, userId, vehicleNumber, parkingLotId, slotNumber, bookingTime, startTime, endTime, amountPaid, paymentStatus, qrCode, status);
                }
            }
        });
    }
}