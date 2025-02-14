package apcoders.in.carpark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import apcoders.in.carpark.Utils.VehicleManagement;
import apcoders.in.carpark.adapters.SeeAllVehiclesAdapter;
import apcoders.in.carpark.models.VehicleModel;

public class SeeAllVehiclesActivity extends AppCompatActivity {
    RecyclerView seeVehiclesRecyclerView;
    FloatingActionButton add_vehicles_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_all_vehicles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        add_vehicles_btn = findViewById(R.id.add_vehicles_btn);
        add_vehicles_btn.setOnClickListener(v -> {
            startActivity(new Intent(SeeAllVehiclesActivity.this, AddVehicleActivity.class));
        });

        seeVehiclesRecyclerView = findViewById(R.id.seeVehiclesRecyclerView);
        seeVehiclesRecyclerView.setHasFixedSize(true);
        seeVehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        VehicleManagement.getAllVehicles(new VehicleManagement.onVehicleListReceived() {
            @Override
            public void onCallback(ArrayList<VehicleModel> vehicleList) {
                if (vehicleList.size() > 0 || vehicleList != null) {
                    SeeAllVehiclesAdapter seeAllVehiclesAdapter = new SeeAllVehiclesAdapter(vehicleList);
                    seeVehiclesRecyclerView.setAdapter(seeAllVehiclesAdapter);
                } else {
                    Log.d("SeeAllVehiclesActivity", "No vehicles found.");
                }
            }
        });
    }
}