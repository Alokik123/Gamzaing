package com.example.uiuxapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import java.util.HashMap;
import java.util.Map;

public class InternshipActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, phoneInput, messageInput;
    private Spinner iAmSpinner, androidExperienceSpinner, previousInternshipSpinner, selectCitySpinner;
    private Button applyButton;
    private ViewStub studentViewStub, fresherViewStub, workingViewStub;
    private CardView studentView;
    private CardView fresherView;
    private CardView workingView;
    private ProgressBar progressBar;
    private boolean isSubmitting = false; // Flag to prevent multiple submissions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internship_details);

        firstNameEditText = findViewById(R.id.input111);
        lastNameEditText = findViewById(R.id.lastname_input1);
        phoneInput = findViewById(R.id.phone_input);
        messageInput = findViewById(R.id.message_input);
        iAmSpinner = findViewById(R.id.i_am_spinner);
        androidExperienceSpinner = findViewById(R.id.android_experience_spinner);
        previousInternshipSpinner = findViewById(R.id.previous_training_spinner);
        selectCitySpinner = findViewById(R.id.select_city_spinner);
        applyButton = findViewById(R.id.training_button);
        studentViewStub = findViewById(R.id.view_stub_student1);
        fresherViewStub = findViewById(R.id.view_stub_fresher1);
        workingViewStub = findViewById(R.id.view_stub_working1);
        progressBar = findViewById(R.id.progress_bar);

        setUpSpinners();

        iAmSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSpinnerSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    if (!isSubmitting) { // Check if not already submitting
                        isSubmitting = true;
                        submitForm();
                    }
                } else {
                    // Show Toast message instead of Snackbar
                    Toast.makeText(InternshipActivity.this, "Please fill all fields correctly", Toast.LENGTH_LONG).show();
                }
            }
        });


        ImageView backButton = findViewById(R.id.back_button2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addTextWatcher(firstNameEditText);
        addTextWatcher(lastNameEditText);
        addTextWatcher(phoneInput);
    }

    private void addTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetField(editText);
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });
    }

    private void resetField(EditText field) {
        field.setError(null);
        field.setBackgroundResource(R.drawable.edittext_normal); // Reset background
    }

    private boolean validateForm() {
        boolean isFormValid = true;

        // Validate other fields
        isFormValid = validateEditText(firstNameEditText) && isFormValid;
        isFormValid = validateEditText(lastNameEditText) && isFormValid;
        isFormValid = validatePhoneNumber() && isFormValid;

        // Validate spinners and set borders
        if (iAmSpinner.getSelectedItemPosition() == 0) {
            setSpinnerError(iAmSpinner);
            isFormValid = false;
        } else {
            resetSpinnerError(iAmSpinner);
        }

        if (androidExperienceSpinner.getSelectedItemPosition() == 0) {
            setSpinnerError(androidExperienceSpinner);
            isFormValid = false;
        } else {
            resetSpinnerError(androidExperienceSpinner);
        }

        if (selectCitySpinner.getSelectedItemPosition() == 0) {
            setSpinnerError(selectCitySpinner);
            isFormValid = false;
        } else {
            resetSpinnerError(selectCitySpinner);
        }

        // Validate view stubs
        if (studentView != null) {
            EditText studentName = studentView.findViewById(R.id.name_input);
            EditText studentCourse = studentView.findViewById(R.id.course_input);
            EditText studentYear = studentView.findViewById(R.id.year_input);

            isFormValid = validateEditText(studentName) && isFormValid;
            isFormValid = validateEditText(studentCourse) && isFormValid;
            isFormValid = validateEditText(studentYear) && isFormValid;
        }

        if (fresherView != null) {
            EditText fresherName = fresherView.findViewById(R.id.name_input1);
            EditText fresherCourse = fresherView.findViewById(R.id.course_input1);
            EditText fresherYear = fresherView.findViewById(R.id.year_of_input);

            isFormValid = validateEditText(fresherName) && isFormValid;
            isFormValid = validateEditText(fresherCourse) && isFormValid;
            isFormValid = validateEditText(fresherYear) && isFormValid;
        }

        if (workingView != null) {
            EditText workingName = workingView.findViewById(R.id.name_input2);
            EditText workingJob = workingView.findViewById(R.id.job_input);
            EditText workingEducation = workingView.findViewById(R.id.edu_input);
            EditText workingExperience = workingView.findViewById(R.id.exp_input);
            EditText workingMentorship = workingView.findViewById(R.id.mentorship_input);

            isFormValid = validateEditText(workingName) && isFormValid;
            isFormValid = validateEditText(workingJob) && isFormValid;
            isFormValid = validateEditText(workingEducation) && isFormValid;
            isFormValid = validateEditText(workingExperience) && isFormValid;
            isFormValid = validateEditText(workingMentorship) && isFormValid;
        }

        return isFormValid;
    }

    private void setSpinnerError(Spinner spinner) {
        spinner.setBackgroundResource(R.drawable.spinner_error_border);
    }

    private void resetSpinnerError(Spinner spinner) {
        spinner.setBackgroundResource(R.drawable.spinner_default_border);
    }

    private boolean validateEditText(EditText editText) {
        String text = editText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            editText.setError("This field is required");
            setFieldError(editText);
            return false;
        } else {
            resetField(editText);
            return true;
        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = phoneInput.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 10) {
            phoneInput.setError("Please enter a valid 10-digit phone number");
            setFieldError(phoneInput);
            return false;
        } else {
            resetField(phoneInput);
            return true;
        }
    }

    private void setFieldError(EditText field) {
        field.setBackgroundResource(R.drawable.edittext_error); // Set red border
    }

    private void setUpSpinners() {
        ArrayAdapter<CharSequence> iAmAdapter = ArrayAdapter.createFromResource(this,
                R.array.i_am_array, android.R.layout.simple_spinner_item);
        iAmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iAmSpinner.setAdapter(iAmAdapter);

        ArrayAdapter<CharSequence> androidExperienceAdapter = ArrayAdapter.createFromResource(this,
                R.array.android_experience_array, android.R.layout.simple_spinner_item);
        androidExperienceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        androidExperienceSpinner.setAdapter(androidExperienceAdapter);

        ArrayAdapter<CharSequence> previousInternshipAdapter = ArrayAdapter.createFromResource(this,
                R.array.previous_training_array, android.R.layout.simple_spinner_item);
        previousInternshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        previousInternshipSpinner.setAdapter(previousInternshipAdapter);

        ArrayAdapter<CharSequence> selectCityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        selectCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCitySpinner.setAdapter(selectCityAdapter);
    }

    private void handleSpinnerSelection(int position) {
        switch (position) {
            case 0:
                hideAllViewStubs();
                break;
            case 1:
                showViewStub(studentViewStub);
                break;
            case 2:
                showViewStub(fresherViewStub);
                break;
            case 3:
                showViewStub(workingViewStub);
                break;
            default:
                break;
        }
    }

    @SuppressLint("WrongViewCast")
    private void showViewStub(ViewStub viewStub) {
        hideAllViewStubs();
        View view = viewStub.inflate();
        if (viewStub == studentViewStub) {
            studentView = view.findViewById(R.id.view_stub_student1);
        } else if (viewStub == fresherViewStub) {
            fresherView = view.findViewById(R.id.view_stub_fresher1);
        } else if (viewStub == workingViewStub) {
            workingView = view.findViewById(R.id.view_stub_working1);
        }
    }

    private void hideAllViewStubs() {
        if (studentView != null) {
            studentView.setVisibility(View.GONE);
        }
        if (fresherView != null) {
            fresherView.setVisibility(View.GONE);
        }
        if (workingView != null) {
            workingView.setVisibility(View.GONE);
        }
    }

    private void submitForm() {
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://app-api.gamazingstudios.com/Routes/internship_master.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        isSubmitting = false; // Reset flag
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                "Form submitted successfully", Snackbar.LENGTH_LONG);

                        // Customize Snackbar
                        View snackbarView = snackbar.getView();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
                        params.setMargins(20, 0, 20, 205); // Adjust bottom margin as needed
                        snackbarView.setLayoutParams(params);
                        snackbar.setBackgroundTint(getResources().getColor(android.R.color.holo_green_light));
                        snackbar.show();
                        // Delay transition to next activity
                        findViewById(android.R.id.content).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(InternshipActivity.this, InternshipSucessfull.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 2000);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        isSubmitting = false; // Reset flag
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                                "Error submitting form: " + error.getMessage(), Snackbar.LENGTH_LONG);

                        // Customize Snackbar
                        View snackbarView = snackbar.getView();
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) snackbarView.getLayoutParams();
                        params.setMargins(20, 0, 20, 205); // Adjust bottom margin as needed
                        snackbarView.setLayoutParams(params);
                        snackbar.setBackgroundTint(getResources().getColor(android.R.color.holo_red_light));
                        snackbar.show();}
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // Add your parameters here
                params.put("first_name", firstNameEditText.getText().toString().trim());
                params.put("last_name", lastNameEditText.getText().toString().trim());
                params.put("phone", phoneInput.getText().toString().trim());
                params.put("message", messageInput.getText().toString().trim());
                // Add additional parameters based on view stubs if necessary
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
