package com.example.dmit2504oa01lab2youngjaelee;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SetThemeBottomSheetDialog extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.custom_dialog_theme,
                container, false);

        ImageButton color_main = v.findViewById(R.id.color_main);
        ImageButton color_second = v.findViewById(R.id.color_second);
        ImageButton color_third = v.findViewById(R.id.color_third);

        color_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onDialogClick1(SetThemeBottomSheetDialog.this);
            }
        });

        color_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onDialogClick2(SetThemeBottomSheetDialog.this);
            }
        });

        color_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onDialogClick3(SetThemeBottomSheetDialog.this);
            }
        });

        return v;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onDialogClick1(DialogFragment dialog);
        public void onDialogClick2(DialogFragment dialog);
        public void onDialogClick3(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(UpdateListItemActivity.class
                    + " must implement Delete`DialogListener");
        }
    }
}
