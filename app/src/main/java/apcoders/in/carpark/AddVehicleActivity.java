package apcoders.in.carpark;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import apcoders.in.carpark.Utils.VehicleManagement;
import es.dmoral.toasty.Toasty;

public class AddVehicleActivity extends AppCompatActivity {
    EditText VehicleNumberEditText, VehicleTypeEditText, VehicleBrandEditText, VehicleModelEditText, VehicleColorEditText;
    Button AddVehicleBtn;
    String VehicleNumber, VehicleType, VehicleBrand, VehicleModel, VehicleColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_vehicle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        VehicleNumberEditText = findViewById(R.id.VehicleNumber);
        VehicleTypeEditText = findViewById(R.id.VehicleType);
        VehicleBrandEditText = findViewById(R.id.VehicleBrand);
        VehicleModelEditText = findViewById(R.id.VehicleModel);
        VehicleColorEditText = findViewById(R.id.VehicleColor);

        AddVehicleBtn = findViewById(R.id.AddVehicleBtn);
        AddVehicleBtn.setOnClickListener(v -> {
            VehicleNumber = VehicleNumberEditText.getText().toString();
            VehicleType = VehicleTypeEditText.getText().toString();
            VehicleBrand = VehicleBrandEditText.getText().toString();
            VehicleModel = VehicleModelEditText.getText().toString();
            VehicleColor = VehicleColorEditText.getText().toString();

            if (TextUtils.isEmpty(VehicleNumber) || TextUtils.isEmpty(VehicleType) || TextUtils.isEmpty(VehicleBrand) || TextUtils.isEmpty(VehicleModel) || TextUtils.isEmpty(VehicleColor)) {
                Toasty.error(AddVehicleActivity.this, "Please fill all the fields", Toasty.LENGTH_SHORT).show();
                return;
            } else {
                VehicleManagement.AddVehicle(VehicleNumber, VehicleType, VehicleBrand, VehicleModel, VehicleColor, new VehicleManagement.isVehicleAdded() {
                    @Override
                    public void onCallback(boolean isVehicleAdded) {
                        if (isVehicleAdded) {
                            Toasty.success(AddVehicleActivity.this, "Vehicle added successfully", Toasty.LENGTH_SHORT).show();
                        }else{
                            Toasty.error(AddVehicleActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}