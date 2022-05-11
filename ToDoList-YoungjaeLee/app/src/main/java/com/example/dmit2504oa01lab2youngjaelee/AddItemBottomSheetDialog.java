package com.example.dmit2504oa01lab2youngjaelee;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.WindowManager;

public class AddItemBottomSheetDialog extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.custom_dialog_add_item,
                container, false);



        ImageButton addButton = v.findViewById(R.id.add_new_item);

//        EditText editText =  v.findViewById(R.id.new_task_description);
//
//        showSoftKeyboard(editText);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listener.onClickAdd(AddItemBottomSheetDialog.this);
            }
        });

        return v;
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onClickAdd(DialogFragment dialog);
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

//    public void showSoftKeyboard(View view) {
//        if(view.requestFocus()){
//            InputMethodManager imm =(InputMethodManager)
//                    getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
//        }
//    }

}
