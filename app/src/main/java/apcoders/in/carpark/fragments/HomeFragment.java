package apcoders.in.carpark.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import apcoders.in.carpark.AboutActivity;
import apcoders.in.carpark.Adapter.ViewpagerImageSliderAdapter;
import apcoders.in.carpark.LoginActivity;
import apcoders.in.carpark.R;
import apcoders.in.carpark.Utils.FetchUserData;
import apcoders.in.carpark.models.UserModel;

public class HomeFragment extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FirebaseAuth auth;
    TextView welcom;
    Button endSessionButton;
    LinearLayout activeSessionContainer;
    TextView sessionTimer;
    Handler handler = new Handler();
    int seconds = 0;
    boolean isRunning = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        welcom = view.findViewById(R.id.Welcomtitle);
        TextView username = view.findViewById(R.id.textview_username);

        if (user == null) {
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        }

        activeSessionContainer = view.findViewById(R.id.active_session_container);
        endSessionButton = view.findViewById(R.id.end_session_button);
        sessionTimer = view.findViewById(R.id.session_timer);

        startSession(); // Call this when a parking session starts

        endSessionButton.setOnClickListener(v -> endSession());


        DrawerLayout drawerLayout;
        NavigationView navigationView;
        drawerLayout = view.findViewById(R.id.drawerlayout);
        navigationView = view.findViewById(R.id.navigation_view);
        ImageButton buttondrawer = view.findViewById(R.id.buttonDrawer);
        View headerView = navigationView.getHeaderView(0);

        banner_recyclerview_setup(view);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("share_prefs", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("UserType", "User");
        Log.d("TAG", "onCreate: UserType" + userType);

        FetchUserData.FetchNormalUserData(new FetchUserData.GetNormalUserData() {
            @Override
            public void onCallback(UserModel userModel) {
                if (userModel != null) {
                    Log.d("TAG", "onCallback: " + userModel.getUserFulName() + userModel.getEmail());
                    TextView usernameTextView = headerView.findViewById(R.id.menu_username);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("UserFullName", userModel.getUserFulName());
                    editor.apply();
                    editor.commit();
                    String Name = "";
                    if (userModel.getUserFulName().length() < 17) {
                        Name = userModel.getUserFulName();
                    } else {
                        Name = userModel.getUserFulName().substring(0, 17);
                    }

                    username.setText(Name);
                    welcom.setText("CarPark");
                    usernameTextView.setText(userModel.getUserFulName());
                    TextView emailTextView = headerView.findViewById(R.id.menu_email);
                    emailTextView.setText(userModel.getEmail());

                } else {
                    Toast.makeText(requireContext(), "Signed Out", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    startActivity(new Intent(requireActivity(), LoginActivity.class));
                    requireActivity().finish();
                }
            }
        });
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
                if (itemId == R.id.navigation_about) {
                    Toast.makeText(requireContext(), "AboutActivity Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireActivity(), AboutActivity.class));
                } else if (itemId == R.id.navigation_share) {
                    Toast.makeText(requireContext(), "Share Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String Body = "Download this App";
                    String Sub = "https://play.google.com";
                    intent.putExtra(Intent.EXTRA_TEXT, Body);
                    intent.putExtra(Intent.EXTRA_TEXT, Sub);
                    startActivity(Intent.createChooser(intent, "Share using"));


                } else if (itemId == R.id.navigation_logout) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", false);
                    editor.apply();
                    firebaseAuth.signOut();
                    Toast.makeText(requireContext(), "Logout Clicked", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(requireActivity(), LoginActivity.class));
                    requireActivity().finish(); // Close HomeActivity

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        return view;
    }

    private void banner_recyclerview_setup(View view) {
        RecyclerView Banner_recycleView = view.findViewById(R.id.Banner_recycleView);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        Banner_recycleView.setLayoutManager(linearLayoutManager);

        // Array of drawable resource IDs
        List<Uri> imageIds = new ArrayList<>();
        imageIds.add(Uri.parse("android.resource://apcoders.in.carpark/" + R.drawable.banner_1));
        imageIds.add(Uri.parse("android.resource://apcoders.in.carpark/" + R.drawable.banner_2));
        imageIds.add(Uri.parse("android.resource://apcoders.in.carpark/" + R.drawable.banner_3));

        ViewpagerImageSliderAdapter adapter = new ViewpagerImageSliderAdapter(requireContext(), imageIds);
        Banner_recycleView.setAdapter(adapter);

        final int scrollSpeed = 100;   // Scroll Speed in Milliseconds (Increased for slower rate)
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int x = 10;        // Reduced Pixels To Move/Scroll (for smoother transition)
            boolean flag = true;
            int scrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int arraySize = imageIds.size();  // Gets RecyclerView's Adapter's Array Size

            @Override
            public void run() {
                if (scrollPosition < arraySize) {
                    if (scrollPosition == arraySize - 1) {
                        flag = false;
                    } else if (scrollPosition <= 1) {
                        flag = true;
                    }
                    if (!flag) {
                        try {
                            // Delay in Seconds So User Can Completely Read Till Last String
                            TimeUnit.SECONDS.sleep(1);
                            Banner_recycleView.scrollToPosition(0);  // Jumps Back Scroll To Start Point
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // Know The Last Visible Item
                    scrollPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                    Banner_recycleView.smoothScrollBy(x, 0);
                    handler.postDelayed(this, scrollSpeed);  // Adjust delay between scroll movements
                }
            }
        };
        handler.postDelayed(runnable, scrollSpeed); // Start the scrolling with the new speed
    }

    private void startSession() {
        activeSessionContainer.setVisibility(View.VISIBLE);
        isRunning = true;
        runTimer();
    }

    private void endSession() {
        activeSessionContainer.setVisibility(View.GONE);
        isRunning = false;
        seconds = 0;
    }

    private void runTimer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                sessionTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));

                if (isRunning) {
                    seconds++;
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }
}