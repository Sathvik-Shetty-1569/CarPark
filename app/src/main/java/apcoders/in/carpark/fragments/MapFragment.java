package apcoders.in.carpark.fragments;

import static androidx.compose.ui.semantics.SemanticsPropertiesKt.dismiss;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apcoders.in.carpark.Adapter.SearchAdapter;
import apcoders.in.carpark.BookingCompleteActivity;
import apcoders.in.carpark.R;

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
    private apcoders.in.carpark.Adapter.SearchAdapter searchAdapter;
    private List<String> suggestionList = new ArrayList<>();
private Button bookslots;
    private String[] locationNames = {
            "Panvel Railway Station Parking",
            "Khandeshwar Railway Station Parking",
            "Belapur CBD Parking",
            "Vashi Sector 17 Parking",
            "Sanpada Station Parking",
            "Seawoods Grand Central Mall Parking",
            "Navi Mumbai International Airport Parking",
            "Palm Beach Road Public Parking",
            "Kalamboli Truck Terminal Parking",
            "Kharghar Sector 21 Parking",
            "Little World Mall Parking",
            "Hiranandani Fortis Parking",
            "Raghunath Vihar Parking",
            "Nerul Railway Station Parking",
            "Juhi Nagar Sector 24 Parking",
            "Vashi Inorbit Mall Parking",
            "Lower Parel Phoenix Mall Parking",
            "Bandra Kurla Complex Parking",
            "Phoenix Marketcity Kurla Parking",
            "CST Railway Station Parking",
            "Mumbai Central Parking Lot",
            "Colaba Causeway Parking",
            "Gateway of India Parking",
            "Marine Drive Public Parking",
            "Dadar Station Parking",
            "Bandra Linking Road Parking",
            "Andheri Station East Parking",
            "Powai Hiranandani Gardens Parking",
            "Borivali National Park Parking",
            "Thane Viviana Mall Parking",
            "Pen Railway Station Parking",
            "Pen Bus Stand Parking",
            "Rasayani Public Parking",
            "Panvel Orion Mall Parking",
            "Shivaji Chowk Parking Panvel",
            "Vashi Palm Beach Parking",
            "Parel ITC Grand Central Parking",
            "Lokmanya Tilak Terminus Parking",
            "Kanjurmarg Railway Parking",
            "Thane Majiwada Parking",
            "Malad Infinity Mall Parking",
            "Goregaon Oberoi Mall Parking",
            "Juhu Beach Parking",
            "Dadar Shivaji Park Parking",
            "Chhatrapati Shivaji Maharaj Airport Parking",
            "Sion Hospital Parking",
            "Mira Road Public Parking",
            "Mulund LBS Marg Parking",
            "Kalyan Railway Station Parking",
            "Karjat Railway Station Parking",
            "Pillai HOC Campus Parking",
            "Rasayani Railway Station Parking",
            "MIDC Taloja Parking Area",
            "HOC Colony Community Center Parking",
            "Panvel Municipal Parking Lot"
    };

    private LatLng[] locations = {
            new LatLng(18.9886, 73.1101),
            new LatLng(19.0213, 73.0401),
            new LatLng(19.0725, 72.9977),
            new LatLng(19.0795, 73.0076),
            new LatLng(19.0236, 73.0482),
            new LatLng(19.0646, 73.0923),
            new LatLng(18.9900, 72.8656),
            new LatLng(19.0542, 73.0460),
            new LatLng(19.0123, 73.0978),
            new LatLng(19.0317, 73.0654),
            new LatLng(19.0412, 73.0681),
            new LatLng(19.0519, 73.0598),
            new LatLng(19.0482, 73.0549),
            new LatLng(19.0326, 73.0175),
            new LatLng(19.0697, 73.0049),
            new LatLng(19.0793, 72.9973),
            new LatLng(19.0180, 72.8305),
            new LatLng(19.0587, 72.8495),
            new LatLng(19.0815, 72.8842),
            new LatLng(18.9388, 72.8354),
            new LatLng(18.9676, 72.8196),
            new LatLng(18.9217, 72.8331),
            new LatLng(18.9219, 72.8346),
            new LatLng(18.9451, 72.8238),
            new LatLng(19.0191, 72.8423),
            new LatLng(19.0607, 72.8365),
            new LatLng(19.1197, 72.8464),
            new LatLng(19.1201, 72.9023),
            new LatLng(19.2288, 72.8540),
            new LatLng(19.1957, 72.9725),
            new LatLng(18.7390, 73.0957),
            new LatLng(18.7375, 73.0958),
            new LatLng(18.8845, 73.1689),
            new LatLng(18.9903, 73.1276),
            new LatLng(18.9912, 73.1105),
            new LatLng(19.0718, 73.0047),
            new LatLng(19.0033, 72.8347),
            new LatLng(19.0636, 72.9003),
            new LatLng(19.1203, 72.8275),
            new LatLng(19.0276, 72.8409),
            new LatLng(19.0896, 72.8657),
            new LatLng(19.0997, 72.9131),
            new LatLng(19.0097, 72.8489),
            new LatLng(19.2801, 72.8725),
            new LatLng(19.1717, 72.9395),
            new LatLng(19.2437, 73.1274),
            new LatLng(18.9106, 73.3228),
            new LatLng(19.1765, 72.9553),
            new LatLng(19.1970, 72.9701),
            new LatLng(18.8785, 73.1189),  // Pillai HOC Campus Parking
            new LatLng(18.8952, 73.1187),  // Rasayani Railway Station Parking
            new LatLng(18.8965, 73.0973),  // MIDC Taloja Parking Area
            new LatLng(18.8702, 73.1278),  // HOC Colony Community Center Parking
            new LatLng(18.9945, 73.1175)   // Panvel Municipal Parking Lot
    };

