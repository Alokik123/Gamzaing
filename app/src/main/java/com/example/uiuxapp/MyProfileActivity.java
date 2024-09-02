package com.example.uiuxapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MyProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, numberEditText, dobEditText;
    private Button applyButton;
    private CardView nameCard, emailCard, numberCard, dobCard;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        // Initialize EditTexts
        nameEditText = findViewById(R.id.name_edittext_profile);
        emailEditText = findViewById(R.id.email_edittext_profile);
        numberEditText = findViewById(R.id.number_edittext_profile);
        dobEditText = findViewById(R.id.dob_edittext_profile);

        // Initialize Cards for validation feedback
        nameCard = findViewById(R.id.name_card_profile);
        emailCard = findViewById(R.id.email_card_profile);
        numberCard = findViewById(R.id.number_card_profile);
        dobCard = findViewById(R.id.dob_card_profile);

        // Initialize Apply Button
        applyButton = findViewById(R.id.apply_button_card);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        // Setup back button in the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void applyChanges() {
        // Reset error messages
        nameEditText.setError(null);
        emailEditText.setError(null);
        numberEditText.setError(null);
        dobEditText.setError(null);

        // Get values from EditTexts
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String number = numberEditText.getText().toString().trim();
        String dob = dobEditText.getText().toString().trim();

        // Validate fields
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(number)) {
            numberEditText.setError("Number is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(dob)) {
            dobEditText.setError("Date of Birth is required");
            isValid = false;
        }

        // If all fields are valid, proceed with applying changes
        if (isValid) {
            // Perform your apply logic here (save data, update profile, etc.)
            Toast.makeText(this, "Changes applied successfully", Toast.LENGTH_SHORT).show();
            // Example: saveProfileChanges(name, email, number, dob);
        } else {
            // Scroll to the first field with an error (optional)
            scrollToFirstError();
        }
    }

    private void scrollToFirstError() {
        // Implement scrolling logic if needed (e.g., ScrollView.scrollTo())
        // For simplicity, you can focus on the first field with an error
        if (nameEditText.getError() != null) {
            nameCard.requestFocus();
        } else if (emailEditText.getError() != null) {
            emailCard.requestFocus();
        } else if (numberEditText.getError() != null) {
            numberCard.requestFocus();
        } else if (dobEditText.getError() != null) {
            dobCard.requestFocus();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back button click
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
