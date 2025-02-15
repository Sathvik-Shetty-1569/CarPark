package apcoders.in.carpark;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import es.dmoral.toasty.Toasty;
//
//import com.github.ybq.android.spinkit.sprite.Sprite;
//import com.github.ybq.android.spinkit.style.Wave;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;

//import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    EditText edEmail, edPassowrd;
    TextView tv;
    Button btn;
    String UserType = "User";
    FirebaseAuth firebaseAuth;
    RadioGroup radioGroup;
    FirebaseFirestore firebaseFirestore;
    CollectionReference UserscollectionReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Initialize Firebase and other components
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build());
        UserscollectionReference = firebaseFirestore.collection("Users");

        edEmail = findViewById(R.id.editTextLoginUsername);
        edPassowrd = findViewById(R.id.editTextLoginPassword);
        tv = findViewById(R.id.textViewNewUser);
        btn = findViewById(R.id.buttonLogin);
        radioGroup = findViewById(R.id.radioGroup);

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
        UserType = sharedPreferences.getString("UserType", "User"); // Default: Normal User
        setRadioButtons();
        isLogin();

        // Check if user is already logged in
        try {
            if (firebaseAuth.getCurrentUser().getUid() != null) {
                navigateToHome();
            }
        } catch (Exception exception) {
            // Handle exception
        }

        // Style the "then register" part of the text in blue
        String fullText = "If not signed in, then register.";
        SpannableString spannableString = new SpannableString(fullText);
        int startIndex = fullText.indexOf("then register");
        int endIndex = startIndex + "then register".length();
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);

        // Set click listener for the "then register" text
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        // Set click listener for the login button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                btn.setEnabled(false);
                String email = edEmail.getText().toString();
                String password = edPassowrd.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.GONE);
                    btn.setEnabled(true);
                    Toasty.error(LoginActivity.this, "Fill All The Fields", Toasty.LENGTH_LONG).show();
                } else {
                    UserscollectionReference.whereEqualTo("email", email.toLowerCase()).whereEqualTo("userRole", UserType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().getDocuments().size() > 0) {
                                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                btn.setEnabled(true);
                                                Toasty.success(LoginActivity.this, "Login Successful", Toasty.LENGTH_LONG).show();

                                                SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                                if (UserType.equals("User")) {
                                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                    editor.putBoolean("isLoggedIn", true);
                                                    editor.putString("UserType", UserType);
                                                    editor.apply();
                                                    i.putExtra("UserType", UserType);
                                                    startActivity(i);
                                                    finish();
                                                } else if (UserType.equals("ParkingOwner")) {
                                                    Intent i = new Intent(LoginActivity.this, HostMainActivity.class);
                                                    editor.putBoolean("isLoggedIn", true);
                                                    editor.putString("UserType", UserType);
                                                    editor.apply();
                                                    i.putExtra("UserType", UserType);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.GONE);
                                            btn.setEnabled(true);
                                            Toasty.error(LoginActivity.this, "Invalid Credentials", Toasty.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    btn.setEnabled(true);
                                    Toasty.error(LoginActivity.this, "Check Your Operating Role", Toasty.LENGTH_LONG).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            btn.setEnabled(true);
                            Toasty.error(LoginActivity.this, "Check Your Operating Role", Toasty.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void navigateToHome() {
    }

    private void isLogin() {
    }

    private void setRadioButtons() {

        if (UserType.equals("User")) {
            radioGroup.check(R.id.radioBtnNormalBtn);
//            EdAuthorityLevel.setVisibility(View.GONE);
        } else {
            radioGroup.check(R.id.radioBtnAuthorityBtn);
//            EdAuthorityLevel.setVisibility(View.VISIBLE);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBtnNormalBtn) {
                    // Handle "Normal User" selection
//                    EdAuthorityLevel.setVisibility(View.GONE);
                    UserType = "User";
//                    Toast.makeText(getApplicationContext(), "Normal User selected", Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radioBtnAuthorityBtn) {
                    // Handle "Authorities" selection
//                    EdAuthorityLevel.setVisibility(View.VISIBLE);
                    UserType = "ParkingOwner";
//                    Toast.makeText(getApplicationContext(), "Authorities selected", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}