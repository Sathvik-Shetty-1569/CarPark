package apcoders.in.carpark.fragments;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import apcoders.in.carpark.LoginActivity;
import apcoders.in.carpark.R;
import apcoders.in.carpark.SuccessfulPaymentActivity;
import apcoders.in.carpark.Utils.WalletManagement;
import apcoders.in.carpark.WalletActivity;
import apcoders.in.carpark.models.WalletTransaction;
import es.dmoral.toasty.Toasty;

public class SearchFragment extends Fragment implements PaymentResultListener {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    Button makePayments_Btn;
    double Amount = 100;
    TextView walletBalance;
    EditText addToWalletAmount;
    RecyclerView walletRecyclerView;
    String UserId;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wallet, container, false);
        
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserId = firebaseAuth.getCurrentUser().getUid();

        walletRecyclerView = view.findViewById(R.id.walletRecyclerView);
        walletBalance = view.findViewById(R.id.walletBalance);
        makePayments_Btn = view.findViewById(R.id.makePayments_Btn);
        addToWalletAmount = view.findViewById(R.id.addToWalletAmount);
        makePayments_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amount = Double.parseDouble(addToWalletAmount.getText().toString());
                if (Amount > 100) {
                    startPayment(Amount);
                } else {
                    Toasty.info(requireContext(), "Minimum 100 Required", Toasty.LENGTH_LONG).show();
                }
            }
        });

        islogin();
        showWalletHistory();
        Checkout.preload(requireContext());

        WalletManagement.getBalance(UserId, new WalletManagement.OnBalanceRetrievedListener() {
            @Override
            public void onBalanceRetrieved(Double balance) {
                walletBalance.setText("Balance : " + balance);
//                userBalance = balance;
                makePayments_Btn.setActivated(true);
            }
        });
        return view;

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
            checkout.open(requireActivity(), options);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onPaymentSuccess(String PaymentId) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        Toasty.success(requireContext(), "Payment Successful", Toast.LENGTH_SHORT).show();
        WalletManagement.creditToWallet(firebaseAuth.getCurrentUser().getUid(), PaymentId, Amount, "Added To Wallet" + new Date().toLocaleString().substring(0, 15));
        requireActivity().startActivity(new Intent(requireContext(), SuccessfulPaymentActivity.class));
        requireActivity().finish();
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toasty.error(requireContext(), "Payment Canceled ", Toast.LENGTH_SHORT).show();
        WalletManagement.creditToWallet(firebaseAuth.getCurrentUser().getUid(), "Error Payment Checking", Amount, "Added To Wallet " + new Date().toLocaleString().substring(0, 13));
        startActivity(new Intent(requireContext(), SuccessfulPaymentActivity.class));
        requireActivity().finish();
    }

    private void islogin() {
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(requireContext(), LoginActivity.class));
        }
    }

    private void showWalletHistory() {
        walletRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
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