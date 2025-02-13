package apcoders.in.carpark;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences onboardingsharedPreferences = getSharedPreferences("OnBoarding", MODE_PRIVATE);
                SharedPreferences sharedPreference = getSharedPreferences("share_prefs", MODE_PRIVATE);
                String Usertype = sharedPreference.getString("UserType", "User");
                boolean IsLogging = sharedPreference.getBoolean("isLoggedIn", false);
                boolean IsOnboardingHide = onboardingsharedPreferences.getBoolean("IsOnboardingHide", false);

                if (IsLogging && Usertype.equals("User")) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else if (IsLogging && Usertype.equals("ParkingOwner")) {
                    startActivity(new Intent(SplashActivity.this, HostMainActivity.class));
                    finish();
                } else {
                    if (!IsOnboardingHide) {
                        startActivity(new Intent(SplashActivity.this, OnBoardingActivityOne.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                }

            }
        }, 2000);
    }
}