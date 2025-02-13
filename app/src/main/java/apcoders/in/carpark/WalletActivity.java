package apcoders.in.carpark;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class WalletActivity extends AppCompatActivity implements PaymentResultListener {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Button makePayments_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wallet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        makePayments_Btn = findViewById(R.id.makePayments_Btn);

        makePayments_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment(200.0);
            }
        });

        islogin();
        Checkout.preload(this);
    }

    private void startPayment(double totalAmount) {
        try {
            JSONObject options = new JSONObject();
            options.put("name", "CarPark");
            options.put("description", "Add To Cart");
//            options.put("image", "https://example.com/your-logo.png");
            options.put("currency", "INR");
            options.put("amount", totalAmount * 100); // Convert to paisa

            Checkout checkout = new Checkout();
            checkout.open(WalletActivity.this, options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onPaymentSuccess(String PaymentId) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        Toasty.success(WalletActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onPaymentError(int code, String response) {
        Toasty.error(WalletActivity.this, "Payment Canceled ", Toast.LENGTH_SHORT).show();
    }

    private void islogin() {
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(WalletActivity.this, LoginActivity.class));
        }
    }
}