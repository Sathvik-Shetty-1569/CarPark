package apcoders.in.carpark.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import apcoders.in.carpark.R;

public class ProductImageAdapter extends RecyclerView.Adapter<ProductImageAdapter.SliderViewHolder> {

    private final List<Uri> imageUris;
    private final Context context;

    public ProductImageAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_image, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        Uri imageUri = imageUris.get(position);
        Log.d("TAG", "Loading Image: " + imageUri);

        Picasso.get()
                .load(imageUri)
                .placeholder(R.drawable.app_logo) // Add a placeholder image
                .error(R.drawable.car_icon) // Add an error fallback image
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
        return imageUris.size();
    }

    public static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewProduct);
        }
    }
}
