package apcoders.in.carpark;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;

public class SubscriptionActivity extends AppCompatActivity {
    Button mini_plan_btn, oneMonth_btn, twoMonth_btn;
    String PlanName, PlanStatus;
    double PlanPrice;
    int PlanDuration;

    Date StartDate, EndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_subscription);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mini_plan_btn = findViewById(R.id.mini_plan_btn);
        oneMonth_btn = findViewById(R.id.goldplan);
        twoMonth_btn = findViewById(R.id.premiumpack);

        mini_plan_btn.setOnClickListener(view -> {
            PlanName = "Mini";
            PlanPrice = 1500;
            PlanDuration = 7;
            PlanStatus = "Active";
            StartDate = new Date();
            EndDate = new Date(StartDate.getTime() + PlanDuration * 24 * 60 * 60 * 1000);
            saveData();
        });
        oneMonth_btn.setOnClickListener(view -> {
            PlanName = "OneMonth";
            PlanPrice = 2500;
            PlanDuration = 28;
            PlanStatus = "Active";
            StartDate = new Date();
            EndDate = new Date(StartDate.getTime() + PlanDuration * 24 * 60 * 60 * 1000);
            saveData();
        });
        twoMonth_btn.setOnClickListener(view -> {
            PlanName = "TwoMonth";
            PlanPrice = 5000;
            PlanDuration = 56;
            PlanStatus = "Active";
            StartDate = new Date();
            EndDate = new Date(StartDate.getTime() + PlanDuration * 24 * 60 * 60 * 1000);
            saveData();
        });
    }

    private void saveData() {
        
    }


}