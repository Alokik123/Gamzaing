package com.example.uiuxapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EmailScreenPopup extends BottomSheetDialogFragment {

    private static final String ARG_EMAIL = "email";
    private static final String ARG_REFERRAL_CODE = "referral_code";
    private String email;
    private String referralCode;

    public static EmailScreenPopup newInstance(String email, String referralCode) {
        EmailScreenPopup fragment = new EmailScreenPopup();
        Bundle args = new Bundle();
        args.putString(ARG_EMAIL, email);
        args.putString(ARG_REFERRAL_CODE, referralCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.email_screen_popup, container, false);
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog; // Apply the custom style with rounded corners
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                bottomSheet.setBackgroundResource(android.R.color.transparent); // Ensure transparency to show rounded corners
            }
        });
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the email and referral code arguments
        if (getArguments() != null) {
            email = getArguments().getString(ARG_EMAIL);
            referralCode = getArguments().getString(ARG_REFERRAL_CODE);
        }

        EditText referralCodeInput = view.findViewById(R.id.CouponCodeBottomSheetReferalCode);
        Button applyButton = view.findViewById(R.id.CouponCodeBottomSheetbtnApply);
        TextView errorText = view.findViewById(R.id.error_text);
        TextView successText = view.findViewById(R.id.success_text);
        TextView noCouponText = view.findViewById(R.id.CouponCodeBottomSheetbtnNoCoupnCode);

        SharedPreferences preferences = getActivity().getSharedPreferences("LoginDetails", MODE_PRIVATE);
        String userEmail = preferences.getString("user_email", "");
        Log.d("EmailScreenPopup", "User Email: " + userEmail);

        applyButton.setOnClickListener(v -> {
            String enteredReferralCode = referralCodeInput.getText().toString().trim();
            checkReferralCodeInFirebase(enteredReferralCode, referralCodeInput, errorText, successText);
        });

        noCouponText.setOnClickListener(v -> {
            // Redirect to HomeActivity
            navigateToHome();
        });
    }

    private void checkReferralCodeInFirebase(String enteredReferralCode, EditText referralCodeInput, TextView errorText, TextView successText) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("referral_codes");
        databaseReference.child(enteredReferralCode).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Referral code exists in Firebase, show success
                    showSuccessDialog();
                } else {
                    // Referral code does not exist, show error and redirect to HomeActivity
                    errorText.setVisibility(View.VISIBLE);
                    successText.setVisibility(View.GONE);
                    setEditTextBorderRed(referralCodeInput);
                    navigateToHome();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("EmailScreenPopup", "Database error: " + databaseError.getMessage());
            }
        });
    }
    private void showSuccessDialog() {
        dismiss(); // Dismiss the current popup
        SuccessDialogFragment successDialog = new SuccessDialogFragment();
        successDialog.show(getParentFragmentManager(), "SuccessDialogFragment");
    }

    private void navigateToHome() {
        // Dismiss the popup and navigate to HomeActivity
        dismiss();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish(); // Optionally finish the current activity
    }

    public class SuccessDialogFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Success")
                    .setMessage("Referral code applied successfully!")
                    .setPositiveButton("OK", (dialog, which) -> {
                        // Dismiss the dialog
                        dismiss();

                        // Navigate to HomeActivity
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        startActivity(intent);
                        getActivity().finish(); // Optionally finish the current activity
                    });

            return builder.create();
        }
    }

    private void setEditTextBorderRed(EditText editText) {
        editText.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.edittext_error));
    }
}
