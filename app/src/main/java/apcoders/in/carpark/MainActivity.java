package apcoders.in.carpark;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import apcoders.in.carpark.fragments.BookingFragment;
import apcoders.in.carpark.fragments.HomeFragment;
import apcoders.in.carpark.fragments.MapFragment;
import apcoders.in.carpark.fragments.ProfileFragment;
import apcoders.in.carpark.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private FloatingActionButton floatingActionButton;
    TextView welcom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Log.d("TAG", "onCreate: ");
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.Map);
        frameLayout = findViewById(R.id.frame_layout);
        checkForLoadingFragment();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    loadFragment(new HomeFragment(), false);
                } else if (itemId == R.id.wallet) {
                    loadFragment(new SearchFragment(), false);
                } else if (itemId == R.id.Settings) {
                    loadFragment(new BookingFragment(), false);
                } else if(itemId == R.id.Profile) {
                    loadFragment(new ProfileFragment(), false);
                }else {

                }
                return true;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new MapFragment(), false);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new HomeFragment()) // Replace with the container ID and initial fragment
                    .commit();
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void checkForLoadingFragment() {
        String data = getIntent().getStringExtra("LoadFragment");
        Log.d("TAG", "checkForLoadingFragment: "+data);
        if ("Location".equals(data)) {
            loadFragment(new MapFragment(), true);
        }else{
            loadFragment(new HomeFragment(), false);
        }
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForLoadingFragment();
    }
}