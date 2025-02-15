package apcoders.in.carpark.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import apcoders.in.carpark.BookingCompleteActivity;
import apcoders.in.carpark.R;
import apcoders.in.carpark.models.BookingDetailsModel;

public class BookingsRecyclerViewAdapter extends RecyclerView.Adapter<BookingsRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<BookingDetailsModel> parkingList;

    public BookingsRecyclerViewAdapter(Context context, List<BookingDetailsModel> parkingList) {
        this.context = context;
        this.parkingList = parkingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookings_recyclerview_layout, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingDetailsModel model = parkingList.get(position);

        holder.parkingAreaName.setText(model.getParkingAreaName());
        holder.parkingStatus.setText(model.getPaymentStatus());
        holder.parkingId.setText("Unique ID: " + model.getParkingLotId());
        holder.bookingTime.setText(model.getBookingTime());
        holder.checkInTime.setText(model.getStartTime());
        holder.checkOutTime.setText(model.getEndTime());
        holder.timeRemaining.setText("01:24 Hours");
    }

    @Override
    public int getItemCount() {
        return parkingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView parkingAreaName, parkingStatus, parkingId, bookingTime, checkInTime, checkOutTime, timeRemaining;
        CardView bookingCardView;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            bookingCardView = itemView.findViewById(R.id.bookingCardView);
            parkingAreaName = itemView.findViewById(R.id.ParkingAreaName);
            parkingStatus = itemView.findViewById(R.id.ParkingStatus);
            parkingId = itemView.findViewById(R.id.ParkingArea);
            bookingTime = itemView.findViewById(R.id.textview_time_remaining);
            checkInTime = itemView.findViewById(R.id.textview_time_checkIn);
            checkOutTime = itemView.findViewById(R.id.textview_time_checkOut);
            timeRemaining = itemView.findViewById(R.id.textview_time_remaining);

            bookingCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    if (id == R.id.bookingCardView) {
                        Intent i = new Intent(context, BookingCompleteActivity.class);
                        i.putExtra("BookingId", "PKG463F1847");
                        i.putExtra("ParkAreaName", "Swami Vivekananda Engineering Buiding\"");
                        context.startActivity(i);
                    }
                }
            });
        }
    }
}