private LinearLayout bottomDrawer;
    private TextView parkingArea, address, spaceSlot, chargesPerHour;

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
        bottomDrawer.setVisibility(View.GONE); // Initially hide it
        searchLocation = view.findViewById(R.id.search_location);
         parkingArea = view.findViewById(R.id.textview_parking);
         address = view.findViewById(R.id.Address);
         bookslots = view.findViewById(R.id.Bookslots);
         spaceSlot = view.findViewById(R.id.textview_space_Slot);
         chargesPerHour = view.findViewById(R.id.chargesperhour);
        ImageView ratings = view.findViewById(R.id.ratings);

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
                startActivity(new Intent(requireActivity(), BookingCompleteActivity.class));
            }
        });

        searchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getPlacePredictions(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
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

            updateBottomSheet(marker.getTitle());
            return false;
        });


    }


    private void updateBottomSheet(String locationName) {
        if (getView() == null) return;
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        FloatingActionButton Map = getActivity().findViewById(R.id.Map);
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE); // Hide BottomNavigationView
        }

        if (Map != null) {
            Map.animate().alpha(0f).setDuration(300).withEndAction(() -> Map.setVisibility(View.GONE)).start();
        }

        int index = -1;
        for (int i = 0; i < locationNames.length; i++) {
            if (locationNames[i].equals(locationName)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            parkingArea.setText(locationNames[index]);
            address.setText("Address: " + locationNames[index]); // Use parking name as address
        } else {
            parkingArea.setText(locationName);
            address.setText("Address: Not Available");
        }
            int inte = -1;
        for (int i = 0; i < locations.length; i++) {
            if (locations[i].equals(locationName)) {
                inte = i;
                break;
            }
        }

        if (index != -1) {
            parkingArea.setText(locationNames[index]);
            address.setText("Address: " + locationNames[index]); // Use parking name as address
        } else {
            parkingArea.setText(locationName);
            address.setText("Address: Not Available");
        }
        spaceSlot.setText("Available Slots: 10");  // Example value
        chargesPerHour.setText("$5 per hour");  // Example value

        Log.d("BottomSheet", "Updated with: " + locationName );
        bottomDrawer.setVisibility(View.VISIBLE);
        Map.setVisibility(View.VISIBLE);

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
        for (int i = 0; i < locations.length; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(locations[i])
                    .title(locationNames[i])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))); // Blue color for parking
        }
    }


    private void moveCameraToCurrentLocation() {
        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
        } else {
            // If location is null, center the map around the first parking location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locations[0], 12f));
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
            recyclerView.setVisibility(View.VISIBLE);
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
                recyclerView.setVisibility(View.GONE);
                for (int i = 0; i < locations.length; i++) {
                    double distance = calculateDistance(latLng, locations[i]);
                    if (distance <= 5.0) {
                        mMap.addMarker(new MarkerOptions().position(locations[i]).title(locationNames[i]));
                    }
                }

            }

             else {
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
