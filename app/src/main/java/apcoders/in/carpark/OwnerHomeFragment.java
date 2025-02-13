package apcoders.in.carpark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import apcoders.in.carpark.Utils.FetchUserData;


public class OwnerHomeFragment extends Fragment {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FirebaseAuth auth;
    TextView welcom;
    CardView cardOwnerUsernameTicket;

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
        SharedPreferences sharedPreferences;

        if (user == null) {
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        }

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        boolean isProfileSaved = sharedPreferences.getBoolean("profile_completed", false);
        cardOwnerUsernameTicket = view.findViewById(R.id.card_owner_username_ticket_create);

        // Check if the profile is already completed
        if (isProfileSaved) {
            cardOwnerUsernameTicket.setVisibility(View.GONE);  // Hide the CardView
        }

        // Open Profile Activity on click
        cardOwnerUsernameTicket.setOnClickListener(v -> {
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


         sharedPreferences =requireActivity().getSharedPreferences("share_prefs", Context.MODE_PRIVATE);
        String userType = sharedPreferences.getString("UserType", "Normal User");
        Log.d("TAG", "onCreate: UserType"+userType);
//        if (userType.equals("Normal User")) {
//            FetchUserData.FetchNormalUserData(new FetchUserData.GetNormalUserData() {
//                @Override
//                public void onCallback(NormalUserModel normalUserModel) {
//                    if (normalUserModel != null) {
//                        Log.d("TAG", "onCallback: " + normalUserModel.getUserFulName() + normalUserModel.getEmail());
//                        TextView usernameTextView = headerView.findViewById(R.id.menu_username);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("UserFullName", normalUserModel.getUserFulName());
//                        editor.apply();
//                        editor.commit();
//                        usernameTextView.setText(normalUserModel.getUserFulName());
//                        welcom.setText("Welcome "+normalUserModel.getUserFulName()+" !");
//                        TextView emailTextView = headerView.findViewById(R.id.menu_email);
//                        emailTextView.setText(normalUserModel.getEmail());
//
//                    } else {
//                        Toast.makeText(requireContext(),"Signed Out",Toast.LENGTH_SHORT).show();
//                        firebaseAuth.signOut();
//                        startActivity(new Intent(requireActivity(), LoginActivity.class));
//                        requireActivity().finish();
//                    }
//                }
//            });
//        } else if (userType.equals("")) {
//            FetchUserData.FetchAuthorityData(new FetchUserData.GetAuthorityData() {
//                @Override
//                public void onCallback(AuthorityModel authorityModel) {
//                    if (authorityModel != null) {
//                        TextView usernameTextView = headerView.findViewById(R.id.menu_username);
//                        usernameTextView.setText(authorityModel.getUserFulName());
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("UserFullName", authorityModel.getUserFulName());
//                        editor.apply();
//                        editor.commit();
//                        welcom.setText("Welcome "+authorityModel.getUserFulName()+" !");
//                        TextView emailTextView = headerView.findViewById(R.id.menu_email);
//                        emailTextView.setText(authorityModel.getEmail());
//                    } else {
//                        Toast.makeText(requireContext(),"Signed Out",Toast.LENGTH_SHORT).show();
//                        firebaseAuth.signOut();
//                        startActivity(new Intent(requireActivity(), LoginActivity.class));
//                        requireActivity().finish();
//                    }
//                }
//            });
//        } else {
//            firebaseAuth.signOut();
//            startActivity(new Intent(requireActivity(), LoginActivity.class));
//            requireActivity().finish();
//        }
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
                    intent.putExtra(Intent.EXTRA_TEXT,Body);
                    intent.putExtra(Intent.EXTRA_TEXT,Sub);
                    startActivity(Intent.createChooser(intent,"Share using"));


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
}