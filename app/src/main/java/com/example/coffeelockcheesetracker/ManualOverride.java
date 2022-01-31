package com.example.coffeelockcheesetracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ManualOverride extends AppCompatActivity {
    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_override);

        Toolbar manualOverrideAppBar = (Toolbar) findViewById(R.id.manualOverrideAppBar);
        setSupportActionBar(manualOverrideAppBar);
        ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        data = new String[15];
    }//end onCreate

    public void overrideValuesButton(View view){
        data[0] = ((EditText) findViewById(R.id.moL1Temp)).getText().toString();
        data[1] = ((EditText) findViewById(R.id.moL2Temp)).getText().toString();
        data[2] = ((EditText) findViewById(R.id.moL3Temp)).getText().toString();
        data[3] = ((EditText) findViewById(R.id.moL4Temp)).getText().toString();
        data[4] = ((EditText) findViewById(R.id.moL5Temp)).getText().toString();
        data[5] = ((EditText) findViewById(R.id.moL1Reg)).getText().toString();
        data[6] = ((EditText) findViewById(R.id.moL2Reg)).getText().toString();
        data[7] = ((EditText) findViewById(R.id.moL3Reg)).getText().toString();
        data[8] = ((EditText) findViewById(R.id.moL4Reg)).getText().toString();
        data[9] = ((EditText) findViewById(R.id.moL5Reg)).getText().toString();
        data[10] = ((EditText) findViewById(R.id.moW)).getText().toString();
        data[11] = ((EditText) findViewById(R.id.moWarlockLevel)).getText().toString();
        data[12] = ((EditText) findViewById(R.id.moSorcererLevel)).getText().toString();
        data[13] = ((EditText) findViewById(R.id.moWarlockSlotLevel)).getText().toString();
        data[14] = ((EditText) findViewById(R.id.moSorceryPoints)).getText().toString();

        int[] returnData = {-1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1 , -1};
        //String test = ((EditText) findViewById(R.id.moL2Temp)).getText().toString();
        for (int index = 0; index < data.length; index++){
            if(data[index].length() > 0){
                returnData[index] = Integer.parseInt(data[index]);
            }//end if data is entered put in datareturn
        }//end for traverse the edit text array

        Intent intent = new Intent();
        intent.putExtra("returnData" , returnData);
        setResult(RESULT_OK , intent);
        finish();
    }//end OVbutton
}//end class