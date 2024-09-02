package com.example.uiuxapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SuccessDialogFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.success_dialog, container, false);

        Button doneButton = view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(v -> {
            // Close the dialog and redirect to HomeActivity
            dismiss();
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            dialog.setOnShowListener(dialogInterface -> {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    bottomSheet.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.rounded_dialog_top));
                }
            });
        }
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog; // Use the custom style
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        // Redirect to HomeActivity after dismissing the dialog
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
    }
}
