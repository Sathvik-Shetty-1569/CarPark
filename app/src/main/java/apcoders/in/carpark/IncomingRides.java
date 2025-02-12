package apcoders.in.carpark;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import apcoders.in.carpark.Adapter.RideAdapter;
import apcoders.in.carpark.models.RideModel;

public class IncomingRides extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RideAdapter adapter;
    private List<RideModel> rideList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_incoming_rides);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Sample Data
        rideList = new ArrayList<>();
        rideList.add(new RideModel("John Doe", "A1", "12345", "10:00 AM", "2:00 PM", "VIP Parking"));
        rideList.add(new RideModel("Jane Smith", "B2", "67890", "11:30 AM", "3:30 PM", "Handicap Access"));
        rideList.add(new RideModel("Robert Brown", "C3", "54321", "1:00 PM", "5:00 PM", "None"));
        adapter = new RideAdapter(rideList);
        recyclerView.setAdapter(adapter);
    }
}