package apcoders.in.carpark.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import apcoders.in.carpark.models.BookingDetailsModel;

public class BookingManagement {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void fetchBookingDetails(String bookingId, BookingCallback callback) {
        db.collection("Bookings").whereEqualTo("bookingId", bookingId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    BookingDetailsModel booking = task.getResult().getDocuments().get(0).toObject(BookingDetailsModel.class);
                    callback.onSuccess(booking);
                } else {
                    callback.onFailure("Booking not found or error occurred.");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
        });
    }

    public static void fetchUserBookings(UserBookingsCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Bookings").whereEqualTo("userId", userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<BookingDetailsModel> bookings = task.getResult().toObjects(BookingDetailsModel.class);
                        callback.onSuccess(bookings);
                    } else {
                        callback.onFailure("Failed to retrieve bookings.");
                    }
                }).addOnFailureListener(e -> Log.d("TAG", "onFailure: " + e.getMessage()));
    }

    public static void fetchActiveSession(BookingCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("Bookings").whereEqualTo("userId", userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (BookingDetailsModel area : task.getResult().toObjects(BookingDetailsModel.class)) {
                            if (area.getStatus().equals("CHECK_IN")) {
                                callback.onSuccess(area);
                                break;
                            }
                        }
                        for (BookingDetailsModel area : task.getResult().toObjects(BookingDetailsModel.class)) {
                            if (area.getStatus().equals("BOOKED")) {
                                callback.onSuccess(area);
                                break;
                            }
                        }
//                        List<BookingDetailsModel> bookings = task.getResult().toObjects(BookingDetailsModel.class);
//                        callback.onSuccess(bookings);
                    } else {
                        callback.onFailure("Failed to retrieve bookings.");
                    }
                }).addOnFailureListener(e -> Log.d("TAG", "onFailure: " + e.getMessage()));
    }

    public interface BookingCallback {
        void onSuccess(BookingDetailsModel booking);

        void onFailure(String errorMessage);
    }


    public interface UserBookingsCallback {
        void onSuccess(List<BookingDetailsModel> bookings);

        void onFailure(String errorMessage);
    }
}
