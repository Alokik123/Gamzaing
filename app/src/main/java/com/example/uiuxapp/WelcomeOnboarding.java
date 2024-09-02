package com.example.uiuxapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class WelcomeOnboarding extends AppCompatActivity {

    private EditText otpDigit1, otpDigit2, otpDigit3, otpDigit4, otpDigit5, otpDigit6;
    private Button verifyOtpButton;
    private String verificationId;
    private String phoneNumber; // Added to store phone number
    private long timeLeftInMillis = 20000;
    private TextView resendOtpText;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_onboarding);

        otpDigit1 = findViewById(R.id.otp_digit_1);
        otpDigit2 = findViewById(R.id.otp_digit_2);
        otpDigit3 = findViewById(R.id.otp_digit_3);
        otpDigit4 = findViewById(R.id.otp_digit_4);
        otpDigit5 = findViewById(R.id.otp_digit_5);
        otpDigit6 = findViewById(R.id.otp_digit_6);
        verifyOtpButton = findViewById(R.id.verify_otp_button);
        resendOtpText = findViewById(R.id.resend_otp_text);

        startTimer();

        Intent intent = getIntent();
        if (intent != null) {
            verificationId = intent.getStringExtra("verificationId");
            phoneNumber = intent.getStringExtra("phoneNumber");
        }

        setupOtpTextListeners();

        verifyOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = otpDigit1.getText().toString() +
                        otpDigit2.getText().toString() +
                        otpDigit3.getText().toString() +
                        otpDigit4.getText().toString() +
                        otpDigit5.getText().toString() +
                        otpDigit6.getText().toString();

                if (!TextUtils.isEmpty(otp)) {
                    verifyPhoneNumberWithCode(verificationId, otp);
                } else {
                    Toast.makeText(WelcomeOnboarding.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resendOtpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerRunning) {
                    resendOTP(); // Initiate resend OTP process
                    startTimer(); // Restart timer
                    Toast.makeText(WelcomeOnboarding.this, "Resending OTP...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(WelcomeOnboarding.this, "Please wait before requesting a new OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupOtpTextListeners() {
        otpDigit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but required to implement the interface
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpDigit2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed, but required to implement the interface
            }
        });

        otpDigit2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but required to implement the interface
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpDigit3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed, but required to implement the interface
            }
        });

        otpDigit3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but required to implement the interface
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpDigit4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed, but required to implement the interface
            }
        });

        otpDigit4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but required to implement the interface
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpDigit5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed, but required to implement the interface
            }
        });

        otpDigit5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but required to implement the interface
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    otpDigit6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed, but required to implement the interface
            }
        });

        otpDigit6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed, but required to implement the interface
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Optionally handle completion of OTP input here
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed, but required to implement the interface
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                resendOtpText.setText("Resend OTP");
                resendOtpText.setClickable(true);
            }
        }.start();

        timerRunning = true;
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        resendOtpText.setText("Resend OTP (" + seconds + "s)");
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Verification successful
                            Toast.makeText(WelcomeOnboarding.this, "Verification successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(WelcomeOnboarding.this, EmailActivity.class);
                            startActivity(intent);
                            finish(); // Finish current activity to prevent going back on success
                        } else {
                            // Verification failed
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(WelcomeOnboarding.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            } else {
                                // Any other error occurred during verification
                                Toast.makeText(WelcomeOnboarding.this, "Verification failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void resendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, // Use the phone number obtained from previous activity
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // Auto-retrieval or instant verification completed
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        // This method will be invoked if an invalid request for verification is made,
                        // for instance if the phone number format is not valid.
                        Toast.makeText(WelcomeOnboarding.this, "Failed to resend OTP: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String newVerificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
// Code successfully sent to phone
                        verificationId = newVerificationId;
                        Toast.makeText(WelcomeOnboarding.this, "OTP Resent", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


