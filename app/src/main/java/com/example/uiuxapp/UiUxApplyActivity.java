package com.example.uiuxapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UiUxApplyActivity extends AppCompatActivity {

    private EditText firstNameInput;
    private EditText lastNameInput;
    private Spinner hear;
    private Spinner citySpinner;

    private Button btechButton;
    private Button mtechButton;
    private Button enggButton;
    private Button commerceButton;
    private Button othersButton;

    private Button studentButton;
    private Button fresherButton;
    private Button trainingButton;
    private Button workingButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_form);

        ImageView backButton = findViewById(R.id.back_button1111);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Button applyButton = findViewById(R.id.Training_button);
        applyButton.setBackgroundResource(R.drawable.button_background);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    saveFormData();
                    Toast.makeText(UiUxApplyActivity.this, "Application submitted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UiUxApplyActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        citySpinner = findViewById(R.id.city_spinner);
        hear=findViewById(R.id.hear_about_course_spinner);

        ArrayAdapter<CharSequence> priorExperienceAdapter = ArrayAdapter.createFromResource(this,
                R.array.how_did_you_hear, android.R.layout.simple_spinner_item);
        priorExperienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hear.setAdapter(priorExperienceAdapter);

        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        btechButton = findViewById(R.id.btech);
        mtechButton = findViewById(R.id.MTech);
        enggButton = findViewById(R.id.Engg_diploma);
        commerceButton = findViewById(R.id.Commerce);
        othersButton = findViewById(R.id.others);

        // Set onClick listeners for educational background buttons
        btechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEducationButtons();
                highlightButton(btechButton);
            }
        });

        mtechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEducationButtons();
                highlightButton(mtechButton);
            }
        });

        enggButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEducationButtons();
                highlightButton(enggButton);
            }
        });

        commerceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEducationButtons();
                highlightButton(commerceButton);
            }
        });

        othersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEducationButtons();
                highlightButton(othersButton);
            }
        });

        // Initialize buttons for current status
        studentButton = findViewById(R.id.student);
        fresherButton = findViewById(R.id.Fresher);
        trainingButton = findViewById(R.id.Training_Experience);
        workingButton = findViewById(R.id.Working_Profesional);

        // Set onClick listeners for current status buttons
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStatusButtons();
                highlightButton(studentButton);
            }
        });

        fresherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStatusButtons();
                highlightButton(fresherButton);
            }
        });

        trainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStatusButtons();
                highlightButton(trainingButton);
            }
        });

        workingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStatusButtons();
                highlightButton(workingButton);
            }
        });
    }

    private boolean validateForm() {
        firstNameInput = findViewById(R.id.firstname_input);
        lastNameInput = findViewById(R.id.lastname_input);

        String firstName = firstNameInput.getText().toString().trim();
        String lastName = lastNameInput.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty()) {
            return false;
        }
        return true;
    }

    private void saveFormData() {
        // Redirect to FormSuccessfulActivity
        Intent intent = new Intent(UiUxApplyActivity.this, FormSuccessfulActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Button applyButton = findViewById(R.id.apply_button22);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            applyButton.setTextColor(getResources().getColor(R.color.white));
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            applyButton.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
        return super.onTouchEvent(event);
    }


    private void resetEducationButtons() {
        btechButton.setSelected(false);
        btechButton.setTextColor(getResources().getColor(R.color.gray));
        mtechButton.setSelected(false);
        mtechButton.setTextColor(getResources().getColor(R.color.gray));
        enggButton.setSelected(false);
        enggButton.setTextColor(getResources().getColor(R.color.gray));
        commerceButton.setSelected(false);
        commerceButton.setTextColor(getResources().getColor(R.color.gray));
        othersButton.setSelected(false);
        othersButton.setTextColor(getResources().getColor(R.color.gray));
    }

    private void resetStatusButtons() {
        studentButton.setSelected(false);
        studentButton.setTextColor(getResources().getColor(R.color.gray));
        fresherButton.setSelected(false);
        fresherButton.setTextColor(getResources().getColor(R.color.gray));
        trainingButton.setSelected(false);
        trainingButton.setTextColor(getResources().getColor(R.color.gray));
        workingButton.setSelected(false);
        workingButton.setTextColor(getResources().getColor(R.color.gray));
    }

    private void highlightButton(Button button) {
        // Highlight the selected button
        button.setSelected(true);
        button.setTextColor(getResources().getColor(R.color.white));
    }
}
