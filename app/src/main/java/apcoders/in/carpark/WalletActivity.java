package apcoders.in.carpark;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import apcoders.in.carpark.Adapter.WalletRecyclerViewAdapter;
import apcoders.in.carpark.Utils.WalletManagement;
import apcoders.in.carpark.models.WalletTransaction;
import es.dmoral.toasty.Toasty;

public class WalletActivity extends AppCompatActivity implements PaymentResultListener {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Button makePayments_Btn;
    double Amount = 100;
    TextView walletBalance;
    EditText addToWalletAmount;
    RecyclerView walletRecyclerView;
    String UserId;

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

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        walletRecyclerView = findViewById(R.id.walletRecyclerView);
        walletBalance = findViewById(R.id.walletBalance);
        makePayments_Btn = findViewById(R.id.makePayments_Btn);
        addToWalletAmount = findViewById(R.id.addToWalletAmount);
        makePayments_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amount = Double.parseDouble(addToWalletAmount.getText().toString());
                if (Amount > 100) {
                    startPayment(Amount);
                } else {
                    Toasty.info(WalletActivity.this, "Minimum 100 Required", Toasty.LENGTH_LONG).show();
                }
            }
        });

        islogin();
        showWalletHistory();
        Checkout.preload(this);

        WalletManagement.getBalance(UserId, new WalletManagement.OnBalanceRetrievedListener() {
            @Override
            public void onBalanceRetrieved(Double balance) {
                walletBalance.setText("Balance : " + balance);
//                userBalance = balance;
                makePayments_Btn.setActivated(true);
            }
        });
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
        WalletManagement.creditToWallet(firebaseAuth.getCurrentUser().getUid(), PaymentId, Amount, "Added To Wallet" + new Date().toLocaleString().substring(0, 15));
        startActivity(new Intent(WalletActivity.this, SuccessfulPaymentActivity.class));
        finish();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toasty.error(WalletActivity.this, "Payment Canceled ", Toast.LENGTH_SHORT).show();
        WalletManagement.creditToWallet(firebaseAuth.getCurrentUser().getUid(), "Error Payment Checking", Amount, "Added To Wallet " + new Date().toLocaleString().substring(0, 13));
        startActivity(new Intent(WalletActivity.this, SuccessfulPaymentActivity.class));
        finish();
    }

    private void islogin() {
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(WalletActivity.this, LoginActivity.class));
        }
    }

    private void showWalletHistory() {
        walletRecyclerView.setLayoutManager(new LinearLayoutManager(WalletActivity.this));
        WalletManagement.getTransactions(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), new WalletManagement.OnTransactionsRetrievedListener() {
            @Override
            public void onTransactionsRetrieved(ArrayList<WalletTransaction> walletTransactions) {
                if (walletTransactions != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                        walletTransactions = (ArrayList<WalletTransaction>) walletTransactions.reversed();
                    }
                    Log.d("TAG", "onWithdrawalRequestsFetched: " + walletTransactions.size());
//                    request_layout.setVisibility(View.VISIBLE);
                    WalletRecyclerViewAdapter adapter = new WalletRecyclerViewAdapter(walletTransactions);
                    walletRecyclerView.setAdapter(adapter);
                }
            }
        });

    }
}