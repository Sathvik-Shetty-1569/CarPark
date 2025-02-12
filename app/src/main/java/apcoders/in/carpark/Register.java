package apcoders.in.carpark;
import apcoders.in.carpark.models.AuthorityModel;
import apcoders.in.carpark.models.NormalUserModel;
import es.dmoral.toasty.Toasty;
import com.github.ybq.android.spinkit.sprite.Sprite;

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

public class Register extends AppCompatActivity {
    EditText edUsername, edFullName, edAuthorityLevel, edPassowrd, edEmail, edComfirm;
    TextView tv;
    Button btn;
    Spinner sp;
    String UserType = "Normal User";
    RadioGroup radioGroup;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    CollectionReference UserscollectionReference, AuthoritycollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.setFirestoreSettings(new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build());
        UserscollectionReference = firebaseFirestore.collection("Users");
        AuthoritycollectionReference = firebaseFirestore.collection("Authorities");
        firebaseAuth = FirebaseAuth.getInstance();
        edFullName = findViewById(R.id.editTextRegName);
//        edAuthorityLevel = findViewById(R.id.editTextRegAuthorityLevel);
        edUsername = findViewById(R.id.editTextRegUsername);
        edEmail = findViewById(R.id.editTextRegEmail);
        edComfirm = findViewById(R.id.editTextRegComfirmPassword);
        edPassowrd = findViewById(R.id.editTextRegPassword);
        tv = findViewById(R.id.textViewExistingUser);
        btn = findViewById(R.id.buttonRegister);
        radioGroup = findViewById(R.id.radioGroup);

        isLogin();
        setRadioButtons();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite doubleBounce = new Wave();
        progressBar.setIndeterminateDrawable(doubleBounce);

        tv.setOnClickListener(view -> startActivity(new Intent(Register.this, LoginActivity.class)));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btn.setEnabled(false);
                String name = edFullName.getText().toString();
                String username = edUsername.getText().toString().trim();
                String email = edEmail.getText().toString().trim();
                String password = edPassowrd.getText().toString();
                String comfirm = edComfirm.getText().toString();

                if (username.length() == 0 || password.length() == 0 || comfirm.length() == 0 || email.length() == 0) {
                    Toasty.error(getApplicationContext(), "Fill all the above details", Toasty.LENGTH_SHORT).show();
                } else {
                    if (password.compareTo(comfirm) == 0) {
                        if (isValidPassword(password)) {
                            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("RegisterActivity", "Firebase authentication successful");

                                        if (!UserType.equals("Normal User")) {
                                            AuthorityModel authorityModel = new AuthorityModel(firebaseAuth.getCurrentUser().getUid(), name, username, email, "");
                                            AuthoritycollectionReference.add(authorityModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        btn.setEnabled(true);
                                                        Toasty.success(Register.this, "Registration Done", Toasty.LENGTH_SHORT).show();
                                                        Log.d("RegisterActivity", "Registration successful. Navigating to MainActivity.");

                                                        startActivity(new Intent(Register.this, HostMainActivity.class));
                                                        finish();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressBar.setVisibility(View.GONE);
                                                    btn.setEnabled(true);
                                                    Log.d("TAG", "onFailure: " + e.getMessage());
                                                    Toasty.error(Register.this, "Something Goes Wrong", Toasty.LENGTH_SHORT).show();


                                                    SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putBoolean("isLoggedIn", true);
                                                    editor.putString("UserType", UserType);
                                                    editor.apply();

//                                                    // Navigate to MainActivity
//                                                    Intent i = new Intent(Register.this, HostMainActivity.class);
//                                                    i.putExtra("UserType", UserType);
//                                                    startActivity(i);
//                                                    finish();

                                                }
                                            });
                                        } else {
                                            NormalUserModel normalUserModel = new NormalUserModel(firebaseAuth.getCurrentUser().getUid(), name, username, email);
                                            UserscollectionReference.add(normalUserModel).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    if (task.isSuccessful()) {
                                                        progressBar.setVisibility(View.VISIBLE);
                                                        btn.setEnabled(true);
                                                        Toasty.success(Register.this, "Registration Done", Toasty.LENGTH_SHORT).show();
                                                        Intent i = new Intent(Register.this, MainActivity.class);
                                                        SharedPreferences sharedPreferences = getSharedPreferences("share_prefs", MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putBoolean("isLoggedIn", true);
                                                        editor.putString("UserType", UserType);
                                                        i.putExtra("UserType", UserType);
                                                        editor.apply();
                                                        editor.commit();

                                                        startActivity(i);
                                                        finish();
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
                                    } else {
                                        Log.d("RegisterActivity", "Firebase authentication failed: " + task.getException().getMessage());

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
        return password.length() >= 8 && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[!@#$%^&*].*");
    }


    public void isLogin() {
        try {

            if (firebaseAuth.getCurrentUser().getUid() != null) {
                Log.d("RegisterActivity", "User already logged in, redirecting to MainActivity.");

//                startActivity(new Intent(Register.this, MainActivity.class));
//                finish();
            } else {
                Log.d("RegisterActivity", "No user logged in.");

            }
        } catch (Exception exception) {

        }
    }

    private void setRadioButtons() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBtnNormalBtn) {
                    // Handle "Normal User" selection
//                    edAuthorityLevel.setVisibility(View.GONE);
                    UserType = "Normal User";
//                    Toasty.success(getApplicationContext(), "Normal User selected", Toast.LENGTH_SHORT).show();

                } else if (checkedId == R.id.radioBtnAuthorityBtn) {
                    // Handle "Authorities" selection
                    UserType = "Authorities";
//                    edAuthorityLevel.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // If password verification is successful, show admin registration fields
            edAuthorityLevel.setVisibility(View.VISIBLE);
            UserType = "Authorities";
        } else {
            // If verification fails, reset the selection to "Normal User"
            radioGroup.clearCheck();
            Toasty.error(getApplicationContext(), "Password verification failed", Toast.LENGTH_SHORT).show();
        }


    }


}