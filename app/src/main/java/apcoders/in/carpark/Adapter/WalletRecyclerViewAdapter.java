package apcoders.in.carpark.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import apcoders.in.carpark.R;
import apcoders.in.carpark.models.WalletTransaction;

public class WalletRecyclerViewAdapter extends RecyclerView.Adapter<WalletRecyclerViewAdapter.ViewHolder> {

    private ArrayList<WalletTransaction> withdrawalRequests = new ArrayList<>();

    public WalletRecyclerViewAdapter(ArrayList<WalletTransaction> withdrawalRequests) {
        this.withdrawalRequests = withdrawalRequests;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_withdrawal_request, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WalletTransaction withdrawal = withdrawalRequests.get(position);

//        holder.upiIdTextView.setText("UPI ID: " + withdrawal.ge);
        holder.TransactionAmountdTextView.setText("" + withdrawal.getAmount());
        holder.MessageTextView.setText(withdrawal.getDescription());
        holder.TransactionDateTextView.setText(withdrawal.getTransactionDate().toLocaleString().substring(0, 13));
        holder.TransactionIdTextView.setText(withdrawal.getTransactionId());
        holder.TypeTextView.setText(withdrawal.getType());
//        holder.tvRequestId.setText("Request ID: " + withdrawal.getRequestId());
//        if (withdrawal.getStatus().equals("Pending")) {
//            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.black));
//            holder.statusTextView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.yellow));
//        } else if (withdrawal.getStatus().equals("Completed")) {
//            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
//            holder.statusTextView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.dark_green));
//        } else {
//            holder.statusTextView.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.white));
//            holder.statusTextView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.red));
//        }
//        holder.statusTextView.setText("Status: " + withdrawal.getStatus());
//        holder.tvMessage.setText("Message: " + withdrawal.getMsg());
//        holder.tvTransactionDate.setText("Date: " + withdrawal.getRequestDate().toLocaleString().substring(0, 12));
    }

    @Override
    public int getItemCount() {
        return withdrawalRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView TransactionIdTextView, TransactionDateTextView, MessageTextView, TypeTextView, TransactionAmountdTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            TransactionIdTextView = itemView.findViewById(R.id.TransactionIdTextView);
            TransactionDateTextView = itemView.findViewById(R.id.tvTransactionDate);
            MessageTextView = itemView.findViewById(R.id.tvMessage);
            TransactionAmountdTextView = itemView.findViewById(R.id.text_amount);
            TypeTextView = itemView.findViewById(R.id.TypeIdTextView);

        }
    }
}
