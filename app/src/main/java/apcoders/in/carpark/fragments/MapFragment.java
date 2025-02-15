package apcoders.in.carpark.fragments;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apcoders.in.carpark.Adapter.SearchAdapter;
import apcoders.in.carpark.BookingSlotActivity;
import apcoders.in.carpark.R;
import apcoders.in.carpark.Utils.ParkingAreaManagement;
import apcoders.in.carpark.models.ParkingInfo;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private EditText searchLocation;
    private RecyclerView recyclerView;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    private SearchAdapter searchAdapter;
    private List<String> suggestionList = new ArrayList<>();
    private Button bookslots;
    String name;
    double latitude;
    double longitude;
    String slots;
    String amount;
    String locaddress;
    List<Uri> parkingUrlListUri = new ArrayList<>();
    List<String> parkingUrlListString = new ArrayList<>();
    String parkingName, AvailableSlots, Amount;
    private LinearLayout bottomDrawer;
    private TextView parkingArea, address, spaceSlot, chargesPerHour;
    private ImageView bookmarkIcon,getBookmarkIcon; // Added bookmark icon
    private boolean isBookmarked = false; // Track bookmark state

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map_fragment, container, false);

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyAUDX2GMv7MX6Mi1D8tBbsDvlu1OyuaDOY");
        }
        placesClient = Places.createClient(requireContext());
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchAdapter = new SearchAdapter(suggestionList, this::searchPlace);
        recyclerView.setAdapter(searchAdapter);
        // Initialize fused location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        bottomDrawer = view.findViewById(R.id.showdrawerbottom);
        bottomDrawer.setVisibility(GONE); // Initially hide it
        searchLocation = view.findViewById(R.id.search_location);
        parkingArea = view.findViewById(R.id.textview_parking);
        address = view.findViewById(R.id.Address);
        bookslots = view.findViewById(R.id.Bookslots);
        spaceSlot = view.findViewById(R.id.textview_space_Slot);
        chargesPerHour = view.findViewById(R.id.chargesperhour);
        ImageView ratings = view.findViewById(R.id.ratings);
        bookmarkIcon = view.findViewById(R.id.bookmarkIcon); // Initialize bookmark icon
        getBookmarkIcon = view.findViewById(R.id.bookmarkIconselected);

        initializeUI(view);
        setupSearchFunctionality();

        // Bookmark icon click listener
        bookmarkIcon.setOnClickListener(v -> toggleWishlist());
        getBookmarkIcon.setOnClickListener(v -> toggleWishlist());

        searchLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String location = searchLocation.getText().toString().trim();
                if (!location.isEmpty()) {
                    searchPlace(location);
                }
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                searchLocation.clearFocus();

                return true; // Return true to consume event (prevent new line)
            }
            return false;
        });

        bookslots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(requireActivity(), BookingSlotActivity.class);
                i.putExtra("parkingName", parkingName);
                i.putExtra("AvailableSlots", AvailableSlots);
                i.putExtra("Amount", Amount);
                startActivity(i);
            }
        });

        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPlacePredictions(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Load the map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getLastLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        addParkingMarkers();

        mMap.setOnMarkerClickListener(marker -> {

            if (marker.getTag() instanceof ParkingInfo) {
//                bottomDrawer.setVisibility(View.GONE);
                ParkingInfo info = (ParkingInfo) marker.getTag();
                parkingName = info.getName();
                AvailableSlots = String.valueOf(info.getSlots());
                Amount = info.getAmount();
                locaddress = info.getLocaddress();
                parkingUrlListUri.clear();
                for (String url : info.getParkingAreaImagesUrl()) {
                    parkingUrlListUri.add(Uri.parse(url));
                }
                updateBottomSheet(info.getName(), Integer.parseInt(info.getSlots()), info.getAmount());
                View ParkingAreaBottomView = LayoutInflater.from(requireActivity()).inflate(R.layout.bottom_parking_details, null, false);

                ImageView product_image_add_image = ParkingAreaBottomView.findViewById(R.id.product_image_add_image);
                RecyclerView parkingRecycler_layout = ParkingAreaBottomView.findViewById(R.id.product_images_recyclerView);
                TextView textview_parking = ParkingAreaBottomView.findViewById(R.id.textview_parking);
                TextView Address = ParkingAreaBottomView.findViewById(R.id.Address);
                Button Bookslots = ParkingAreaBottomView.findViewById(R.id.Bookslots);
                TextView textview_space_Slot = ParkingAreaBottomView.findViewById(R.id.textview_space_Slot);
                TextView chargesperhour = ParkingAreaBottomView.findViewById(R.id.chargesperhour);
                ImageView ratings = ParkingAreaBottomView.findViewById(R.id.ratings);

                textview_parking.setText(parkingName);
                Address.setText(locaddress);
                textview_space_Slot.setText(AvailableSlots);
                chargesperhour.setText(Amount);
                Bookslots.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(requireActivity(), BookingSlotActivity.class);
                        i.putExtra("parkingName", parkingName);
                        i.putExtra("AvailableSlots", AvailableSlots);
                        i.putExtra("Amount", Amount);
                        startActivity(i);
                    }
                });
                product_image_add_image.setVisibility(VISIBLE);
                parkingRecycler_layout.setVisibility(VISIBLE);
                Log.d("TAG", "onMapReady: " + parkingUrlListUri.size());
//                ProductImageAdapter adapter = new ProductImageAdapter(requireActivity(), parkingUrlListUri);
//                parkingRecycler_layout.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//                parkingRecycler_layout.setAdapter(adapter);


                BottomSheetDialog ParkignbottomSheetDialog = new BottomSheetDialog(requireContext());
                ParkignbottomSheetDialog.setContentView(ParkingAreaBottomView);
                ParkignbottomSheetDialog.show();
            } else {
                bottomDrawer.setVisibility(GONE);
            }
            return false;
        });
    }

    private void initializeUI(View view) {
        bottomDrawer = view.findViewById(R.id.showdrawerbottom);
        bottomDrawer.setVisibility(GONE);
        searchLocation = view.findViewById(R.id.search_location);
        parkingArea = view.findViewById(R.id.textview_parking);
        address = view.findViewById(R.id.Address);
        bookslots = view.findViewById(R.id.Bookslots);
        spaceSlot = view.findViewById(R.id.textview_space_Slot);
        chargesPerHour = view.findViewById(R.id.chargesperhour);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchAdapter = new SearchAdapter(suggestionList, this::searchPlace);
        recyclerView.setAdapter(searchAdapter);
    }

    private void setupSearchFunctionality() {
        searchLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String location = searchLocation.getText().toString().trim();
                if (!location.isEmpty()) {
                    searchPlace(location);
                }
                hideKeyboard(v);
                searchLocation.clearFocus();
                return true;
            }
            return false;
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateBottomSheet(String locationName, int slots, String amount) {
        if (getView() == null) return;
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        FloatingActionButton Map = getActivity().findViewById(R.id.Map);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(GONE); // Hide BottomNavigationView
        }
        if (Map != null) {
            Map.animate().alpha(0f).setDuration(300).withEndAction(() -> Map.setVisibility(GONE)).start();
        }

        parkingArea.setText(locationName);
        address.setText("Address: " + locationName);
        spaceSlot.setText("Available Slots: " + slots);
        chargesPerHour.setText(amount);

        Log.d("BottomSheet", "Updated with: " + locationName);
        bottomDrawer.setVisibility(View.VISIBLE);
        Map.setVisibility(View.VISIBLE);
    }

    private void toggleWishlist() {
        if (isBookmarked) {
            // Show black wishlist, hide purple
            getBookmarkIcon.setVisibility(View.GONE);
            bookmarkIcon.setVisibility(View.VISIBLE);
        } else {
            // Show purple wishlist with pop effect, hide black
            bookmarkIcon.setVisibility(View.GONE);
            getBookmarkIcon.setVisibility(View.VISIBLE);
            applyPopAnimation(getBookmarkIcon);
        }
        isBookmarked = !isBookmarked; // Toggle state
    }

    private void applyPopAnimation(View view) {
        ScaleAnimation popAnim = new ScaleAnimation(
                0.8f, 1.2f,  // Scale X from 0.8 to 1.2
                0.8f, 1.2f,  // Scale Y from 0.8 to 1.2
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        popAnim.setDuration(200); // Animation duration
        popAnim.setFillAfter(true);
        view.startAnimation(popAnim);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    moveCameraToCurrentLocation();
                } else {
                    Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addParkingMarkers() {

        ParkingAreaManagement.fetchParkingInfo(new ParkingAreaManagement.FirestoreDataCallback() {
            @Override
            public void onDataFetched(List<ParkingInfo> parkingList) {
                Log.d("TAG", "onDataFetched: " + parkingList.size());
                for (ParkingInfo area : parkingList) {
                    name = area.getName();
                    latitude = area.getLat();
                    longitude = area.getLog();
                    slots = area.getSlots();
                    amount = area.getAmount();
                    locaddress = area.getLocaddress();
                    parkingUrlListString = area.getParkingAreaImagesUrl();
                    Log.d("TAG", "onDataFetched: " + name);
                    Log.d("TAG", "onDataFetched: " + latitude);
                    Log.d("TAG", "onDataFetched: " + longitude);
                    Log.d("TAG", "onDataFetched: " + slots);
                    Log.d("TAG", "onDataFetched: " + amount);
                    Log.d("TAG", "onDataFetched: " + locaddress);

                    for (String url : area.getParkingAreaImagesUrl()) {
                        parkingUrlListUri.add(Uri.parse(url));
                    }
                    Log.d("TAG", "onDataFetched:URL " + parkingUrlListUri.size());
                    Log.d("TAG", "onDataFetched: " + name);
                    LatLng location = new LatLng(latitude, longitude);
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .title(name)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))); // Blue color for parking

                    // Store extra data in marker using ParkingInfo model
                    marker.setTag(new ParkingInfo(name, slots, amount, FirebaseAuth.getInstance().getCurrentUser().getUid(), latitude, longitude, locaddress, parkingUrlListString));
                }
            }

            @Override
            public void onFailure(String error) {
                Log.d("TAG", "onFailure: " + error);
            }
        });
    }

    private void moveCameraToCurrentLocation() {
        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
        } else {
            // If no location is available, move to the first parking location from Firebase
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("parking_areas");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        double latitude = snapshot.child("latitude").getValue(Double.class);
                        double longitude = snapshot.child("longitude").getValue(Double.class);
                        LatLng firstLocation = new LatLng(latitude, longitude);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12f));
                        break;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Failed to read data", databaseError.toException());
                }
            });
        }
    }

    private void getPlacePredictions(String query) {
        sessionToken = AutocompleteSessionToken.newInstance();
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(sessionToken)
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            suggestionList.clear();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                suggestionList.add(prediction.getPrimaryText(null).toString());
            }
            searchAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(VISIBLE);
        });
    }

    private void searchPlace(String location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                // Add marker on searched location
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                recyclerView.setVisibility(GONE);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("parking_areas");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            double lat = snapshot.child("latitude").getValue(Double.class);
                            double lng = snapshot.child("longitude").getValue(Double.class);
                            String name = snapshot.child("name").getValue(String.class);

                            LatLng parkingLocation = new LatLng(lat, lng);
                            double distance = calculateDistance(latLng, parkingLocation);
                            if (distance <= 5.0) {
                                mMap.addMarker(new MarkerOptions().position(parkingLocation).title(name));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Error fetching locations", databaseError.toException());
                    }
                });

            } else {
                Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private double calculateDistance(LatLng latLng1, LatLng latLng2) {
        double earthRadius = 6371; // Earth's radius in km
        double dLat = Math.toRadians(latLng2.latitude - latLng1.latitude);
        double dLng = Math.toRadians(latLng2.longitude - latLng1.longitude);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latLng1.latitude)) * Math.cos(Math.toRadians(latLng2.latitude)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getLastLocation();
                }
            } else {
                Toast.makeText(requireContext(), "Location Permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}