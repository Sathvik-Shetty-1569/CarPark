package apcoders.in.carpark.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import apcoders.in.carpark.models.UserModel;


public class FetchUserData {

    public interface GetNormalUserData {
        public void onCallback(UserModel userModel);
    }



    public FetchUserData() {

    }

    public static void FetchNormalUserData(GetNormalUserData GetNormalUserData) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        Log.d("TAG", "FetchNormalUserData: " + uid);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build());
        CollectionReference collectionReference = firebaseFirestore.collection("Users");

        collectionReference.whereEqualTo("userid", uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    UserModel userModel = null;
                    for (int i = 0; i < task.getResult().size(); i++) {
                        userModel = task.getResult().toObjects(UserModel.class).get(i);
                    }
                    GetNormalUserData.onCallback(userModel);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: "+e.getMessage());
                UserModel userModel = new UserModel(null,null,null,null,null);
                GetNormalUserData.onCallback(null);
            }
        });
    }

}