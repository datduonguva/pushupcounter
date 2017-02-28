package com.duongCompany.duong.pushupcounter;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.duongCompany.duong.pushupcounter.data.PushContract;

/**
 * Created by duong on 2/24/2017.
 */

public class ParameterUpdatedActivity extends AppCompatActivity {
    private boolean isSI = true;
    private Button siButton;
    private Button imButton;
    private Button saveButton;

    private View siMassView;
    private View imMAssView;
    private View siHeightView;
    private View imHeightView;

    private int height = 0;
    private float weight;
    private int goal = 0;

    private EditText siMassEdit;
    private EditText siHeightEdit;
    private EditText imMassEdit;
    private EditText imHeightEditFt;
    private EditText imHeightEditIn;
    private EditText goalEdit;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameter_activity);
        siButton = (Button) findViewById(R.id.SI_button);
        imButton = (Button) findViewById(R.id.IM_button);
        saveButton = (Button) findViewById(R.id.save_button);

        siMassView = (View) findViewById(R.id.weight_view_SI);
        imMAssView = (View) findViewById(R.id.weight_view_IM);
        siHeightView = (View) findViewById(R.id.height_view_SI);
        imHeightView = (View) findViewById(R.id.height_view_IM);
        siMassEdit = (EditText) findViewById(R.id.mass_edit_text_SI);
        siHeightEdit = (EditText) findViewById(R.id.height_edit_cm);
        imMassEdit = (EditText) findViewById(R.id.mass_edit_text_IM);
        imHeightEditFt = (EditText) findViewById(R.id.height_edit_IM_ft);
        imHeightEditIn = (EditText) findViewById(R.id.height_edit_IM_in);
        goalEdit = (EditText) findViewById(R.id.daily_goal_edit);

        final Context context = getApplicationContext();
        sharedPreferences = context.getSharedPreferences(getString(R.string.sharedPre), MODE_PRIVATE);

        height = sharedPreferences.getInt(getString(R.string.height), 170);
        weight = sharedPreferences.getFloat(getString(R.string.weight), 60.0F);
        goal = sharedPreferences.getInt(getString(R.string.goal), 50);
        isSI = sharedPreferences.getBoolean(getString(R.string.isSI), true);

        if(isSI){
            siHeightEdit.setText(String.valueOf(height));
            siMassEdit.setText(String.valueOf(weight));
            goalEdit.setText(String.valueOf(goal));
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
                if(isSI){
                    if(TextUtils.isEmpty(siHeightEdit.getText().toString())||
                            TextUtils.isEmpty(siMassEdit.getText().toString())||
                            TextUtils.isEmpty(goalEdit.getText().toString())){
                        Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                    }else{
                        changeParameter(Integer.parseInt(siHeightEdit.getText().toString()),
                                Float.parseFloat(siMassEdit.getText().toString()),
                                Integer.parseInt(goalEdit.getText().toString()));
                    }
                }else{
                    if(TextUtils.isEmpty(imHeightEditFt.getText().toString())||
                            TextUtils.isEmpty(imMassEdit.getText().toString())||
                            TextUtils.isEmpty(goalEdit.getText().toString()) ||
                            TextUtils.isEmpty(imHeightEditIn.getText().toString())){
                        Toast.makeText(context, "Invalid Input", Toast.LENGTH_SHORT).show();
                    }else {
                        height = Integer.parseInt(imHeightEditFt.getText().toString()) * 12;
                        height += Integer.parseInt(imHeightEditIn.getText().toString());
                        float temp = (float) height * 2.54f;
                        height = (int) temp;

                        weight = Float.parseFloat(imMassEdit.getText().toString());
                        weight = weight * 0.454f;
                        goal = Integer.parseInt(goalEdit.getText().toString());
                        changeParameter(height, weight, goal);
                    }
                }
            }
        });

    }


    private void changeParameter(int height, float weight, int goal) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getString(R.string.height), height);
        editor.putFloat(getString(R.string.weight), weight);
        editor.putInt(getString(R.string.goal), goal);
        editor.commit();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
}
