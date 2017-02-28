package com.duongCompany.duong.pushupcounter;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        final Context mContext = context;
        TextView dateText = (TextView) view.findViewById(R.id.date_text);
        TextView countText = (TextView) view.findViewById(R.id.count_text);
        TextView caloriesText = (TextView) view.findViewById(R.id.calories_text);
        ImageView imageView = (ImageView) view.findViewById(R.id.delete_icon);
        imageView.setTag((int) cursor.getInt(cursor.getColumnIndex(PushContract.PushEntry.COLUMN_ID)));
        //Toast.makeText(context, "Id of the item created"+ imageView.getTag(), Toast.LENGTH_SHORT).show();
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
                final View V = v;
                if(!MainActivity.insue){
                    final int id = (int) v.getTag();
                    final Uri uri = ContentUris.withAppendedId(PushContract.PushEntry.CONTENT_URI,id);
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
                    alertBuilder.setMessage("Do you to delete this item?");
                    alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mContext.getContentResolver().delete(uri,null, null);
                        }
                    });

                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }
            }
        });

    }
}
