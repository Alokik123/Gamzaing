package com.example.uiuxapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MentorActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, educationEditText, expEditText,
            workExpEditText, mentorExpEditText, currentCompanyEditText, roleEditText;
    private Spinner citySpinner, companySpinner, mentorSpinner;
    private ProgressBar progressBar;
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mentor_details);

        progressBar = findViewById(R.id.progress_bar_2);
        applyButton = findViewById(R.id.mentor_button);

        // Initialize views
        firstNameEditText = findViewById(R.id.name_input22233);
        lastNameEditText = findViewById(R.id.lastname_input);
        educationEditText = findViewById(R.id.edu_back1);
        expEditText = findViewById(R.id.exp_card);
        workExpEditText = findViewById(R.id.years_input);
        mentorExpEditText = findViewById(R.id.years_input2);
        currentCompanyEditText = findViewById(R.id.name_input);
        roleEditText = findViewById(R.id.role_input);
        citySpinner = findViewById(R.id.city_spinner);
        companySpinner = findViewById(R.id.spinner_company_status);
        mentorSpinner = findViewById(R.id.mentor_spinner);

        addTextWatcher(firstNameEditText);
        addTextWatcher(lastNameEditText);
        addTextWatcher(educationEditText);
        addTextWatcher(expEditText);
        addTextWatcher(workExpEditText);
        addTextWatcher(mentorExpEditText);
        addTextWatcher(currentCompanyEditText);
        addTextWatcher(roleEditText);

        // Set up adapters for spinners
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.city_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> companyAdapter = ArrayAdapter.createFromResource(this,
                R.array.company_status_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> mentorAdapter = ArrayAdapter.createFromResource(this,
                R.array.mentorship_experience_array, android.R.layout.simple_spinner_item);

        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mentorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        citySpinner.setAdapter(cityAdapter);
        companySpinner.setAdapter(companyAdapter);
        mentorSpinner.setAdapter(mentorAdapter);

        // Set up spinner listeners
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle city selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle company status selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        mentorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle mentorship experience selection if needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set up apply button click listener
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldsEmpty()) {
                    showSnackbar("Please fill all fields", false, null);
                } else {
                    // Save form data and proceed
                    saveFormData();
                }
            }
        });

        // Set up back button click listener
        ImageView backButton = findViewById(R.id.back_button123);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void showSnackbar(String message, boolean isSuccess, Runnable action) {
        // Find the root view
        View rootView = findViewById(android.R.id.content);

        // Create a Snackbar
        Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);

        // Access the Snackbar's layout and set the text alignment
        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = snackbarLayout.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);  // Center the text

        // Set the Snackbar's background color based on success or failure
        int backgroundColor = isSuccess ? getResources().getColor(R.color.holo_light) : getResources().getColor(R.color.holo_dark);
        snackbarLayout.setBackgroundColor(backgroundColor);

        // Set custom margins
        ViewGroup.LayoutParams layoutParams = snackbarLayout.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.setMargins(20, 0, 20, 205); // Adjust bottom margin as needed
            snackbarLayout.setLayoutParams(marginLayoutParams);
        }
        // Set the action if provided
        if (action != null) {
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    action.run();
                }
            });
        }

        // Show the Snackbar
        snackbar.show();

        // If it's a success message, add a callback to navigate to the new activity after a delay
        if (isSuccess) {
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    super.onDismissed(snackbar, event);
                    // Intent to navigate to another activity
                    Intent intent = new Intent(MentorActivity.this, MentorSucessfull.class);
                    startActivity(intent);
                    // No need to finish the current activity if you want to keep it visible
                }
            });
        }
    }



    private void addTextWatcher(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If text is entered, reset background to normal
                editText.setBackgroundResource(R.drawable.edittext_normal);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }

    private boolean areFieldsEmpty() {
        boolean hasEmptyFields = false;

        // Array of all EditText fields to check
        EditText[] editTexts = {
                firstNameEditText, lastNameEditText, educationEditText, expEditText,
                workExpEditText, mentorExpEditText, currentCompanyEditText, roleEditText
        };

        // Iterate through each EditText field and check if empty
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText().toString().trim())) {
                editText.setError("Field is required");
                editText.setBackgroundResource(R.drawable.edittext_error);
                hasEmptyFields = true;
            } else {
                editText.setBackgroundResource(R.drawable.edittext_normal); // Reset background if not empty
            }
        }

        // Check if Spinner fields have a valid selection
        if (citySpinner.getSelectedItemPosition() == 0) { // Assuming first item is default
            citySpinner.setBackgroundResource(R.drawable.spinner_error_border);
            showSnackbar("Please select a city", false, null);
            hasEmptyFields = true;
        } else {
            citySpinner.setBackgroundResource(R.drawable.spinner_default_border); // Reset background if valid
        }

        if (companySpinner.getSelectedItemPosition() == 0) { // Assuming first item is default
            companySpinner.setBackgroundResource(R.drawable.spinner_error_border);
            showSnackbar("Please select a company status", false, null);
            hasEmptyFields = true;
        } else {
            companySpinner.setBackgroundResource(R.drawable.spinner_default_border); // Reset background if valid
        }

        if (mentorSpinner.getSelectedItemPosition() == 0) { // Assuming first item is default
            mentorSpinner.setBackgroundResource(R.drawable.spinner_error_border);
            showSnackbar("Please select mentorship experience", false, null);
            hasEmptyFields = true;
        } else {
            mentorSpinner.setBackgroundResource(R.drawable.spinner_default_border); // Reset background if valid
        }

        return hasEmptyFields;
    }

    private void saveFormData() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        // Keep the apply button visible
        applyButton.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        final String authToken = sharedPreferences.getString("authToken", "");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://app-api.gamazingstudios.com/Routes/mentor_master.php";

        JSONObject postData = new JSONObject();
        try {
            postData.put("first_name", firstNameEditText.getText().toString().trim());
            postData.put("last_name", lastNameEditText.getText().toString().trim());
            postData.put("education", educationEditText.getText().toString().trim());
            postData.put("exp", expEditText.getText().toString().trim());
            postData.put("work_experience", workExpEditText.getText().toString().trim());
            postData.put("mentor_experience", mentorExpEditText.getText().toString().trim());
            postData.put("current_company", currentCompanyEditText.getText().toString().trim());
            postData.put("role", roleEditText.getText().toString().trim());
            postData.put("city", citySpinner.getSelectedItem().toString());
            postData.put("company_status", companySpinner.getSelectedItem().toString());
            postData.put("mentorship_experience", mentorSpinner.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            showSnackbar("Error preparing data", false, null);
            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        showSnackbar("Data sent successfully", true, null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        showSnackbar("Failed to send data", false, null);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("data", postData.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + authToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

}
