package com.example.dmit2504oa01lab2youngjaelee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.fragment.app.DialogFragment;

public class NewListDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View v = inflater.inflate(R.layout.custom_dialog_new_list, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(v)
                // Add action buttons
                .setPositiveButton(R.string.create_list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onCreateNewList(NewListDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onCancel(NewListDialogFragment.this);
                    }
                });

        ImageButton color_main = v.findViewById(R.id.color_main);
        ImageButton color_second = v.findViewById(R.id.color_second);
        ImageButton color_third = v.findViewById(R.id.color_third);

        color_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onNewListDialogClick1(NewListDialogFragment.this);
            }
        });

        color_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onNewListDialogClick2(NewListDialogFragment.this);
            }
        });

        color_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onNewListDialogClick3(NewListDialogFragment.this);
            }
        });
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onCreateNewList(DialogFragment dialog);
        public void onCancel(DialogFragment dialog);
        public void onNewListDialogClick1(DialogFragment dialog);
        public void onNewListDialogClick2(DialogFragment dialog);
        public void onNewListDialogClick3(DialogFragment dialog);
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
            throw new ClassCastException(ViewListItemsActivity.class
                    + " must implement`DialogListener");
        }
    }
}
