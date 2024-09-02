package com.example.uiuxapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OtpOnboarding extends AppCompatActivity {

    private EditText phoneNumberEditText;
    private Button sendOtpButton, button_1;
    private Spinner countryCodeSpinner;
    private String selectedCountryCode;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mVerificationId;
    private ProgressBar progressBar; // ProgressBar
    private FirebaseFirestore firestore; // Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_onboarding);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance(); // Initialize Firestore
        phoneNumberEditText = findViewById(R.id.phone_number_edittext);
        sendOtpButton = findViewById(R.id.send_otp_button);
        button_1 = findViewById(R.id.b111); // Initialize button_1
        countryCodeSpinner = findViewById(R.id.country_code_spinner);
        progressBar = findViewById(R.id.progress_bar); // Initialize ProgressBar

        // Check if the user is already authenticated
        if (mAuth.getCurrentUser() != null) {
            // Redirect to EmailActivity if the user is already authenticated
            Intent intent = new Intent(OtpOnboarding.this, EmailActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Replace with your actual list of flag resources
        int[] flags = {R.drawable.india, R.drawable.brazil}; // Add more as needed
        CountryCodeAdapter adapter = new CountryCodeAdapter(this, flags);
        countryCodeSpinner.setAdapter(adapter);

        // Set India as the main country by default
        countryCodeSpinner.setSelection(0); // Adjust this based on your default country

        countryCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCountryCode = "+" + (position == 0 ? "91" : "55"); // Set country code based on position (India or Brazil)
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        sendOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(phoneNumber)) {
                    progressBar.setVisibility(View.VISIBLE); // Show ProgressBar
                    String completePhoneNumber = selectedCountryCode + phoneNumber;
                    checkIfPhoneNumberVerified(completePhoneNumber);
                } else {
                    Toast.makeText(OtpOnboarding.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtpOnboarding.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkIfPhoneNumberVerified(String phoneNumber) {
        firestore.collection("users")
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.firestore.QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // Phone number is already verified
                            progressBar.setVisibility(View.GONE); // Hide ProgressBar
                            Intent intent = new Intent(OtpOnboarding.this, EmailActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Phone number is not verified, proceed to send OTP
                            sendOTP(phoneNumber);
                        }
                    }
                });
    }

    private void sendOTP(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        Toast.makeText(OtpOnboarding.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        mVerificationId = verificationId;
                        mResendToken = token;

                        Intent intent = new Intent(OtpOnboarding.this, WelcomeOnboarding.class);
                        intent.putExtra("verificationId", mVerificationId);
                        intent.putExtra("phoneNumber", phoneNumber); // Pass the phone number to verification activity
                        startActivity(intent);
                    }
                });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        progressBar.setVisibility(View.GONE); // Hide ProgressBar
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(OtpOnboarding.this, EmailActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(OtpOnboarding.this, "Invalid code.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
