package apcoders.in.carpark.fragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import apcoders.in.carpark.Adapter.SearchAdapter;
import apcoders.in.carpark.R;

public class OwnerParkMap extends Fragment implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private LatLng selectedLatLng;
    private EditText searchLocation;
    private RecyclerView recyclerView;
    private PlacesClient placesClient;
    private Button confirmButton;
    private AutocompleteSessionToken sessionToken;
    private apcoders.in.carpark.Adapter.SearchAdapter searchAdapter;
    private List<String> suggestionList = new ArrayList<>();




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_ownermap_fragment, container, false);

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyAUDX2GMv7MX6Mi1D8tBbsDvlu1OyuaDOY");
        }
        placesClient = Places.createClient(requireContext());
        recyclerView = view.findViewById(R.id.recycler_view);
        confirmButton = view.findViewById(R.id.btncomfirmlocation);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchAdapter = new SearchAdapter(suggestionList, this::searchPlace);
        recyclerView.setAdapter(searchAdapter);
        // Initialize fused location provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        searchLocation = view.findViewById(R.id.search_location);

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

confirmButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (selectedLatLng == null) {
            Toast.makeText(requireContext(), "Please mark the location before confirming!", Toast.LENGTH_SHORT).show();
        } else {
            // Get address from LatLng
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(selectedLatLng.latitude, selectedLatLng.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    String selectedAddress = addresses.get(0).getAddressLine(0);

                    // Send back to OwnerProfileActivity
                    Bundle result = new Bundle();
                    result.putString("selected_location", selectedAddress);
                    getParentFragmentManager().setFragmentResult("requestKey", result);

                    // Close fragment
                    getParentFragmentManager().popBackStack();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error fetching address", Toast.LENGTH_SHORT).show();
            }
        }
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
            mMap.setOnMapClickListener(latLng -> {
                mMap.clear(); // Clear previous markers
                mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                selectedLatLng = latLng;
                Toast.makeText(requireContext(), "Location Selected: " + latLng.latitude + ", " + latLng.longitude, Toast.LENGTH_SHORT).show();
            });

        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

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

    private void moveCameraToCurrentLocation() {
        if (currentLocation != null) {
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f));
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

            } else {
                Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
