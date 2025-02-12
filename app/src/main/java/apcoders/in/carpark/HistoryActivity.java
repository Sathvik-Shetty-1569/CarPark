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

import apcoders.in.carpark.Adapter.SessionAdapter;
import apcoders.in.carpark.models.Session;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("John Doe", "Parking Lot A", "12 Feb 2025", "2h", "200 Rs", "30 min", true));  // Active
        sessions.add(new Session("Jane Smith", "Parking Lot B", "10 Feb 2025", "3h", "300 Rs", "0 min", false)); // Completed

        SessionAdapter adapter = new SessionAdapter(this, sessions);
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}