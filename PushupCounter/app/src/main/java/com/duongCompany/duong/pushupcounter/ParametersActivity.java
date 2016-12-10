package com.duongCompany.duong.pushupcounter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duongCompany.duong.pushupcounter.data.PushContract;

/**
 * Created by duong on 12/5/2016.
 */

public class ParametersActivity extends AppCompatActivity {
    private boolean isSI = true;
    private Button siButton;
    private Button imButton;
    private Button saveButton;
    private View siMassView;
    private View imMAssView;
    private View siHeightView;
    private View imHeightView;
    private int height = 0;
    private double weight;
    private EditText siMassEdit;
    private EditText siHeightEdit;
    private EditText imMassEdit;
    private EditText imHeightEditFt;
    private EditText imHeightEditIn;
    private int id = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameter_activity);
        siButton = (Button) findViewById(R.id.SI_button);
        imButton = (Button) findViewById(R.id.IM_button);
        siMassView = (View) findViewById(R.id.weight_view_SI);
        imMAssView = (View) findViewById(R.id.weight_view_IM);
        siHeightView = (View) findViewById(R.id.height_view_SI);
        imHeightView = (View) findViewById(R.id.height_view_IM);
        siMassEdit = (EditText) findViewById(R.id.mass_edit_text_SI);
        siHeightEdit = (EditText) findViewById(R.id.height_edit_cm);

        imMassEdit = (EditText) findViewById(R.id.mass_edit_text_IM);
        imHeightEditFt = (EditText) findViewById(R.id.height_edit_IM_ft);
        imHeightEditIn = (EditText) findViewById(R.id.height_edit_IM_in);

        saveButton = (Button) findViewById(R.id.save_button);


        Cursor cursor = getContentResolver().query(PushContract.Parameter.CONTENT_URI, null, null, null, null);
        //Toast.makeText(this, "#" + cursor.getCount(), Toast.LENGTH_SHORT).show();
        cursor.moveToPosition(0);
        id = cursor.getInt(cursor.getColumnIndex(PushContract.Parameter.COLUMN_ID));

        height = cursor.getInt(cursor.getColumnIndex(PushContract.Parameter.COLUMN_HEIGHT));
        weight = cursor.getDouble(cursor.getColumnIndex(PushContract.Parameter.COLUMN_WEIGHT));
        if (isSI) {
            siHeightEdit.setText(String.valueOf(height));
            siMassEdit.setText(String.valueOf(weight));
        }
        siButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imMAssView.setVisibility(View.GONE);
                imHeightView.setVisibility(View.GONE);
                siButton.setEnabled(false);
                imButton.setEnabled(true);
                siMassView.setVisibility(View.VISIBLE);
                siHeightView.setVisibility(View.VISIBLE);
                isSI = true;


            }
        });

        imButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imButton.setEnabled(false);
                siButton.setEnabled(true);
                siMassView.setVisibility(View.GONE);
                siHeightView.setVisibility(View.GONE);
                imMAssView.setVisibility(View.VISIBLE);
                imHeightView.setVisibility(View.VISIBLE);
                isSI = false;

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean erOrcur = false;
                if (isSI) {
                    if (!TextUtils.isEmpty(siMassEdit.getText().toString()))
                        weight = Double.parseDouble(siMassEdit.getText().toString());
                    else erOrcur = true;

                    if (!TextUtils.isEmpty(siHeightEdit.getText().toString()))
                        height = Integer.parseInt(siHeightEdit.getText().toString());
                    else erOrcur = true;
                    if (erOrcur) {
                        Toast.makeText(getApplicationContext(), "Invalid inputs", Toast.LENGTH_SHORT).show();
                    } else changeParameter(height, weight);
                } else {
                    if (!TextUtils.isEmpty(imMassEdit.getText().toString()) ||
                            !TextUtils.isEmpty(imHeightEditFt.getText().toString()) ||
                            !TextUtils.isEmpty(imHeightEditIn.getText().toString())) {
                        weight = 0.454 * Double.parseDouble(imMassEdit.getText().toString());
                        height = (int) (12 * Integer.parseInt(imHeightEditFt.getText().toString()) + Integer.parseInt(imHeightEditIn.getText()
                                .toString())) * 245 / 100;
                        changeParameter(height, weight);
                    } else Toast.makeText(getApplicationContext(),"Invalid inpud", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void changeParameter(int height, double weight) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PushContract.Parameter.COLUMN_HEIGHT, height);
        contentValues.put(PushContract.Parameter.COLUMN_WEIGHT, weight);
        int result = getContentResolver().update(ContentUris.withAppendedId(PushContract.Parameter.CONTENT_URI, id),contentValues, null, null);
        if(result!= 0) Toast.makeText(this, "Done changing", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
