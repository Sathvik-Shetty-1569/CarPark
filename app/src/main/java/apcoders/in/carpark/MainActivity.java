package apcoders.in.carpark;
import apcoders.in.carpark.models.AuthorityModel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import apcoders.in.carpark.Utils.FetchUserData;
import apcoders.in.carpark.models.NormalUserModel;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }



        DrawerLayout drawerLayout;
        NavigationView navigationView;
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        ImageButton buttondrawer = findViewById(R.id.buttonDrawer);
        View headerView = navigationView.getHeaderView(0);


        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
        String userType = sharedPreferences.getString("UserType", "Normal User");
        if (userType.equals("Normal User")) {
            FetchUserData.FetchNormalUserData(new FetchUserData.GetNormalUserData() {
                @Override
                public void onCallback(NormalUserModel normalUserModel) {
                    if (normalUserModel != null) {
                        Log.d("TAG", "onCallback: " + normalUserModel.getUserFulName() + normalUserModel.getEmail());
                        TextView usernameTextView = headerView.findViewById(R.id.menu_username);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserFullName", normalUserModel.getUserFulName());
                        editor.apply();
                        editor.commit();
                        usernameTextView.setText(normalUserModel.getUserFulName());
                        TextView emailTextView = headerView.findViewById(R.id.menu_email);
                        emailTextView.setText(normalUserModel.getEmail());

                    } else {
                        Toast.makeText(MainActivity.this,"Signed Out",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }
                }
            });
        } else if (userType.equals("Authorities")) {
            FetchUserData.FetchAuthorityData(new FetchUserData.GetAuthorityData() {
                @Override
                public void onCallback(AuthorityModel authorityModel) {
                    if (authorityModel != null) {
                        TextView usernameTextView = headerView.findViewById(R.id.menu_username);
                        usernameTextView.setText(authorityModel.getUserFulName());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("UserFullName", authorityModel.getUserFulName());
                        editor.apply();
                        editor.commit();
                        TextView emailTextView = headerView.findViewById(R.id.menu_email);
                        emailTextView.setText(authorityModel.getEmail());
                    } else {
                        Toast.makeText(MainActivity.this,"Signed Out",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }
                }
            });
        } else {
            firebaseAuth.signOut();
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
        buttondrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navigation_profile) {
                    Toast.makeText(MainActivity.this, "Manage Profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Profile_Activity.class));
                }

                else if (itemId == R.id.navigation_about) {
                    Toast.makeText(MainActivity.this, "About Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, About.class));
                } else if (itemId == R.id.navigation_share) {
                    Toast.makeText(MainActivity.this, "Share Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String Body = "Download this App";
                    String Sub = "https://play.google.com";
                    intent.putExtra(Intent.EXTRA_TEXT,Body);
                    intent.putExtra(Intent.EXTRA_TEXT,Sub);
                    startActivity(Intent.createChooser(intent,"Share using"));


                } else if (itemId == R.id.navigation_logout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();

                    Toast.makeText(MainActivity.this, "Logout Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,Login.class));
                    finish(); // Close HomeActivity

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}