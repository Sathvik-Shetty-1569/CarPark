
package apcoders.in.carpark.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.List;


import apcoders.in.carpark.R;
import apcoders.in.carpark.models.ParkingInfo;
import es.dmoral.toasty.Toasty;

public class WishlistProductAdapter extends RecyclerView.Adapter<WishlistProductAdapter.ViewHolder> {

    Context context;
    List<ParkingInfo> ProductDataList;

    public WishlistProductAdapter(Context context, List<ParkingInfo> ProductDataList) {
        this.context = context;
        this.ProductDataList = ProductDataList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.item_withdrawal_request, parent, false);
        return new ViewHolder(context, ProductDataList, view);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(context).load(ProductDataList.get(position).getProductImagesUrl().get(0))
//                .placeholder(R.drawable.logo)
//                .error(R.drawable.chat_bot_img)
//                .into(holder.product_image);
//        if(ProductDataList.get(position).getServiceType().equals("Rent")){
//            holder.service_type.setBackground(context.getResources().getDrawable(R.drawable.outline_shape));
//        }
//        holder.service_type.setText(ProductDataList.get(position).getServiceType());
//        holder.product_name.setText(ProductDataList.get(position).getProductName());
//        holder.product_location.setText(ProductDataList.get(position).getProductAddress());
//        holder.product_price.setText("₹" + ProductDataList.get(position).getProductPrice());

    }

    @Override
    public int getItemCount() {
        return ProductDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView product_image;
        TextView product_name,service_type, product_price, product_location;
        CardView product_CardView;
        ImageView removeWishlistedItem;
        MaterialButton seeProductbtn;

        public ViewHolder(Context context, List<ParkingInfo> ProductDataList, @NonNull View itemView) {
            super(itemView);
//            seeProductbtn = itemView.findViewById(R.id.seeProductbtn);
//            product_CardView = itemView.findViewById(R.id.productCardView);
//            service_type = itemView.findViewById(R.id.service_type);
//            product_image = itemView.findViewById(R.id.product_image);
//            removeWishlistedItem = itemView.findViewById(R.id.removeWishlistedItem);
//            product_name = itemView.findViewById(R.id.product_name);
//            product_price = itemView.findViewById(R.id.product_price);
//            product_location = itemView.findViewById(R.id.product_location);
//
//            removeWishlistedItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        // Remove the product from SharedPreferences
//                        CartManagement.RemoveProductById(context, ProductDataList.get(position).getProductId());
//
//                        // Remove the item from the dataset
//                        ProductDataList.remove(position);
//
//                        // Notify the adapter about item removal
//                        notifyItemRemoved(position);
//
//                        // Optional: Notify the adapter about changes to the dataset
//                        notifyItemRangeChanged(position, ProductDataList.size());
//
//                        // Show a confirmation message
//                        Toasty.success(context, "Removed From Wishlist", Toasty.LENGTH_LONG).show();
//                    }
//                }
//            });
//
//            try {
//                seeProductbtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            Intent i = new Intent(context, ProductDetailsActivity.class);
//                            Log.e("TAG", "onClick: " + ProductDataList.get(position).getProductId());
//                            i.putExtra("ProductId", ProductDataList.get(position).getProductId());
//                            context.startActivity(i);
//                        }
//                    }
//                });
//            } catch (Exception e) {
//
//            }
//            product_CardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        Intent i = new Intent(context, ProductDetailsActivity.class);
//                        Log.e("TAG", "onClick: " + ProductDataList.get(position).getProductId());
//                        i.putExtra("ProductId", ProductDataList.get(position).getProductId());
//                        context.startActivity(i);
//                    }
//                }
//            });
        }
    }
}