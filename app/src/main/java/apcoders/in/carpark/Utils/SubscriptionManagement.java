package apcoders.in.carpark.Utils;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import apcoders.in.carpark.models.SubscriptionModel;

public class SubscriptionManagement {
    private static final String TAG = "FirestoreHelper";


    public SubscriptionManagement() {

    }

    public static void storeSubscription(String plan, double price,int days,String qrCodeUrl) {
        FirebaseFirestore db;
        FirebaseAuth auth;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        // Get current date and expiry date (1 month later)
        Date startDate = new Date();
        long expiryTime = startDate.getTime() + (days * 24 * 60 * 60 * 1000L); // Add days in milliseconds

        Date expiryDate = new Date(expiryTime);

        SubscriptionModel subscription = new SubscriptionModel(plan, startDate, expiryDate, price, "Active", userId,qrCodeUrl);

        db.collection("subscriptions").document(userId)
                .set(subscription)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Subscription stored successfully"))
                .addOnFailureListener(e -> Log.e(TAG, "Error storing subscription", e));
    }

    private static String calculateExpiryDate(int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date today = new Date();
        long expiryTime = today.getTime() + (days * 24 * 60 * 60 * 1000L); // Add days in milliseconds
        return sdf.format(new Date(expiryTime));
    }
}
