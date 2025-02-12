package apcoders.in.carpark.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import apcoders.in.carpark.R;
import apcoders.in.carpark.models.RideModel;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.ViewHolder> {
    private List<RideModel> rideList;

    public RideAdapter(List<RideModel> rideList) {
        this.rideList = rideList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rides_ticket, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RideModel ride = rideList.get(position);

        holder.textviewUsername.setText(ride.getUsername());
        holder.textviewParkingArea.setText(ride.getParkingArea());
        holder.textviewUniqueId.setText("Unique ID: " + ride.getUniqueId());
        holder.textviewCheckIn.setText(ride.getCheckIn());
        holder.textviewCheckOut.setText(ride.getCheckOut());
        holder.textviewSpecification.setText(ride.getSpecifications());
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textviewUsername, textviewParkingArea, textviewUniqueId, textviewCheckIn, textviewCheckOut, textviewSpecification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textviewUsername = itemView.findViewById(R.id.textview_username);
            textviewParkingArea = itemView.findViewById(R.id.textview_parking);
            textviewUniqueId = itemView.findViewById(R.id.textview_unique_id);
            textviewCheckIn = itemView.findViewById(R.id.textview_checkin);
            textviewCheckOut = itemView.findViewById(R.id.textview_checkout);
            textviewSpecification = itemView.findViewById(R.id.textview_specification);
        }
    }
}
