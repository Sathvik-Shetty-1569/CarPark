package apcoders.in.carpark.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import apcoders.in.carpark.models.ParkingInfo;


public class CartManagement {
    public interface GetProductDataByIdCallback {
        void onCallback(List<ParkingInfo> ProductDataList);
    }

    public CartManagement() {

    }

    @SuppressLint("MutatingSharedPrefs")
    public static void AddToCart(Context context, String ProductId) {
        SharedPreferences sp = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Get the current products in cart
        Set<String> products_in_cart = sp.getStringSet("ids", null);

        // Create a new set to avoid modifying the original returned set
        if (products_in_cart != null) {
            products_in_cart = new HashSet<>(products_in_cart);
        } else {
            products_in_cart = new HashSet<>();
        }

        // Add the product to the set
        products_in_cart.add(ProductId);

        // Save the updated set back to SharedPreferences
        editor.putStringSet("ids", products_in_cart);
        editor.apply();  // Apply changes asynchronously

        // Log the updated cart
        Log.d("AddToCart", "All Products In Cart After Adding: " + products_in_cart.toString());
    }


    @SuppressLint("MutatingSharedPrefs")
    public static Set<String> getCartProductIds(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        Set<String> products_in_cart = sp.getStringSet("ids", null);
        if (products_in_cart != null) {
            Log.d("getCartProductIds", "All Products In Cart : " + products_in_cart);
            return products_in_cart;
        }

        return null;
    }

    public static void GetProductDataById(Context context, Set<String> ProductIds, final GetProductDataByIdCallback getProductDataByIdCallback) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        CollectionReference collectionReference = firebaseFirestore.collection("ParkingInfo");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ParkingInfo> ProductDataList = new ArrayList<>();
                    if (!task.getResult().getDocuments().isEmpty()) {

                        for (DocumentSnapshot documentSnapshot : task.getResult()) {
                            if (ProductIds.contains(documentSnapshot.get("productId"))) {
                                ProductDataList = task.getResult().toObjects(ParkingInfo.class);
                            }
                        }
                        Log.d("TAG", "onComplete: products data fetched successfully" + ProductDataList.size());
                        // Pass userData to the callback
                        getProductDataByIdCallback.onCallback(ProductDataList);
                    } else {
                        firebaseAuth.signOut();
                        getProductDataByIdCallback.onCallback(ProductDataList);
                    }
                } else {
                    Log.e("TAG", "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Failed to fetch user data: " + e.getMessage());
            }
        });
    }

    @SuppressLint("MutatingSharedPrefs")
    public static void RemoveProductById(Context context, String ProductId) {
        SharedPreferences sp = context.getSharedPreferences("Cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // Get the current products in cart
        Set<String> products_in_cart = sp.getStringSet("ids", null);

        // Make a new HashSet to avoid modifying the original returned set
        if (products_in_cart != null) {
            Set<String> ProductList = new HashSet<>(products_in_cart);
            try {
                // Remove the product by its ID
                ProductList.remove(ProductId);
            } catch (Exception e) {
                Log.d("RemoveProductById: ", e.toString());
            }

            // Save the updated list back to SharedPreferences
            editor.putStringSet("ids", ProductList);
        } else {
            Log.d("RemoveProductById: ", "Product list is empty or null.");
        }

        // Commit changes asynchronously
        editor.apply();

        // Log the products in the cart after removal
        Log.d("After Remove", "All Products In Cart After Removal: " + sp.getStringSet("ids", new HashSet<>()).toString());
    }

}
