package com.example.dmit2504oa01lab2youngjaelee;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, FontDialogFragment.NoticeDialogListener, SetThemeBottomSheetDialog.NoticeDialogListener, SetUsernameDialogFragment.NoticeDialogListener, SetPasswordDialogFragment.NoticeDialogListener  {

    ListAdapter adapter;
    LinearLayout addbar;
    ListView listView;
    Cursor cursor;
    SQLiteDatabase database;
    DBManager dbManager;

    public static TextView header;
    public static ImageButton menuButton;

    public static ImageView listIcon;

    public static String username = "Ylee1234";
    public static String password = "123";
    public static String selectedListName;
    public static String selectedListUUID;

    public static Integer selectedThemeColor;
    public static boolean fontSizeIncrease;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addbar = findViewById(R.id.add_bar);
        listView = findViewById(R.id.current_list);
        listView.setOnItemClickListener(this);
        dbManager = new DBManager(this);
        database = dbManager.getReadableDatabase();
        header = findViewById(R.id.title_main);
        menuButton = findViewById(R.id.menu_button);
        listIcon = listView.findViewById(R.id.list_icon1);

        if(MainActivity.fontSizeIncrease){
            header.setTextSize(50);
        }

        if(MainActivity.selectedThemeColor != null){
            header.setTextColor(selectedThemeColor);
            menuButton.setColorFilter(selectedThemeColor);
            if(listIcon != null){
                listIcon.setColorFilter(selectedThemeColor);
            }
        }

        addbar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ViewListItemsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("newList", "Untitled list");
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);
            }
        });
    }


    //apply where clause for UUID
    @Override
    protected void onResume()
    {
        cursor = database.query(DBManager.DB_TABLE,null,null,null,null,null,DBManager.C_NAME + " DESC");
        startManagingCursor(cursor);
        adapter = new ListAdapter(this, cursor);
        listView.setAdapter(adapter);
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View row, int position, long id)
    {

        TextView listName = row.findViewById(R.id.custom_row_list_name);
        selectedListName = listName.getText().toString();
        TextView UUID = row.findViewById(R.id.custom_row_list_UUID);
        selectedListUUID = UUID.getText().toString();

        Intent intent = new Intent(this, ViewListItemsActivity.class);
        startActivity(intent);
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
        header.setTextSize(50);
        MainActivity.fontSizeIncrease = true;
    }

    public void changeColor(Integer colorHex){
        header.setTextColor(colorHex);
        menuButton.setColorFilter(colorHex);
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
            password = input;
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
            username = input;
        }
    }
}