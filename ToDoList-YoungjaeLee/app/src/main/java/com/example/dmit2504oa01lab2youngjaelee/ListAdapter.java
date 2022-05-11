package com.example.dmit2504oa01lab2youngjaelee;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class ListAdapter extends SimpleCursorAdapter {

        static final String[] columns = {DBManager.C_ID2, DBManager.C_NAME};
        static final int[] rowIds = {R.id.custom_row_list_UUID, R.id.custom_row_list_name};
        static int row = 0;

        public ListAdapter(Context context, Cursor cursor)
        {
            super(context, R.layout.custom_row_main, cursor, columns, rowIds);
        }

        //perhaps for editing date
        //        @Override
        //        public void bindView(View currentRow, Context context, Cursor cursor)
        //        {
        //            super.bindView(currentRow, context, cursor);
        //            String strDate = cursor.getString(cursor.getColumnIndex(DBManager.C_DATE));
        //            String strShortDate = strDate.substring(7,17);
        //            TextView tvDate = currentRow.findViewById(R.id.custom_row_date);
        //            tvDate.setText(strShortDate);
        //        }
}
