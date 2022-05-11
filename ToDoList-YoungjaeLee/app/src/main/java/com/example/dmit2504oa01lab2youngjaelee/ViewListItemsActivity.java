package com.example.dmit2504oa01lab2youngjaelee;

import androidx.fragment.app.DialogFragment;

import android.app.LauncherActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



public class ViewListItemsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AddItemBottomSheetDialog.NoticeDialogListener, NewListDialogFragment.NoticeDialogListener, FontDialogFragment.NoticeDialogListener, SetThemeBottomSheetDialog.NoticeDialogListener,SetUsernameDialogFragment.NoticeDialogListener, SetPasswordDialogFragment.NoticeDialogListener{

    ListItemsAdapter adapter;
    ListView listView;
    Cursor cursor;
    SQLiteDatabase database;
    DBManager dbManager;
    static final String TAG = "CursorActivity";

    String listNameText;

    String listUUID;

    public static TextView listName;

    public static Button viewArchivedButton;
    public static ImageButton addButton;
    public static ImageButton backButton;
    public static ImageButton optionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        Bundle bundle = getIntent().getExtras();
        listName = findViewById(R.id.title_list_view);

        if(bundle != null){
            MainActivity.selectedListName = null;
            MainActivity.selectedListUUID = null;
            listName.setText(bundle.getString("newList"));
            showNewListDialog();
        }

        else {
            listNameText = MainActivity.selectedListName;
            listUUID = MainActivity.selectedListUUID;
            listName.setText(listNameText);
        }

        listView = findViewById(R.id.list);
        listView.setOnItemClickListener(this);

        viewArchivedButton = findViewById(R.id.view_archived_button);
        addButton = findViewById(R.id.add_button);
        backButton = findViewById(R.id.redirects_to_main_back_button);
        optionButton = findViewById(R.id.menu_button);

        viewArchivedButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        if(MainActivity.fontSizeIncrease){
            listName.setTextSize(50);
        }

        if(MainActivity.selectedThemeColor != null)
        {
            listName.setTextColor(MainActivity.selectedThemeColor);
            backButton.setColorFilter(MainActivity.selectedThemeColor);
            optionButton.setColorFilter(MainActivity.selectedThemeColor);

            if(MainActivity.selectedThemeColor == Color.parseColor("#ADD8E6"))
            {
                Drawable d = getResources().getDrawable(R.drawable.button);
                viewArchivedButton.setBackground(d);
            }
            if(MainActivity.selectedThemeColor == Color.parseColor("#90EE90"))
            {
                Drawable d = getResources().getDrawable(R.drawable.button_green);
                viewArchivedButton.setBackground(d);
            }
            if(MainActivity.selectedThemeColor == Color.parseColor("#FFC0CB"))
            {
                Drawable d = getResources().getDrawable(R.drawable.button_pink);
                viewArchivedButton.setBackground(d);
            }

            addButton.setColorFilter(MainActivity.selectedThemeColor);
        }

