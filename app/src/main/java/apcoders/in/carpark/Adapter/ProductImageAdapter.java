
package apcoders.in.carpark.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import apcoders.in.carpark.R;


public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.SliderViewHolder> {

    private final List<Uri> imageIds;
    private Context context;

    public ProductImageAdapter(Context context, List<Uri> imageIds) {
        this.context = context;
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_parking_details, parent, false);
        return new SliderViewHolder(context, view, imageIds);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
//        holder.imageView.setImageURI(imageIds.get(position));
        Log.d("TAG", "onBindViewHolder: "+imageIds.get(position));
//        Glide.with(context).load(imageIds.get(position)).into(holder.imageView);

        Picasso.get()
                .load(imageIds.get(position))
                .into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Picasso", "Image loaded successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Picasso", "Failed to load image", e);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return imageIds.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(Context context, @NonNull View itemView, List<Uri> imageIds) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            Log.d("TAG", "SliderViewHolder: "+imageIds.size());
            View view = View.inflate(context, R.layout.fullscreen_product_preview_layout, null);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(imageView.getContext()).setView(view);
//            ImageView product_image = view.findViewById(R.id.product_image);
//            ImageView CloseDialog = view.findViewById(R.id.CloseDialog);
//            AlertDialog dialog = builder.create();
//
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
////                        product_image.setImageURI(imageIds.get(position));
//                        Glide.with(context).load(imageIds.get(position)).into(product_image);
//                        dialog.show();
//                    }
//                }
//            });
//            CloseDialog.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });

        }
    }
}
