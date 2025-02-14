package apcoders.in.carpark.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import apcoders.in.carpark.R;
import apcoders.in.carpark.models.VehicleModel;

public class SeeAllVehiclesAdapter extends RecyclerView.Adapter<SeeAllVehiclesAdapter.VehicleViewHolder> {

    private List<VehicleModel> vehicleList;

    public SeeAllVehiclesAdapter(ArrayList<VehicleModel> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        VehicleModel vehicle = vehicleList.get(position);
        holder.vehicleNumber.setText(vehicle.getVehicleNumber());
        holder.vehicleType.setText(vehicle.getVehicleType());
        holder.vehicleBrand.setText(vehicle.getVehicleBrand());
        holder.vehicleModel.setText(vehicle.getVehicleModel());
        holder.vehicleColor.setText(vehicle.getVehicleColor());
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleNumber, vehicleType, vehicleBrand, vehicleModel, vehicleColor;

        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleNumber = itemView.findViewById(R.id.tvVehicleNumber);
            vehicleType = itemView.findViewById(R.id.tvVehicleType);
            vehicleBrand = itemView.findViewById(R.id.tvVehicleBrand);
            vehicleModel = itemView.findViewById(R.id.tvVehicleModel);
            vehicleColor = itemView.findViewById(R.id.tvVehicleColor);
        }
    }
}