        dbManager = new DBManager(this);
    }


    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            //might need to pass archive list ID or name to get it from the correct ones
            case R.id.view_archived_button:
            {
                Intent intent = new Intent(this, ViewArchivedItemsActivity.class);
                this.startActivity(intent);
                break;
            }

            case R.id.add_button:
            {
                showAddItemBottomSheetDialog();
                break;
            }

            case R.id.redirects_to_main_back_button:
            {
                this.finish();
                break;
            }
        }
    }

    //apply where clause for UUID
    @Override
    protected void onResume()
    {
        database = dbManager.getReadableDatabase();
        cursor = database.query(DBManager.DB_TABLE2, null, DBManager.C_ASSOCIATED_LIST + "=" + "'" + MainActivity.selectedListName + "'", null, null, null, DBManager.C_CREATED_DATE + " DESC");
        startManagingCursor(cursor);
        adapter = new ListItemsAdapter(this, cursor);
        listView.setAdapter(adapter);
        Log.d(TAG, "onResume()");
        super.onResume();
        stopManagingCursor(cursor);
    }

    //pass data from DB or text in elements?
    @Override
    public void onItemClick(AdapterView<?> parent, View row, int position, long id)
    {

        TextView itemId = row.findViewById(R.id.custom_row_list_item_UUID);
        TextView description = row.findViewById(R.id.custom_row_content);
        TextView createdDate = row.findViewById(R.id.custom_row_post_date);

        Intent intent = new Intent(this, UpdateListItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", itemId.getText().toString());
        bundle.putString("description", description.getText().toString());
        bundle.putString("createdDate", createdDate.getText().toString());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    private class archiveItem extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... strings)
        {
            String associatedList = strings[0];
            String description = strings[1];
            String createdDate = strings[2];
            String id = strings[3];

            try
            {
                HttpClient client = new DefaultHttpClient();
                HttpPost request = new HttpPost("http://www.youcode.ca/Lab02Post.jsp");
                List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                postParameters.add(new BasicNameValuePair("LIST_TITLE", associatedList));
                postParameters.add(new BasicNameValuePair("CONTENT", description));
                postParameters.add(new BasicNameValuePair("COMPLETED_FLAG", "1"));
                postParameters.add(new BasicNameValuePair("ALIAS", MainActivity.username));
                postParameters.add(new BasicNameValuePair("PASSWORD", MainActivity.password));
                postParameters.add(new BasicNameValuePair("CREATED_DATE", createdDate));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                request.setEntity(formEntity);
                HttpResponse response = client.execute(request);
                Log.d(TAG, "Post in AsyncTask succeeded");

                deleteItemFromDatabase(id);
                System.out.println(id);

            }
            catch(Exception e)
            {
                // ignore exception
                Log.d(TAG, "error" + e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            onResume();
        }

    }

    public void showAddItemBottomSheetDialog() {
        // Create an instance of the dialog fragment and show it
        AddItemBottomSheetDialog dialog = new AddItemBottomSheetDialog();

        dialog.show(getSupportFragmentManager(), "AddItemBottomSheetDialogFragment");

    }

    public void showNewListDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NewListDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), "NewListDialogDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface

    @Override
    public void onClickAdd(DialogFragment dialog) {
        // User touched the dialog's positive button
        try {
            EditText editText =  dialog.getDialog().findViewById(R.id.new_task_description);

            String description = editText.getText().toString();

            if(description.trim().isEmpty())
            {
                Toast.makeText(this, "The task name cannot be empty", Toast.LENGTH_LONG).show();
                return;
            }

            addNewItemToDatabase(description);

            onResume();

            Toast.makeText(this, "Add success", Toast.LENGTH_LONG).show();
        }

        catch (Exception e){
            Toast.makeText(this, "Error occured:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancel(DialogFragment dialog) {
        // User touched the dialog's positive button
        try {
            this.finish();
        }

        catch (Exception e){
            Toast.makeText(this, "Error occured:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateNewList(DialogFragment dialog) {
        // User touched the dialog's positive button
        try {
            EditText editText =  dialog.getDialog().findViewById(R.id.name_of_the_new_list);

            String newListName = editText.getText().toString();

            String newID = addNewListToDatabase(newListName);

            listName.setText(newListName);
            MainActivity.selectedListName = newListName;
            MainActivity.selectedListUUID = newID;

            Toast.makeText(this, "Add success", Toast.LENGTH_LONG).show();
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
    public void onNewListDialogClick1(DialogFragment dialog) {
        MainActivity.selectedThemeColor=hexThemeLightBlue;
        changeColor(hexThemeLightBlue);
    }

    @Override
    public void onNewListDialogClick2(DialogFragment dialog) {
        MainActivity.selectedThemeColor=hexThemeGreen;
        changeColor(hexThemeGreen);
    }

    @Override
    public void onNewListDialogClick3(DialogFragment dialog) {
        MainActivity.selectedThemeColor=hexThemePink;
        changeColor(hexThemePink);
    }

    @Override
    public void onFontChange(DialogFragment dialog) {
        listName.setTextSize(50);
        MainActivity.header.setTextSize(50);
        MainActivity.fontSizeIncrease = true;
    }

    public void changeColor(Integer colorHex){
        listName.setTextColor(colorHex);
        backButton.setColorFilter(colorHex);
        optionButton.setColorFilter(colorHex);
        viewArchivedButton.setBackgroundColor(colorHex);
        addButton.setColorFilter(colorHex);
        MainActivity.header.setTextColor(colorHex);
        MainActivity.menuButton.setColorFilter(colorHex);

        if(colorHex == Color.parseColor("#ADD8E6"))
        {
            Drawable d = getResources().getDrawable(R.drawable.button);

            viewArchivedButton.setBackground(d);
        }
        if(colorHex == Color.parseColor("#90EE90"))
        {
            Drawable d = getResources().getDrawable(R.drawable.button_green);

            viewArchivedButton.setBackground(d);
        }
        if(colorHex == Color.parseColor("#FFC0CB"))
        {
            Drawable d = getResources().getDrawable(R.drawable.button_pink);

            viewArchivedButton.setBackground(d);
        }
//        MainActivity.listIcon.setColorFilter(colorHex);
    }


    public void deleteItemFromDatabase(String itemID) {
        // User touched the dialog's positive button
        try {
            database = dbManager.getWritableDatabase();
            database.delete(dbManager.DB_TABLE2, DBManager.C_ID2 + "=" + "'" + itemID + "'", null);
            database.close();

            Log.d(TAG, "Delete in database succeeded");
        }

        catch (Exception e){
            Toast.makeText(this, "Error occured:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String addNewListToDatabase(String newListName) {
        // User touched the dialog's positive button
        try {
            String newID = UUID.randomUUID().toString();
            String newName = newListName;

            ContentValues values = new ContentValues();
            values.put(DBManager.C_ID, newID);
            values.put(DBManager.C_NAME, newName);

            database = dbManager.getWritableDatabase();
            database.insert(dbManager.DB_TABLE, null, values);
            database.close();

            return newID;
        }

        catch (Exception e){
            Toast.makeText(this, "Error occured:" + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    public void addNewItemToDatabase(String newItemName) {
        // User touched the dialog's positive button
        try {
            String newID = UUID.randomUUID().toString();
            String newDate = LocalDateTime.now().toString();

            ContentValues values = new ContentValues();
            values.put(DBManager.C_ID2, newID);
            values.put(DBManager.C_ASSOCIATED_LIST, MainActivity.selectedListName);
            values.put(DBManager.C_DESCRIPTION, newItemName);
            values.put(DBManager.C_FLAG, "0");
            values.put(DBManager.C_CREATED_DATE, newDate);


            database = dbManager.getWritableDatabase();
            database.insert(dbManager.DB_TABLE2, null, values);
            database.close();
        }

        catch (Exception e){
            Toast.makeText(this, "Error occured:" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void archive(View view){

        if(MainActivity.password == null || MainActivity.username == null)
        {
            Toast.makeText(this, "You must enter password and username", Toast.LENGTH_LONG).show();
            return;
        }

        ViewParent p = view.getParent().getParent();

        LinearLayout layout = (LinearLayout) p;

        TextView listName = layout.findViewById(R.id.custom_row_list_title);
        TextView content = layout.findViewById(R.id.custom_row_content);
        TextView date = layout.findViewById(R.id.custom_row_post_date);
        TextView UUID = layout.findViewById(R.id.custom_row_list_item_UUID);

        String associatedList = listName.getText().toString();
        String description = content.getText().toString();
        String createdDate = date.getText().toString();
        String id = UUID.getText().toString();

        String [] queryStringValuesAndId = new String[4];

        queryStringValuesAndId[0] = associatedList;
        queryStringValuesAndId[1] = description;
        queryStringValuesAndId[2] = createdDate;
        queryStringValuesAndId[3] = id;

        Log.d(TAG, "Invoking AsyncTask..");

        AsyncTask task = new archiveItem().execute(queryStringValuesAndId);

        if(task.getStatus() == AsyncTask.Status.FINISHED)
        {
            task.cancel(true);
        }
    }

    public void showSoftKeyboard(View view) {
        if(view.requestFocus()){
            InputMethodManager imm =(InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
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
