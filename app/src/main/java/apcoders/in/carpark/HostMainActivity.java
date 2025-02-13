package apcoders.in.carpark;

import android.os.Bundle;
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

import apcoders.in.carpark.fragments.HomeFragment;

public class HostMainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_host_main);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            floatingActionButton = findViewById(R.id.Map);
            frameLayout = findViewById(R.id.frame_layout);

            loadFragment(new HomeFragment(), false);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();

                    if (itemId == R.id.home) {
                        loadFragment(new OwnerHomeFragment(), false);}
//                     else if (itemId == R.id.wallet) {
//                        loadFragment(new SearchFragment(), false);
//                    }
//                    else if (itemId == R.id.addlocation) {
//                        loadFragment(new BookingFragment(), false);
//                    } else {
//                        loadFragment(new ProfileFragment(), false);
//                    }
                    return true;
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFragment(new apcoders.in.carpark.MapFragment(),false);
                }
            });

            if (savedInstanceState == null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, new HomeFragment()) // Replace with the container ID and initial fragment
                        .commit();
            }



        }

        public BottomNavigationView getBottomNavigationView() {
            return bottomNavigationView;
        }

        private void loadFragment(Fragment fragment, boolean isAppInitialized) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.frame_layout, fragment);

            fragmentTransaction.commit();
        }
}
