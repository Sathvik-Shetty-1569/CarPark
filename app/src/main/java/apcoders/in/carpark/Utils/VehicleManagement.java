package apcoders.in.carpark.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import apcoders.in.carpark.models.VehicleModel;

public class VehicleManagement {
    public interface isVehicleAdded {
        public void onCallback(boolean isVehicleAdded);
    }

    public interface isVehicleDeleted {
        public void onCallback(boolean isVehicleDeleted);
    }

    public interface onVehicleListReceived {
        void onCallback(ArrayList<VehicleModel> vehicleList);
    }

    private static final String COLLECTION_NAME = "Vehicles";
    private static final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "VehicleManager";

    public VehicleManagement() {

    }

    public static void AddVehicle(String vehicleNumber, String vehicleType, String vehicleBrand, String vehicleModel, String vehicleColor, isVehicleAdded isVehicleAdded) {
        String VehicleId = db.collection(COLLECTION_NAME).document().getId();
        VehicleModel newVehicle = new VehicleModel(VehicleId, userId, vehicleNumber, vehicleType, vehicleBrand, vehicleModel, vehicleColor);

        db.collection(COLLECTION_NAME).document(VehicleId).set(newVehicle)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Vehicle added successfully.");
                        isVehicleAdded.onCallback(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Vehicle Not Added.");
                        isVehicleAdded.onCallback(false);
                    }
                });
    }

    public static void DeleteVehicle(String vehicleNumber, isVehicleDeleted isVehicleDeleted) {
    }

    public static void getAllVehicles(onVehicleListReceived callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<VehicleModel> vehicleList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            VehicleModel vehicle = document.toObject(VehicleModel.class);
                            vehicleList.add(vehicle);
                        }
                        Log.d(TAG, "onSuccess: "+vehicleList.size());
                        callback.onCallback(vehicleList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Failed to retrieve vehicles.", e);
                        callback.onCallback(new ArrayList<>());
                    }
                });
    }
}
