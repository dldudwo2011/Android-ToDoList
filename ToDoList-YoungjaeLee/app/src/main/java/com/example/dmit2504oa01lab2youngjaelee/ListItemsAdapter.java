package com.example.dmit2504oa01lab2youngjaelee;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class ListItemsAdapter extends SimpleCursorAdapter {

    static final String[] columns = {DBManager.C_ID2,DBManager.C_ASSOCIATED_LIST, DBManager.C_DESCRIPTION, DBManager.C_FLAG, DBManager.C_CREATED_DATE};
    static final int[] rowIds = {R.id.custom_row_list_item_UUID, R.id.custom_row_list_title, R.id.custom_row_content, R.id.custom_row_status, R.id.custom_row_post_date};
    static int row = 0;

    public ListItemsAdapter(Context context, Cursor cursor)
    {
        super(context, R.layout.custom_row_list_items, cursor, columns, rowIds);
    }

    //perhaps for editing date
    //    @Override
    //    public void bindView(View currentRow, Context context, Cursor cursor)
    //    {
    //        super.bindView(currentRow, context, cursor);
    //        String strDate = cursor.getString(cursor.getColumnIndex(DBManager.C_DATE));
    //        String strShortDate = strDate.substring(7,17);
    //        TextView tvDate = currentRow.findViewById(R.id.custom_row_date);
    //        tvDate.setText(strShortDate);
    //    }
}
