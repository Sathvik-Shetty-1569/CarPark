package apcoders.in.carpark;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import apcoders.in.carpark.R;
import apcoders.in.carpark.fragments.OwnerParkMap;

public class OwnerProfileActivity    extends AppCompatActivity {
    EditText location;
    double lat;
    double log;
    private EditText parkingName, parkingSlots, parkingAmount, ownerLocation;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_profile);


        add = findViewById(R.id.Save_the_profile);
        location = findViewById(R.id.ownerlocation);
        parkingName = findViewById(R.id.ParkingName);
        parkingSlots = findViewById(R.id.ParkingSlots);
        parkingAmount = findViewById(R.id.Parkingamount);

location.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        findViewById(R.id.frame_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.linearLayout).setVisibility(View.GONE); // Hide main layout

        OwnerParkMap ownerParkMap = new OwnerParkMap();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, ownerParkMap)
                .addToBackStack(null)
                .commit();


    }
});

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            if (bundle != null) {
                String selectedLocation = bundle.getString("selected_location", "");
                lat = bundle.getDouble("selected_latitude",0.0);
                log = bundle.getDouble("selected_longitude",0.0);
                if (lat == 0.0 || log == 0.0) {
                    Toast.makeText(this, "Invalid location received", Toast.LENGTH_SHORT).show();
                    return;
                }
                location.setText(selectedLocation);

            }
        });

add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (validateInputs()) {

            saveProfile();
            Toast.makeText(OwnerProfileActivity.this, "Saved the Owner Profile Details", Toast.LENGTH_SHORT).show();

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

        public void saveProfile() {
            // Assume all required profile details are entered
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("profile_completed", true);
            editor.apply();
            boolean isProfileSaved = sharedPreferences.getBoolean("profile_completed", false);
            Log.d("OwnerProfileActivity", "Profile Completed Saved: " + isProfileSaved);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("parking_areas"); // Root node

            String name = parkingName.getText().toString().trim();
            String slots = parkingSlots.getText().toString().trim();
            String amount = parkingAmount.getText().toString().trim();
            String loc = location.getText().toString().trim();

            String parkingId = reference.push().getKey(); // Generate a unique key
            Map<String, Object> parkingData = new HashMap<>();
            parkingData.put("name", name);
            parkingData.put("slots", Integer.parseInt(slots));
            parkingData.put("amount", amount);
            parkingData.put("latitude",lat);
            parkingData.put("longitude",log);

            assert parkingId != null;
            reference.child(parkingId).setValue(parkingData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("OwnerProfileActivity", "Broadcasting PROFILE_SAVED event");
                        Toast.makeText(OwnerProfileActivity.this, "Parking details saved!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent("com.example.PROFILE_SAVED");
                        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                    })
                    .addOnFailureListener(e -> {
                        Log.e("OwnerProfileActivity", "Failed to save parking details", e);
                        Toast.makeText(OwnerProfileActivity.this, "Failed to save parking details", Toast.LENGTH_SHORT).show();
                    });
            // Go back to previous activity
            finish();
        }

    private boolean validateInputs() {
        if (isEmpty(parkingName) || isEmpty(parkingSlots) || isEmpty(parkingAmount) || isEmpty(ownerLocation)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }


}
