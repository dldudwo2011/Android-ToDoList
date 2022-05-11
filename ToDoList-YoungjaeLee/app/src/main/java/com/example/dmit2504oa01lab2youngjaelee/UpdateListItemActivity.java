package com.example.dmit2504oa01lab2youngjaelee;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class UpdateListItemActivity extends BaseActivity implements View.OnClickListener, DeleteDialogFragment.NoticeDialogListener, FontDialogFragment.NoticeDialogListener, SetThemeBottomSheetDialog.NoticeDialogListener, SetUsernameDialogFragment.NoticeDialogListener, SetPasswordDialogFragment.NoticeDialogListener {

    String selectedItemID;
    String listNameText = MainActivity.selectedListName != null ? MainActivity.selectedListName : "Tasks";
    String description;
    String createdDateText;

    EditText descriptionInput;

    TextView createdDate;
    TextView listName;

    ImageButton backButton;
    ImageButton trashButton;
    ImageButton menuButton;

    Cursor cursor;
    SQLiteDatabase database;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_list_item);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            selectedItemID = bundle.getString("id");
            description = bundle.getString("description");
            createdDateText = "Created on " + bundle.getString("createdDate");
        }

        descriptionInput = findViewById(R.id.input_description);
        createdDate = findViewById(R.id.created_date);
        listName = findViewById(R.id.update_view_title);
        backButton = findViewById(R.id.update_and_redirect_button);
        trashButton = findViewById(R.id.trash_button);
        menuButton = findViewById(R.id.menu_button);

        if(MainActivity.fontSizeIncrease){
            listName.setTextSize(50);
        }

        if(MainActivity.selectedThemeColor != null)
        {
            listName.setTextColor(MainActivity.selectedThemeColor);
            createdDate.setTextColor(MainActivity.selectedThemeColor);
            menuButton.setColorFilter(MainActivity.selectedThemeColor);
            trashButton.setColorFilter(MainActivity.selectedThemeColor);
            backButton.setColorFilter(MainActivity.selectedThemeColor);
        }

        descriptionInput.setText(description);
        createdDate.setText(createdDateText);
        listName.setText(listNameText);

        backButton.setOnClickListener(this);
        trashButton.setOnClickListener(this);

        dbManager = new DBManager(this);
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.update_and_redirect_button:
            {
                //add the redirect or back
                updateItem();
                this.finish();
                break;
            }

            case R.id.trash_button:
            {
                showDeleteDialog();
                break;
            }
        }
    }

    public void showDeleteDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new DeleteDialogFragment();
        dialog.show(getSupportFragmentManager(), "DeleteDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        try {
            database = dbManager.getWritableDatabase();
            database.delete(dbManager.DB_TABLE2, DBManager.C_ID2 + "=" + "'" + selectedItemID + "'", null);
            database.close();
            this.finish();

            Toast.makeText(this, "Delete success", Toast.LENGTH_LONG).show();
        }

        catch (Exception e){
            Toast.makeText(this, "Error occured:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDialogClick1(DialogFragment dialog) {
        MainActivity.selectedThemeColor=hexThemeLightBlue;
        changeColor(hexThemeLightBlue);
    }

    @Override
    public void onDialogClick2(DialogFragment dialog) {
        MainActivity.selectedThemeColor=hexThemeGreen;
        changeColor(hexThemeGreen);
    }

    @Override
    public void onDialogClick3(DialogFragment dialog) {
        MainActivity.selectedThemeColor=hexThemePink;
        changeColor(hexThemePink);
    }

    @Override
    public void onFontChange(DialogFragment dialog) {
        MainActivity.header.setTextSize(50);
        listName.setTextSize(50);
        MainActivity.fontSizeIncrease = true;
    }

    public void changeColor(Integer colorHex){
        listName.setTextColor(colorHex);
        createdDate.setTextColor(colorHex);
        menuButton.setColorFilter(colorHex);
        trashButton.setColorFilter(colorHex);
        backButton.setColorFilter(colorHex);
        MainActivity.header.setTextColor(colorHex);
        MainActivity.menuButton.setColorFilter(colorHex);

        if(colorHex == Color.parseColor("#ADD8E6"))
        {
            Drawable d = getResources().getDrawable(R.drawable.button);

            ViewListItemsActivity.viewArchivedButton.setBackground(d);
        }
        if(colorHex == Color.parseColor("#90EE90"))
        {
            Drawable d = getResources().getDrawable(R.drawable.button_green);

            ViewListItemsActivity.viewArchivedButton.setBackground(d);
        }
        if(colorHex == Color.parseColor("#FFC0CB"))
        {
            Drawable d = getResources().getDrawable(R.drawable.button_pink);

            ViewListItemsActivity.viewArchivedButton.setBackground(d);
        }

        ViewListItemsActivity.listName.setTextColor(colorHex);
        ViewListItemsActivity.backButton.setColorFilter(colorHex);
        ViewListItemsActivity.optionButton.setColorFilter(colorHex);
        ViewListItemsActivity.addButton.setColorFilter(colorHex);
//        MainActivity.listIcon.setColorFilter(colorHex);
    }

    public void updateItem(){
        String newContent = descriptionInput.getText().toString();
        ContentValues values = new ContentValues();
        values.put(DBManager.C_DESCRIPTION, newContent);

        // update the database ... refresh array from the database
        database = dbManager.getWritableDatabase();
        database.update(DBManager.DB_TABLE2, values, DBManager.C_ID2 + "=" + "'" + selectedItemID + "'", null);
        database.close();
    }

    @Override
    public void onChangePassword(DialogFragment dialog) {
        // change password
        EditText editText = dialog.getDialog().findViewById(R.id.password);
        String input = editText.getText().toString();

        if(input.trim().isEmpty())
        {
            Toast.makeText(this, "The password cannot be empty", Toast.LENGTH_LONG).show();
        }
        else {
            MainActivity.password = input;
        }
    }

    @Override
    public void onChangeUsername(DialogFragment dialog) {
        // change password
        EditText editText = dialog.getDialog().findViewById(R.id.username);
        String input = editText.getText().toString();

        if(input.trim().isEmpty())
        {
            Toast.makeText(this, "The username cannot be empty", Toast.LENGTH_LONG).show();
        }
        else {
            MainActivity.username = input;
        }
    }
}
