package apcoders.in.carpark.Utils;


import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import apcoders.in.carpark.models.ParkingInfo;

public class ParkingAreaManagement {

    // Callback for Firestore operations
    public interface FirestoreCallback {
        void onSuccess(String message);

        void onFailure(String error);
    }

    // Callback for fetching data
    public interface FirestoreDataCallback {
        void onDataFetched(List<ParkingInfo> parkingList);

        void onFailure(String error);
    }

    // Callback for multiple image uploads
    public interface ImageUploadCallback {
        void onSuccess(List<String> imageUrls);

        void onFailure(String error);
    }

    private static final String COLLECTION_NAME = "ParkingInfo";
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final StorageReference storageRef = FirebaseStorage.getInstance().getReference("ParkingImages");

    // Upload ParkingInfo to Firestore
    public static void uploadParkingInfo(ParkingInfo parkingInfo, FirestoreCallback callback) {
        db.collection(COLLECTION_NAME)
                .add(parkingInfo)
                .addOnSuccessListener(documentReference -> callback.onSuccess("Upload successful!"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // Fetch ParkingInfo from Firestore
    public static void fetchParkingInfo(FirestoreDataCallback callback) {
        Log.d("TAG", "fetchParkingInfo: ");
        db.collection(COLLECTION_NAME)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<ParkingInfo> parkingList = new ArrayList<>();
                        Log.d("TAG", "fetchParkingInfo: " + task.getResult().size());
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ParkingInfo parkingInfo = document.toObject(ParkingInfo.class);
                            if (parkingInfo != null) {
                                parkingList.add(parkingInfo);
                            }
                        }
                        Log.d("TAG", "fetchParkingInfo: " + parkingList.size());
                        // Ensure parkingList is not null before calling callback
                        callback.onDataFetched(parkingList);
                    } else {
                        callback.onFailure(task.getException() != null ? task.getException().getMessage() : "Failed to fetch data");
                    }
                });
    }


    public static void uploadImagesToStorage(List<Uri> imageUriList, String userId, ImageUploadCallback callback) {
        List<String> imageUrls = new ArrayList<>();
        int[] uploadedCount = {0};

        for (int i = 0; i < imageUriList.size(); i++) {
            String imageName = "parking_" + userId + "_" + System.currentTimeMillis() + "_" + i + ".jpg";
            StorageReference imageRef = storageRef.child(imageName);

            Uri imageUri = imageUriList.get(i);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                imageUrls.add(uri.toString());
                                uploadedCount[0]++;
                                if (uploadedCount[0] == imageUriList.size()) {
                                    callback.onSuccess(imageUrls);
                                }
                            })
                            .addOnFailureListener(e -> callback.onFailure(e.getMessage())))
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
        }
    }


}
