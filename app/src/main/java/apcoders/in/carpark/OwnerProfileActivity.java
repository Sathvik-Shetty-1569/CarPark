package apcoders.in.carpark;

import static android.content.ContentValues.TAG;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import apcoders.in.carpark.Adapter.ProductImageAdapter;
import apcoders.in.carpark.Utils.FetchUserData;
import apcoders.in.carpark.Utils.ParkingAreaManagement;
import apcoders.in.carpark.fragments.OwnerParkMap;
import apcoders.in.carpark.models.ParkingInfo;
import apcoders.in.carpark.models.UserModel;
import es.dmoral.toasty.Toasty;

public class OwnerProfileActivity extends AppCompatActivity {
    EditText location;
    double lat;
    double log;
    SpinKitView spin_kit;
    List<Uri> selectedImages = new ArrayList<>();
    String selectedLocation;
    private EditText parkingName, parkingSlots, parkingAmount, ownerLocation;
    Button add;
    TextView ownerusernam, owneremail;
    RecyclerView product_images_recyclerView;
    ImageView product_image_add_image;
    LinearLayout product_images_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_profile);
        Log.d(TAG, "onCreate: OwnerProfileActivity");

        spin_kit = findViewById(R.id.spin_kit);
        ownerusernam = findViewById(R.id.ownerusernam);
        owneremail = findViewById(R.id.owneremail);

        add = findViewById(R.id.Save_the_profile);
        location = findViewById(R.id.ownerlocation);
        parkingName = findViewById(R.id.ParkingName);
        parkingSlots = findViewById(R.id.ParkingSlots);
        parkingAmount = findViewById(R.id.Parkingamount);
        product_images_layout = findViewById(R.id.product_images_layout);

        product_image_add_image = findViewById(R.id.product_image_add_image);
        product_images_recyclerView = findViewById(R.id.product_images_recyclerView);
        SelectMultipleProductImages();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.frame_layout).setVisibility(VISIBLE);
                findViewById(R.id.mainLayout).setVisibility(GONE); // Hide main layout
                findViewById(R.id.linearLayout).setVisibility(GONE);
                OwnerParkMap ownerParkMap = new OwnerParkMap();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_layout, ownerParkMap)
                        .addToBackStack(null)
                        .commit();


            }
        });
        FetchUserData.FetchNormalUserData(new FetchUserData.GetNormalUserData() {
            @Override
            public void onCallback(UserModel userModel) {
                if (userModel != null) {
                    ownerusernam.setText(userModel.getUserFulName());
                    owneremail.setText(userModel.getEmail());
                }
            }
        });
        getSupportFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            if (bundle != null) {
                selectedLocation = bundle.getString("selected_location", "");
                lat = bundle.getDouble("selected_latitude", 0.0);
                log = bundle.getDouble("selected_longitude", 0.0);
                if (lat == 0.0 || log == 0.0) {
                    Toast.makeText(this, "Invalid location received", Toast.LENGTH_SHORT).show();
                    return;
                }
                location.setText(selectedLocation);
                findViewById(R.id.mainLayout).setVisibility(VISIBLE); // Hide main layout
                findViewById(R.id.linearLayout).setVisibility(VISIBLE);


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    spin_kit.setVisibility(VISIBLE);
                    UploadProduct();
//                    Toast.makeText(OwnerProfileActivity.this, "Saved the Parking Area Details", Toast.LENGTH_SHORT).show();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout, new OwnerHomeFragment()) // Ensure this is the correct container
                            .addToBackStack(null)
                            .commit();

                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void UploadProduct() {
        spin_kit.setVisibility(VISIBLE);
        add.setActivated(false);

        String parkingname = parkingName.getText().toString().trim();
        String slots = parkingSlots.getText().toString().trim();
        String amount = parkingAmount.getText().toString().trim();
        String loc = location.getText().toString().trim();

        if (TextUtils.isEmpty(parkingname) || TextUtils.isEmpty(slots) || Double.parseDouble(amount) <= 0 || TextUtils.isEmpty(loc)) {
            Toasty.error(OwnerProfileActivity.this, "Please Fill All The Fields", Toast.LENGTH_SHORT).show();
            add.setActivated(true);
            spin_kit.setVisibility(GONE);
        } else {
            if (!selectedImages.isEmpty()) {
                ParkingAreaManagement.uploadImagesToStorage(selectedImages, FirebaseAuth.getInstance().getCurrentUser().getUid(), new ParkingAreaManagement.ImageUploadCallback() {
                    @Override
                    public void onSuccess(List<String> imageUrls) {
                        Log.d("TAG", "onSuccess: " + imageUrls.get(0));
                        ParkingInfo parkingInfo = new ParkingInfo(parkingname, slots, amount, FirebaseAuth.getInstance().getCurrentUser().getUid(), lat, log, selectedLocation, imageUrls);
                        ParkingAreaManagement.uploadParkingInfo(parkingInfo, new ParkingAreaManagement.FirestoreCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toasty.success(OwnerProfileActivity.this, "Parking Area Added Successfully", Toasty.LENGTH_LONG).show();
                                OwnerProfileActivity.super.onBackPressed();
                            }

                            @Override
                            public void onFailure(String error) {
                                Toasty.error(OwnerProfileActivity.this, "Network Error Occur", Toasty.LENGTH_LONG).show();
                                add.setActivated(true);
                                spin_kit.setVisibility(GONE);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String error) {
                        spin_kit.setVisibility(GONE);
                        add.setActivated(true);
                        Toasty.error(OwnerProfileActivity.this, "Network Error Occur", Toasty.LENGTH_LONG).show();
                    }
                });


            } else {
                add.setActivated(true);
                spin_kit.setVisibility(GONE);
                Toasty.error(OwnerProfileActivity.this, "Please Select Product Images", Toast.LENGTH_SHORT).show();
            }
        }


    }


//    public void saveProfile() {
//        // Assume all required profile details are entered
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putBoolean("profile_completed", true);
//        editor.apply();
//        boolean isProfileSaved = sharedPreferences.getBoolean("profile_completed", false);
//        Log.d("OwnerProfileActivity", "Profile Completed Saved: " + isProfileSaved);
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("parking_areas"); // Root node
//
//        String name = parkingName.getText().toString().trim();
//        String slots = parkingSlots.getText().toString().trim();
//        String amount = parkingAmount.getText().toString().trim();
//        String loc = location.getText().toString().trim();
//
//        String parkingId = reference.push().getKey(); // Generate a unique key
//        Map<String, Object> parkingData = new HashMap<>();
//        parkingData.put("name", name);
//        parkingData.put("slots", Integer.parseInt(slots));
//        parkingData.put("amount", amount);
//        parkingData.put("latitude", lat);
//        parkingData.put("longitude", log);
//        parkingData.put("loc", selectedLocation);
//
//        assert parkingId != null;
//        reference.child(parkingId).setValue(parkingData)
//                .addOnSuccessListener(aVoid -> {
//                    Log.d("OwnerProfileActivity", "Broadcasting PROFILE_SAVED event");
//                    Toast.makeText(OwnerProfileActivity.this, "Parking details saved!", Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent("com.example.PROFILE_SAVED");
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("OwnerProfileActivity", "Failed to save parking details", e);
//                    Toast.makeText(OwnerProfileActivity.this, "Failed to save parking details", Toast.LENGTH_SHORT).show();
//                });
//        // Go back to previous activity
//        finish();
//    }

    private void SelectMultipleProductImages() {
        product_images_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallary = new Intent(Intent.ACTION_PICK);
                openGallary.setType("image/*");
                openGallary.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                ImagePickerLauncher.launch(Intent.createChooser(openGallary, "Select Product Images"));
            }
        });
    }

    ActivityResultLauncher<Intent> ImagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult activityResult) {
                    Intent data = activityResult.getData();
                    Log.d("Selected Images :", selectedImages.size() + "");
                    if (activityResult.getResultCode() == RESULT_OK && data != null) {
                        assert activityResult.getData() != null;
                        if (activityResult.getData().getClipData() != null) {
                            int count = Objects.requireNonNull(data.getClipData()).getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                // Add the imageUri to a list and update UI
                                selectedImages.add(imageUri);
                            }
                            Log.d("Selected Images :", selectedImages.size() + "");
                            if (!selectedImages.isEmpty()) {
                                product_image_add_image.setVisibility(GONE);
                                product_images_recyclerView.setVisibility(VISIBLE);
                                ProductImageAdapter adapter = new ProductImageAdapter(OwnerProfileActivity.this, selectedImages);
                                product_images_recyclerView.setLayoutManager(new LinearLayoutManager(OwnerProfileActivity.this, LinearLayoutManager.HORIZONTAL, false));
                                product_images_recyclerView.setAdapter(adapter);
                            } else {
                                product_image_add_image.setVisibility(VISIBLE);
                                product_images_recyclerView.setVisibility(GONE);
                            }
                        }
                    }
                }
            }
    );

    private boolean validateInputs() {
        if (isEmpty(parkingName) || isEmpty(parkingSlots) || isEmpty(parkingAmount) || isEmpty(location)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}
