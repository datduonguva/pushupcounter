package com.duongCompany.duong.pushupcounter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.duongCompany.duong.pushupcounter.data.PushContract;


import java.text.SimpleDateFormat;
import java.util.Date;

import static com.duongCompany.duong.pushupcounter.data.PushContract.PushEntry.*;

/**
 * Created by duong on 12/2/2016.
 */

public class PushAdapter extends CursorAdapter {
    public PushAdapter(Context context, Cursor c){
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView dateText = (TextView) view.findViewById(R.id.date_text);
        TextView countText = (TextView) view.findViewById(R.id.count_text);
        TextView caloriesText = (TextView) view.findViewById(R.id.calories_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.delete_icon);
        imageView.setTag(cursor.getInt(cursor.getColumnIndex(PushContract.PushEntry.COLUMN_ID)));
        long time = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE));
        int count = cursor.getInt(cursor.getColumnIndex(COLUMN_COUNT));
        int calories = cursor.getInt(cursor.getColumnIndex(COLUMN_CALORIES))/4200;

        Date date = new Date(time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        String timeString = simpleDateFormat.format(date);
        dateText.setText(timeString);
        countText.setText(""+count);
        caloriesText.setText(""+calories);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag();
                Uri uri = null;
                if(!MainActivity.insue){
                    uri = ContentUris.withAppendedId(PushContract.PushEntry.CONTENT_URI,id);
                    v.getContext().getContentResolver().delete(uri,null, null);
                }

            }
        });

    }
}
