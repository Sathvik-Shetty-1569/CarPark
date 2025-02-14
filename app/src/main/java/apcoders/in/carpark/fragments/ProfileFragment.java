package apcoders.in.carpark.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

import apcoders.in.carpark.BookingCompleteActivity;
import apcoders.in.carpark.LoginActivity;
import apcoders.in.carpark.R;
import apcoders.in.carpark.SeeAllVehiclesActivity;
import apcoders.in.carpark.SettingsActivity;
import apcoders.in.carpark.Utils.FetchUserData;
import apcoders.in.carpark.Utils.WalletManagement;
import apcoders.in.carpark.WalletActivity;
import apcoders.in.carpark.Wishlist_Parking_Areas_Activity;
import apcoders.in.carpark.models.UserModel;
import es.dmoral.toasty.Toasty;

public class ProfileFragment extends Fragment {

    private Button getHelpBtn, withdrawBtn;
    private TextView profileUserName, profileEmail, profilePhone, userType, walletBalance;
    private CardView shopCardView;
    private LinearLayout profileLogout, totalearning_layout, profileSettings, terms_and_conditionsLayout, profileUpdateLayout, my_booking_layout, wishlistedProductsLayout, communityLayout, rateAppLayout, transactionsLayout, see_vehicles_layout;
    private ImageView userAvatar;
    TextView profile_user_wallet_amount;
    private LinearLayout realContentView;

    String userTypeValue;
    private FirebaseAuth firebaseAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        // Initialize views
        initViews(view);

        firebaseAuth = FirebaseAuth.getInstance();
        realContentView.setVisibility(View.VISIBLE);

//        // Setup UI actions
        setupUIActions();
        WalletManagement.getBalance(firebaseAuth.getCurrentUser().getUid(), new WalletManagement.OnBalanceRetrievedListener() {
            @Override
            public void onBalanceRetrieved(Double balance) {
                walletBalance.setText("₹ " + balance);
                profile_user_wallet_amount.setText(walletBalance.getText().toString());
            }
        });
        // Fetch user data
        FetchUserData();

        return view;
    }

    private void initViews(View view) {

        realContentView = view.findViewById(R.id.RealView);
        walletBalance = view.findViewById(R.id.my_wallet_balance);
        getHelpBtn = view.findViewById(R.id.getHelpBtn);
        terms_and_conditionsLayout = view.findViewById(R.id.terms_and_conditionsLayout);
        totalearning_layout = view.findViewById(R.id.totalearning_layout);
        profileSettings = view.findViewById(R.id.settings);
        shopCardView = view.findViewById(R.id.shop_cardview);
        communityLayout = view.findViewById(R.id.community);
        withdrawBtn = view.findViewById(R.id.withdrawBtn);
        wishlistedProductsLayout = view.findViewById(R.id.wishlisted_products_layout);
        my_booking_layout = view.findViewById(R.id.my_booking_layout);
        rateAppLayout = view.findViewById(R.id.ratetheapp);

        profile_user_wallet_amount = view.findViewById(R.id.profile_user_wallet_amount);
        profileEmail = view.findViewById(R.id.profile_user_email_text);
        profileUserName = view.findViewById(R.id.profile_user_name_text);
//        profilePhone = view.findViewById(R.id.profile_user_phone_text);
        profileLogout = view.findViewById(R.id.profile_logout);
        userAvatar = view.findViewById(R.id.userAccountImage);
//        userType = view.findViewById(R.id.userType);
        see_vehicles_layout = view.findViewById(R.id.my_vehicles_info_layout);
        profileUpdateLayout = view.findViewById(R.id.profile_update_layout);
        transactionsLayout = view.findViewById(R.id.transactions_layout);
    }

    private void setupUIActions() {
        totalearning_layout.setOnClickListener(v -> SeeWallet());
        profileUpdateLayout.setOnClickListener(v -> updateProfile());
        withdrawBtn.setOnClickListener(v -> SeeWallet());
        getHelpBtn.setOnClickListener(v -> openHelpLink());
        communityLayout.setOnClickListener(v -> openCommunityLink());
//        transactionsLayout.setOnClickListener(v -> openTransactionsFragment());
        profileSettings.setOnClickListener(v -> openSettingsActivity());
        wishlistedProductsLayout.setOnClickListener(v -> openWishlistActivity());
        my_booking_layout.setOnClickListener(v -> openBookingsFragment());
        profileLogout.setOnClickListener(v -> logout());
//        terms_and_conditionsLayout.setOnClickListener(v -> startActivity(new Intent(requireActivity(), TermsConditionsActivity.class)));
        see_vehicles_layout.setOnClickListener(v -> openSeeMyVehicles());
        rateAppLayout.setOnClickListener(v -> RateApp());
    }

    private void SeeWallet() {
        startActivity(new Intent(requireActivity(), WalletActivity.class));
    }

    private void RateApp() {
        Toasty.success(requireContext(), "Thank You For Support💖. This Feature Will Work Very Soon", Toasty.LENGTH_LONG).show();
//        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://indusapp.store/qr0jezvq"));
//        startActivity(intent);
    }

    //    private void fetchUserData() {
