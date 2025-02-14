package apcoders.in.carpark.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import apcoders.in.carpark.Adapter.BookingsRecyclerViewAdapter;
import apcoders.in.carpark.R;
import apcoders.in.carpark.Utils.BookingManagement;
import apcoders.in.carpark.models.BookingDetailsModel;
import es.dmoral.toasty.Toasty;

public class BookingFragment extends Fragment {
    RecyclerView bookings_recycler_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_booking_fragment, container, false);
        bookings_recycler_view = view.findViewById(R.id.bookings_recycler_view);
        bookings_recycler_view.setHasFixedSize(true);
        bookings_recycler_view.setNestedScrollingEnabled(false);

        bookings_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        BookingManagement.fetchUserBookings(new BookingManagement.UserBookingsCallback() {
            @Override
            public void onSuccess(List<BookingDetailsModel> bookings) {
                if (bookings != null) {
                    BookingsRecyclerViewAdapter adapter = new BookingsRecyclerViewAdapter(requireContext(), bookings);
                    bookings_recycler_view.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toasty.info(requireContext(), "No Bookings Data Found", Toasty.LENGTH_LONG).show();
                    bookings_recycler_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("TAG", "onFailure: " + errorMessage);
            }
        });

        return view;

    }
}