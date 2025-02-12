package apcoders.in.carpark.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import apcoders.in.carpark.R;
import apcoders.in.carpark.models.Session;

public class SessionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ACTIVE = 1;
    private static final int VIEW_TYPE_COMPLETED = 2;

    private Context context;
    private List<Session> sessionList;

    public SessionAdapter(Context context, List<Session> sessionList) {
        this.context = context;
        this.sessionList = sessionList;
    }

    @Override
    public int getItemViewType(int position) {
        return sessionList.get(position).isActive() ? VIEW_TYPE_ACTIVE : VIEW_TYPE_COMPLETED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_ACTIVE) {
            View view = inflater.inflate(R.layout.activesession, parent, false);
            return new ActiveViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.completed_session, parent, false);
            return new CompletedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Session session = sessionList.get(position);

        if (holder instanceof ActiveViewHolder) {
            ((ActiveViewHolder) holder).username.setText(session.getUsername());
            ((ActiveViewHolder) holder).parkingArea.setText(session.getParkingArea());
            ((ActiveViewHolder) holder).timeRemaining.setText("Time Remaining: " + session.getTimeRemaining());
        } else if (holder instanceof CompletedViewHolder) {
            ((CompletedViewHolder) holder).username.setText(session.getUsername());
            ((CompletedViewHolder) holder).parkingArea.setText(session.getParkingArea());
            ((CompletedViewHolder) holder).date.setText("Date: " + session.getDate());
            ((CompletedViewHolder) holder).totalTime.setText("Total Time: " + session.getTotalTime());
            ((CompletedViewHolder) holder).amount.setText("Amount: " + session.getAmount());
        }
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    // ViewHolder for Active Sessions
    static class ActiveViewHolder extends RecyclerView.ViewHolder {
        TextView username, parkingArea, timeRemaining;

        public ActiveViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textview_username);
            parkingArea = itemView.findViewById(R.id.ParkingArea);
            timeRemaining = itemView.findViewById(R.id.textview_time_remaining);
        }
    }

    // ViewHolder for Completed Sessions
    static class CompletedViewHolder extends RecyclerView.ViewHolder {
        TextView username, parkingArea, date, totalTime, amount;

        public CompletedViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textview_username);
            parkingArea = itemView.findViewById(R.id.ParkingArea);
            date = itemView.findViewById(R.id.Date);
            totalTime = itemView.findViewById(R.id.total_time);
            amount = itemView.findViewById(R.id.Amount);
        }
    }
}
