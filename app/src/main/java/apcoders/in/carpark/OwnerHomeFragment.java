package apcoders.in.carpark;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import apcoders.in.carpark.Utils.FetchUserData;
import apcoders.in.carpark.Utils.ParkingAreaManagement;
import apcoders.in.carpark.models.ParkingInfo;
import apcoders.in.carpark.models.UserModel;


public class OwnerHomeFragment extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FirebaseAuth auth;
    TextView welcom;
    CardView cardOwnerUsernameTicketcreate;
    private SharedPreferences sharedPreferences;
    private DatabaseReference databaseReference;
    TextView parking;
    LinearLayout Ownerusernameticket;
    TextView address;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_owner_home_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        welcom = view.findViewById(R.id.Welcomtitle);
        CardView cardNotification = view.findViewById(R.id.cardNotification);
        CardView cardRides = view.findViewById(R.id.CardViewRides);
        CardView cardHistory = view.findViewById(R.id.cardhistory);
        CardView cardPolice = view.findViewById(R.id.cardpolice);
        parking = view.findViewById(R.id.textview_parking);
        address = view.findViewById(R.id.textview_address);
        databaseReference = FirebaseDatabase.getInstance().getReference("parking_areas");
        TextView username = view.findViewById(R.id.textview_username);
        Ownerusernameticket = view.findViewById(R.id.Ownerusernameticket);
        fetchParkingDetails();

        if (user == null) {
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        }
        cardOwnerUsernameTicketcreate = view.findViewById(R.id.card_owner_username_ticket_create);


        // Check if the profile is already completed
        // Open Profile Activity on click
        cardOwnerUsernameTicketcreate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OwnerProfileActivity.class);
            startActivity(intent);
        });


        cardRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), IncomingRides.class));
            }
        });

        cardRides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), HistoryActivity.class));
            }
        });

        DrawerLayout drawerLayout;
        NavigationView navigationView;
        drawerLayout = view.findViewById(R.id.drawerlayout);
        navigationView = view.findViewById(R.id.navigation_view);
        ImageButton buttondrawer = view.findViewById(R.id.buttonDrawer);
        View headerView = navigationView.getHeaderView(0);


        FetchUserData.FetchNormalUserData(new FetchUserData.GetNormalUserData() {
            @Override
            public void onCallback(UserModel userModel) {
                if (userModel != null) {
                    Log.d("TAG", "onCallback: " + userModel.getUserFulName() + userModel.getEmail());
                    TextView usernameTextView = headerView.findViewById(R.id.menu_username);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("UserFullName", userModel.getUserFulName());
//                    editor.apply();
//                    editor.commit();
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
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("share_prefs", Context.MODE_PRIVATE);
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

    private BroadcastReceiver profileSavedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("com.example.PROFILE_SAVED".equals(intent.getAction())) {
                Log.d("OwnerHomeFragment", "Broadcast received: PROFILE_SAVED");
                SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                boolean isProfileSaved = sharedPreferences.getBoolean("profile_completed", false);

                editor.putBoolean("profile_completed", true);
                editor.apply();

                if (getView() != null) {
                    CardView cardView = getView().findViewById(R.id.card_owner_username_ticket_create);
                    if (cardView != null) {
                        cardView.setVisibility(GONE);
                    }
                }
            }
        }

    };


    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("com.example.PROFILE_SAVED");
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(profileSavedReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(profileSavedReceiver);
    }

    private void fetchParkingDetails() {
//        databaseReference.orderByKey().limitToLast(1) // Fetch the latest parking area
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            String name = snapshot.child("name").getValue(String.class);
//                            double latitude = snapshot.child("latitude").getValue(Double.class);
//                            double longitude = snapshot.child("longitude").getValue(Double.class);
//
//                            if (name != null) {
//                                cardOwnerUsernameTicketcreate.setVisibility(GONE);
//                                Ownerusernameticket.setVisibility(VISIBLE);
//                                parking.setText("Parking Name: " + name);
//                                address.setText("Location: " + latitude + ", " + longitude);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e("Firebase", "Error fetching data", databaseError.toException());
//                        Toast.makeText(requireActivity(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
//                    }
//                });

        ParkingAreaManagement.fetchParkingInfo(new ParkingAreaManagement.FirestoreDataCallback() {
            @Override
            public void onDataFetched(List<ParkingInfo> parkingList) {
                ParkingInfo CurrentAreaInfo = new ParkingInfo();
                for (ParkingInfo area : parkingList) {
                    if (area.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        CurrentAreaInfo = area;
                        break;
                    }
                }
                if (CurrentAreaInfo != null) {
                    cardOwnerUsernameTicketcreate.setVisibility(GONE);
                    Ownerusernameticket.setVisibility(VISIBLE);
                    parking.setText("Parking Name: " + CurrentAreaInfo.getName());
                    address.setText("Location: " + CurrentAreaInfo.getLocaddress().substring(0, 25) + "...");
                }

            }

            @Override
            public void onFailure(String error) {

            }
        });
    }


}