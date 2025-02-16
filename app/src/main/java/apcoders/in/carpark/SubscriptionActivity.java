package apcoders.in.carpark;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;

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
import java.util.Date;

import apcoders.in.carpark.Utils.QRCodeManagement;
import apcoders.in.carpark.Utils.SubscriptionManagement;
import apcoders.in.carpark.models.SubscriptionModel;
import es.dmoral.toasty.Toasty;

public class SubscriptionActivity extends AppCompatActivity {
    Button mini_plan_btn, oneMonth_btn, twoMonth_btn;
    String PlanName, PlanStatus;
    double PlanPrice;
    int PlanDuration;
    SpinKitView spin_kit;
    FirebaseFirestore firestore;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    StorageReference storageReference;
    Date StartDate, EndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subscription);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spin_kit = findViewById(R.id.spin_kit);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("QRCODES");

        mini_plan_btn = findViewById(R.id.mini_plan_btn);
        oneMonth_btn = findViewById(R.id.goldplan);
        twoMonth_btn = findViewById(R.id.premiumpack);

        mini_plan_btn.setOnClickListener(view -> {
            PlanName = "Mini";
            PlanPrice = 1500;
            PlanDuration = 7;
            PlanStatus = "Active";
            StartDate = new Date();
            EndDate = new Date(StartDate.getTime() + PlanDuration * 24 * 60 * 60 * 1000);
            save2();
        });
        oneMonth_btn.setOnClickListener(view -> {
            PlanName = "OneMonth";
            PlanPrice = 2500;
            PlanDuration = 28;
            PlanStatus = "Active";
            StartDate = new Date();
            EndDate = new Date(StartDate.getTime() + PlanDuration * 24 * 60 * 60 * 1000);
            save2();
        });
        twoMonth_btn.setOnClickListener(view -> {
            PlanName = "TwoMonth";
            PlanPrice = 5000;
            PlanDuration = 56;
            PlanStatus = "Active";
            StartDate = new Date();
            EndDate = new Date(StartDate.getTime() + PlanDuration * 24 * 60 * 60 * 1000);
            save2();
        });
    }

    public void save2() {
        spin_kit.setVisibility(VISIBLE);
        SubscriptionModel model = new SubscriptionModel(PlanName, StartDate, EndDate, PlanPrice, PlanStatus, firebaseAuth.getCurrentUser().getUid());
        String QRCodeData = QRCodeManagement.convertBookingToJson(model);
        // Generate QR Code Bitmap
        Bitmap qrBitmap = QRCodeManagement.generateQRCodeBitmap(QRCodeData);
        uploadQRCodeToFirebase(qrBitmap, firebaseAuth.getCurrentUser().getUid());
//    SubscriptionManagement.storeSubscription(PlanName, PlanPrice, PlanDuration, "");

//        Intent i = new Intent(SubscriptionActivity.this, BookingCompleteActivity.class);
//        i.putExtra("From", "sub");
//        startActivity(i);
//        return;
    }

    private void saveData(String qrCodeUrl) {
        spin_kit.setVisibility(VISIBLE);
        SubscriptionModel model = new SubscriptionModel(PlanName, StartDate, EndDate, PlanPrice, PlanStatus, firebaseAuth.getCurrentUser().getUid());
        String QRCodeData = QRCodeManagement.convertBookingToJson(model);
        // Generate QR Code Bitmap
        Bitmap qrBitmap = QRCodeManagement.generateQRCodeBitmap(QRCodeData);
        uploadQRCodeToFirebase(qrBitmap, firebaseAuth.getCurrentUser().getUid());
        SubscriptionManagement.storeSubscription(PlanName, PlanPrice, PlanDuration, qrCodeUrl);

        spin_kit.setVisibility(GONE);
//        startActivity(new Intent(SubscriptionActivity.this, SuccessfulPaymentActivity.class));
//       finish();
//        Intent i = new Intent(SubscriptionActivity.this, BookingCompleteActivity.class);
//        i.putExtra("From", "sub");
//        startActivity(i);
//        return;
    }


    private void uploadQRCodeToFirebase(Bitmap qrBitmap, String bookingId) {

        StorageReference qrRef = storageReference.child("qrcodes/" + bookingId + ".png");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] qrBytes = baos.toByteArray();

        UploadTask uploadTask = qrRef.putBytes(qrBytes);
        uploadTask.addOnSuccessListener(taskSnapshot -> qrRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String qrCodeUrl = uri.toString();
//            storeBookingDataToFirestore(bookingId, qrCodeUrl);
            saveData(qrCodeUrl);
        })).addOnFailureListener(e ->
                Toasty.error(SubscriptionActivity.this, "Failed to upload QR Code", Toasty.LENGTH_SHORT).show()
        );
    }
}