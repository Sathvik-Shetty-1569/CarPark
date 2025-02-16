package apcoders.in.carpark;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import apcoders.in.carpark.Utils.WalletManagement;
import apcoders.in.carpark.models.UserModel;
import es.dmoral.toasty.Toasty;

public class Register extends AppCompatActivity {
    EditText edPhoneNumber, edFullName, edPassowrd, edEmail, edComfirm;
    TextView tv;
    Button btn;
    Spinner spUserType;
    String UserType = "User";
    RadioGroup radioGroup;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    CollectionReference UserscollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Initialize Firebase and other components
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build());
        UserscollectionReference = firebaseFirestore.collection("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        edFullName = findViewById(R.id.editTextRegName);
        edPhoneNumber = findViewById(R.id.editTextRegPhoneNumber);
        edEmail = findViewById(R.id.editTextRegEmail);
        edComfirm = findViewById(R.id.editTextRegComfirmPassword);
        edPassowrd = findViewById(R.id.editTextRegPassword);
        tv = findViewById(R.id.textViewExistingUser);
        btn = findViewById(R.id.buttonRegister);
        radioGroup = findViewById(R.id.radioGroup);

        isLogin();
        setRadioButtons();

        ProgressBar progressBar = findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        // Style the "Already have an account?" text in blue
        String fullText = "Already have an account?";
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(spannableString);

        // Set click listener for the "Already have an account?" text
        tv.setOnClickListener(view -> startActivity(new Intent(Register.this, LoginActivity.class)));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btn.setEnabled(false);
                String name = edFullName.getText().toString();
                String phoneNumber = edPhoneNumber.getText().toString().trim();
                String email = edEmail.getText().toString().trim();
                String password = edPassowrd.getText().toString();
                String comfirm = edComfirm.getText().toString();

                if (password.length() == 0 || password.length() == 0 || comfirm.length() == 0 || email.length() == 0) {
                    Toasty.error(getApplicationContext(), "Fill all the above details", Toasty.LENGTH_SHORT).show();
                } else {
                    if (password.compareTo(comfirm) == 0) {
                        if (isValidPassword(password)) {
                            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("RegisterActivity", "Firebase authentication successful");

                                        UserModel userModel = new UserModel(firebaseAuth.getCurrentUser().getUid(), name, phoneNumber, UserType, email);
                                        UserscollectionReference.add(userModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    btn.setEnabled(true);
                                                    Toasty.success(Register.this, "Registration Done", Toasty.LENGTH_SHORT).show();
                                                    SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putBoolean("isLoggedIn", true);
                                                    editor.putString("UserType", UserType);
                                                    editor.apply();
                                                    editor.commit();

                                                    Intent i;
                                                    if (UserType.equals("User")) {
                                                        WalletManagement.initializeWallet(firebaseAuth.getCurrentUser().getUid());
                                                        i = new Intent(Register.this, MainActivity.class);
                                                        i.putExtra("UserType", UserType);
                                                        startActivity(i);
                                                        finish();
                                                    } else if (UserType.equals("ParkingOwner")) {
                                                        WalletManagement.initializeWallet(firebaseAuth.getCurrentUser().getUid());
                                                        i = new Intent(Register.this, HostMainActivity.class);
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
                                                Log.d("TAG", "onFailure: " + e.getMessage());
                                                Toasty.error(Register.this, "Something Goes Wrong", Toasty.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.GONE);
                                    btn.setEnabled(true);
                                    Toasty.error(Register.this, "Something Goes Wrong", Toasty.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            progressBar.setVisibility(View.GONE);
                            btn.setEnabled(true);
                            Toasty.error(getApplicationContext(), "Password must contain at least 8 characters, having letter, digit and special character", Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        btn.setEnabled(true);
                        Toasty.error(getApplicationContext(), "Password and Comfirmed Password didn't match", Toasty.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isValidPassword(String password) {
        return
                password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*");
    }

    private void setRadioButtons() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBtnNormalBtn) {
                    // Handle "Normal User" selection
                    UserType = "User";
//                    Toasty.success(getApplicationContext(), "Normal User selected", Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radioBtnAuthorityBtn) {
                    // Handle "Authorities" selection
                    UserType = "ParkingOwner";
                }
            }
        });
    }

    private void isLogin() {
        try {
            if (firebaseAuth.getCurrentUser().getUid() != null) {
                if (UserType.equals("User")) {
                    Intent i = new Intent(Register.this, MainActivity.class);
                    i.putExtra("UserType", UserType);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(Register.this, HostMainActivity.class);
                    i.putExtra("UserType", UserType);
                    startActivity(i);
                    finish();
                }

            }
        } catch (Exception exception) {

        }
    }
}