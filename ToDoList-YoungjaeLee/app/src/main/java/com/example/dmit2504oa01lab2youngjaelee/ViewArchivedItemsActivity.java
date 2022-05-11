package com.example.dmit2504oa01lab2youngjaelee;

import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;


public class ViewArchivedItemsActivity extends BaseActivity implements FontDialogFragment.NoticeDialogListener, SetThemeBottomSheetDialog.NoticeDialogListener, SetUsernameDialogFragment.NoticeDialogListener, SetPasswordDialogFragment.NoticeDialogListener {
    ArrayList<HashMap<String,String>> archivedItems =
            new ArrayList<HashMap<String, String>>();

    ListView listView;
    static final String TAG = "ArchivedItemsActivity";

    TextView listName;
    String listNameText;

    ImageButton backButton;
    ImageButton menuButton;

    private Thread myThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archived_items);
        listView = findViewById(R.id.list_items);
        listName = findViewById(R.id.title);
        backButton = findViewById(R.id.go_back_button);
        menuButton = findViewById(R.id.menu_button);

        if(MainActivity.fontSizeIncrease){
            listName.setTextSize(50);
        }

        if(MainActivity.selectedThemeColor != null)
        {
            listName.setTextColor(MainActivity.selectedThemeColor);
            backButton.setColorFilter(MainActivity.selectedThemeColor);
            menuButton.setColorFilter(MainActivity.selectedThemeColor);
        }

        if(MainActivity.selectedListName != null)
        {
            listNameText = MainActivity.selectedListName;
            listName.setText(listNameText);
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ViewArchivedItemsActivity.this.finish();
            }
        });

        loadListView();
    }

    private void loadListView()
    {
        String[] fields = new String[]{"TITLE", "CREATED_DATE", "FLAG", "DESCRIPTION"};
        int[] ids = new int[]{R.id.custom_row_list_title,
                R.id.custom_row_post_date,
                R.id.custom_row_status,
                R.id.custom_row_content};

//        boolean finished = runGetAsyncTask();


            boolean finished = startBackgroundThreadForGetRequest();

            if(finished)
            {
                SimpleAdapter adapter = new SimpleAdapter(this, archivedItems, R.layout.custom_row_list_items_archive, fields, ids);
                listView.setAdapter(adapter);
            }


    }

//
//    private class archivedItem extends AsyncTask<String, Void, Void>
//    {
//        @Override
//        protected Void doInBackground(String... strings)
//        {
//            BufferedReader in = null;
//
//            try
//            {
//                HttpClient client = new DefaultHttpClient();
//                HttpGet request = new HttpGet();
//                request.setURI(new URI("http://www.youcode.ca/Lab02Get.jsp?ALIAS=" + MainActivity.username + "&PASSWORD=" + MainActivity.password));
//                HttpResponse response = client.execute(request);
//                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//                String line = "";
//                while((line = in.readLine()) != null)
//                {
//                    HashMap<String,String> temp = new HashMap<String,String>();
//
//                    temp.put("CREATED_DATE",line);
//
//                    line = in.readLine();
//                    temp.put("TITLE", line);
//
//                    line = in.readLine();
//                    temp.put("DESCRIPTION", line);
//
//                    line = in.readLine();
//                    temp.put("FLAG", line);
//
//                    archivedItems.add(temp);
//                }
//                in.close();
//            }
//            catch(Exception e)
//            {
//                Toast.makeText(ViewArchivedItemsActivity.this, "Error: " + e, Toast.LENGTH_LONG).show();
//            }
//
//            return null;
//        }
//
//    }

        private boolean startBackgroundThreadForGetRequest()
        {
            myThread = new Thread(new Runnable() {
                public void run() {
                    BufferedReader in = null;

                    try
                    {
                        HttpClient client = new DefaultHttpClient();
                        HttpGet request = new HttpGet();
                        request.setURI(new URI("http://www.youcode.ca/Lab02Get.jsp?ALIAS=" + MainActivity.username + "&PASSWORD=" + MainActivity.password));
                        HttpResponse response = client.execute(request);
                        in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                        String line = "";
                        while((line = in.readLine()) != null)
                        {
                            HashMap<String,String> temp = new HashMap<String,String>();

                            temp.put("CREATED_DATE",line);

                            line = in.readLine();
                            if(line.trim().equals(MainActivity.selectedListName))
                            {
                                temp.put("TITLE", line);
                            }
                            else {
                                in.readLine();
                                continue;
                            }

                            line = in.readLine();
                            temp.put("DESCRIPTION", line);

                            line = in.readLine();
                            temp.put("FLAG", line);

                            archivedItems.add(temp);
                        }
                        in.close();
                    }
                    catch(Exception e)
                    {
                        Looper.prepare();
                        Toast.makeText(ViewArchivedItemsActivity.this, "Error: " + e, Toast.LENGTH_LONG).show();
                    }
                }
            });

            myThread.start();

            while(myThread.getState() != Thread.State.TERMINATED){
                System.out.println("processing");
            }

            return true;
        }

//    private boolean runGetAsyncTask()
//    {
//        AsyncTask task = new archivedItem().execute();
//
//        if(task.getStatus() == AsyncTask.Status.FINISHED)
//        {
//            task.cancel(true);
//            return true;
//        }
//        return true;
//    }

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
        backButton.setColorFilter(colorHex);
        menuButton.setColorFilter(colorHex);
        MainActivity.header.setTextColor(colorHex);
        MainActivity.menuButton.setColorFilter(colorHex);

        ViewListItemsActivity.listName.setTextColor(colorHex);
        ViewListItemsActivity.backButton.setColorFilter(colorHex);
        ViewListItemsActivity.optionButton.setColorFilter(colorHex);
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
        ViewListItemsActivity.addButton.setColorFilter(colorHex);
//        MainActivity.listIcon.setColorFilter(colorHex);
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
