package apcoders.in.carpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import apcoders.in.carpark.Utils.BookingManagement;
import apcoders.in.carpark.models.BookingDetailsModel;

public class BookingCompleteActivity extends AppCompatActivity {
    Button back_to_home_screen;
    TextView bookingidtextview, parkingAreaNameTextView, textview_checkin, textview_checkout, textview_amount_paid;
    ImageView bookingQRCodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_complete);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bookingQRCodeImage = findViewById(R.id.bookingQRCodeImage);

        back_to_home_screen = findViewById(R.id.back_to_home_screen);
        bookingidtextview = findViewById(R.id.bookingidtextview);
        parkingAreaNameTextView = findViewById(R.id.parkingAreaNameTextView);
        textview_checkin = findViewById(R.id.textview_checkin);
        textview_checkout = findViewById(R.id.textview_checkout);
        textview_amount_paid = findViewById(R.id.textview_amount_paid);

        textview_amount_paid.setText(getIntent().getStringExtra("AmountPaid"));

        back_to_home_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BookingCompleteActivity.this, MainActivity.class));
                finish();
            }
        });

        String bookingId = getIntent().getStringExtra("BookingId");
        BookingManagement.fetchBookingDetails(bookingId, new BookingManagement.BookingCallback() {
            @Override
            public void onSuccess(BookingDetailsModel booking) {
                if (booking != null) {
                    Log.d("TAG", "onSuccess: " + booking.getBookingId());
                    bookingidtextview.setText("Unique ID: " + getIntent().getStringExtra("BookingId"));
                    parkingAreaNameTextView.setText(getIntent().getStringExtra("ParkingAreaName"));
                    textview_checkin.setText(booking.getStartTime());
                    textview_checkout.setText(booking.getEndTime());
//                    bookingQRCodeImage.setImageBitmap(booking.get());

                } else {
                    startActivity(new Intent(BookingCompleteActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: " + errorMessage);
            }
        });
    }
}