//        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
//        userTypeValue = sharedPreferences.getString("user_type", "");
//
//        WalletManagement.getBalance(firebaseAuth.getCurrentUser().getUid(), balance -> walletBalance.setText("₹ " + balance));
//
//        if ("farmer".equals(userTypeValue)) {
//            fetchFarmerData();
//        } else if ("vendor".equals(userTypeValue)) {
////            shopCardView.setVisibility(View.VISIBLE);
//            fetchVendorData();
//        } else {
//            redirectToLogin();
//        }
//    }
    public void FetchUserData() {
        FetchUserData.FetchNormalUserData(new FetchUserData.GetNormalUserData() {
            @Override
            public void onCallback(UserModel userModel) {
                if (userModel != null) {
                    Log.d("TAG", "onCallback: " + userModel.getUserFulName());
                    profileUserName.setText(userModel.getUserFulName());
                    profileEmail.setText(userModel.getEmail());

                }
            }
        });
    }
//    private void fetchFarmerData() {
//        FetchUserData.getUserData(new FetchUserDataCallback() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onCallback(User_Farmer_Model userData) {
//                if (userData != null) {
//                    updateUI(userData.getFull_name(), userData.getPhone_number(), userData.getEmail(), "Farmer", userData.getUser_avatar_url());
//                } else {
//                    redirectToLogin();
//                }
//            }
//        });
//    }

//    private void fetchVendorData() {
//        FetchUserData.getVendorData(new FetchVendorDataCallback() {
//            @SuppressLint("SetTextI18n")
//            @Override
//            public void onCallback(VendorsModel vendorData) {
//                if (vendorData != null) {
//                    updateUI(vendorData.getName(), vendorData.getPhoneNumber(), vendorData.getEmail(), "Vendor", vendorData.getUser_avatar_url());
//                } else {
//                    redirectToLogin();
//                }
//            }
//        });
//    }

//    private void updateUI(String name, String phoneNumber, String email, String type, String avatarUrl) {
//        profileUserName.setText(name);
//        profileEmail.setText(email);
//        userType.setText(type);
//        profilePhone.setText(phoneNumber);
//
//        Glide.with(requireActivity())
//                .load(avatarUrl)
//                .placeholder(R.drawable.farmer_avatar)
//                .error(R.drawable.farmer_avatar)
//                .into(userAvatar);
//
//        shimmerLayout.hideShimmer(); // Stop shimmer effect
//        shimmerLayout.setVisibility(View.GONE);
//        realContentView.setVisibility(View.VISIBLE);
//    }

    //    private void redirectToLogin() {
//        firebaseAuth.signOut();
//        startActivity(new Intent(requireActivity(), LoginActivity.class));
//        requireActivity().finish();
//    }
//
    private void logout() {
        new AlertDialog.Builder(requireContext()).setMessage("Are You Sure To LogOut").setPositiveButton("Yes", (dialog, which) -> {
            firebaseAuth.signOut();
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        }).setNegativeButton("No", (dialog, which) -> dialog.dismiss()).create().show();
    }

    private void openHelpLink() {
        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://chat.whatsapp.com/KXzVe0JrDeF4yx1eLyabrT"));
        startActivity(intent);
    }

    private void openCommunityLink() {
        Intent intent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse("https://chat.whatsapp.com/KXzVe0JrDeF4yx1eLyabrT"));
        startActivity(intent);
    }

    private void openSettingsActivity() {
        startActivity(new Intent(requireActivity(), SettingsActivity.class));
    }

    private void openWishlistActivity() {
        startActivity(new Intent(requireActivity(), Wishlist_Parking_Areas_Activity.class));
    }

    private void openBookingsFragment() {
//        Fragment fragment = new MyOrdersFragment();
//        if (getActivity() instanceof FragmentChangeListener) {
//            ((FragmentChangeListener) getActivity()).changeFragment(fragment, R.id.my_orders);
//        }
        startActivity(new Intent(requireActivity(), BookingCompleteActivity.class));
    }
//
//        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
//        transaction.replace(R.id.framelayout, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

//    private void openTransactionsFragment() {
//        Fragment fragment = new TransactionsFragment();
//        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
//        transaction.replace(R.id.framelayout, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    private void openSeeMyVehicles() {
        startActivity(new Intent(requireActivity(), SeeAllVehiclesActivity.class));
    }

    private void updateProfile() {
//        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
//        userTypeValue = sharedPreferences.getString("user_type", "");
        FragmentTransaction transaction = requireFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new ProfileEditFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
