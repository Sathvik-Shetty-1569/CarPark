package apcoders.in.carpark;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Wishlist_Parking_Areas_Activity extends AppCompatActivity {
    RecyclerView wishlisted_products_recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist_parking_areas);
//        toolbar = findViewById(R.id.toolbar);
//
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        wishlisted_products_recyclerView = findViewById(R.id.wishlisted_products_recyclerView);

//        Set<String> CartProductIds = CartManagement.getCartProductIds(Wishlist_Parking_Areas_Activity.this);
//        if (CartProductIds != null) {
//            Log.d("Shopping Cart Activity", "Cart Have Ids" + CartProductIds);
//            CartManagement.GetProductDataById(Wishlist_Parking_Areas_Activity.this, CartProductIds, new CartManagement.GetProductDataByIdCallback() {
//                @Override
//                public void onCallback(ArrayList<ProductModel> ProductDataList) {
//                    Log.d("onCallback: ", ProductDataList.size() + "");
//                    wishlisted_products_recyclerView.setLayoutManager(new GridLayoutManager(Wishlist_Parking_Areas_Activity.this, 2));
//                    WishlistProductAdapter adapter = new WishlistProductAdapter(Wishlist_Parking_Areas_Activity.this, ProductDataList);
//                    wishlisted_products_recyclerView.setAdapter(adapter);
//                }
//            });
//
//        } else {
//            Log.d("Shopping Cart Activity", "Cart Is Empty");
//        }
    }

}