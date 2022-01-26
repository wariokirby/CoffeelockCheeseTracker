package com.example.coffeelockcheesetracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlotGeneratorActivity extends AppCompatActivity {

    //private SlotEngine engine;
    private Spinner restChoices;
    private int sorceryPointRestPool[];
    private int maxSorceryPointRestPool[];
    private int sorcererLevel;
    private TextView[] slotCounters;
    private TextView spPool;
    private int[] slotsToCreate;
    private int choice;
    private int[][] slotGenLimitList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slot_generator);

        Toolbar slotGeneratorAppBar = (Toolbar) findViewById(R.id.slotGeneratorAppBar);
        setSupportActionBar(slotGeneratorAppBar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        sorceryPointRestPool = intent.getIntArrayExtra("sorceryPointRestPool");
        maxSorceryPointRestPool = sorceryPointRestPool.clone();
        sorcererLevel = intent.getIntExtra("sorcererLevel" , 2);
        slotCounters = new TextView[5];
        slotsToCreate = new int[5];

        slotCounters[0] = findViewById(R.id.numL1);
        slotCounters[1] = findViewById(R.id.numL2);
        slotCounters[2] = findViewById(R.id.numL3);
        slotCounters[3] = findViewById(R.id.numL4);
        slotCounters[4] = findViewById(R.id.numL5);
        spPool = findViewById(R.id.spPool);
        choice = -1;

        slotGenLimitList = new int[5][8];
        slotGenLimitList[0][0] = 14;//L2 S1
        slotGenLimitList[0][1] = 14;//L3 S1
        slotGenLimitList[1][1] = 14;//L3 S2
        slotGenLimitList[0][2] = 28;//L4 S1
        slotGenLimitList[1][2] = 14;//L4 S2
        slotGenLimitList[0][3] = 28;//L5 S1
        slotGenLimitList[1][3] = 14;//L5 S2
        slotGenLimitList[2][3] = 14;//L5 S3
        slotGenLimitList[0][4] = 28;//L6 S1
        slotGenLimitList[1][4] = 14;//L6 S2
        slotGenLimitList[2][4] = 14;//L6 S3
        slotGenLimitList[3][4] = 10;//L6 S4



        restChoices = (Spinner) findViewById(R.id.generateSlots);
        // Initializing a String Array
        String[] slotGenChoices = new String[]{"Choose Rest Type..." , "Short Rest" , "Long rest" , "Down Time" , "Max Cheese"};
        List<String> slotGenChoicesList = new ArrayList<>(Arrays.asList(slotGenChoices));
        ArrayAdapter<String> slotGenArrayAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , slotGenChoicesList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0){
                    return false;
                }
                else{
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        slotGenArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restChoices.setAdapter(slotGenArrayAdapter);
        restChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //int sorcererLevel = engine.getSorcererLevel();
                if (i > 0 && i < 4){
                    reset();
                    //engine.useLeftoverSP();
                    //sorceryPointRestPool = engine.getSorceryPointRestPool(i);
                    if(sorcererLevel == 2){
                        slotsToCreate[0] = sorceryPointRestPool[i - 1] / 2;
                        slotCounters[0].setText(Integer.toString(slotsToCreate[0]));
                        //spPool.setText(Integer.toString(sorceryPointRestPool[i - 1]));
                    }//end level 2

                    else{
                        spPool.setText(Integer.toString(sorceryPointRestPool[i-1]));
                        choice = i - 1;
                    }//set up slot selector
                }//end normal operation no cheese
                else if (i == 4){
                    //engine.useLeftoverSP();
                    //sorceryPointRestPool = engine.getSorceryPointRestPoolMaxCheese(7);
                }//max cheese currently hard coded to 1 week down time 1 long rest per day

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }//end onCreate

    public void spellChooser(View view){
        //int sorcererLevel = engine.getSorcererLevel();
        if(sorcererLevel != 2 && choice >= 0) {
            int tag = Integer.parseInt((String) view.getTag()) - 1;
            if (tag >= 10) {
                tag -= 10;
                if(slotsToCreate[tag] > 0 && sorcererLevel >= 7 && (tag == 4 || (tag == 1 && slotsToCreate[1] == slotsToCreate[4]))){
                    slotsToCreate[4]--;
                    slotsToCreate[1]--;
                    sorceryPointRestPool[choice] += 10;
                }//end else Sorcerer L6 slot L4 special case decrement
                else if(sorcererLevel >= 6 && (slotsToCreate[tag] > 0 && (tag == 3|| (tag == 0 && slotsToCreate[0] == 2 * slotsToCreate[3])))){
                    slotsToCreate[3]--;
                    slotsToCreate[0] -= 2;
                    sorceryPointRestPool[choice] += 10;
                }//end else Sorcerer L6 slot L4 special case decrement
                else if(slotsToCreate[tag] > 0 && sorcererLevel >= 5 && ((tag == 0 && slotsToCreate[0] == slotsToCreate[1]) || tag == 1)){
                    slotsToCreate[0]--;
                    slotsToCreate[1]--;
                    sorceryPointRestPool[choice] += 5;
                }//end if Sorcerer Level 5 decrement special cases
                else if(slotsToCreate[tag] > 0 && (sorcererLevel >= 4) && tag == 0){
                    slotsToCreate[tag] -= 2;
                    sorceryPointRestPool[choice] += 4;
                }//end else if L6 and L5 and L4 individual 1 slot double decrement
                else if (slotsToCreate[tag] > 0) {
                    slotsToCreate[tag]--;
                    if (tag <= 1) {//levels 1 and 2
                        sorceryPointRestPool[choice] += tag + 2;
                    }//end if
                    else {
                        sorceryPointRestPool[choice] += tag + 3;
                    }//end else
                }//decrement if not 0

            }//end if decrement
            else if (sorcererLevel == 3) {
                if (tag <= 1 && sorceryPointRestPool[choice] >= tag + 2 && slotsToCreate[0] + slotsToCreate[1] < maxSorceryPointRestPool[choice] / 3) {
                    //locks out levels > 3 and makes sure SP is left and adds safeties from overloading slots
                    slotsToCreate[tag]++;
                    if (tag <= 1) {//levels 1 and 2
                        sorceryPointRestPool[choice] -= tag + 2;
                    }//end if
                    else {
                        sorceryPointRestPool[choice] -= tag + 3;
                    }//end else
                }//increment if passes safeties

            }//end else if L3 increment case
            else if (sorcererLevel == 4) {
                if (tag <= 1 && sorceryPointRestPool[choice] >= tag + 2 && (slotsToCreate[0] / 2) + slotsToCreate[1] < maxSorceryPointRestPool[choice] / 4) {
                    //locks out levels > 3 and makes sure SP is left and adds safeties from overloading slots
                    if(tag == 0){
                        slotsToCreate[tag]++;
                        sorceryPointRestPool[choice] -= tag + 2;
                    }//end extra inc to double L1 slots
                    slotsToCreate[tag]++;
                    if (tag <= 1) {//levels 1 and 2
                        sorceryPointRestPool[choice] -= tag + 2;
                    }//end if
                    else {
                        sorceryPointRestPool[choice] -= tag + 3;
                    }//end else
                }//increment if not 0

            }//end else if L4 increment case

            else if (sorcererLevel == 5 || (sorcererLevel >= 6 && tag <= 2)) {
               if (tag <= 2 && sorceryPointRestPool[choice] >= tag + 3 && slotsToCreate[0] < (2 * maxSorceryPointRestPool[choice] / 5) - slotsToCreate[1] - (2 * slotsToCreate[2])) {
                    //locks out levels > 4 and makes sure SP is left and adds safeties from overloading slots
                    if(tag == 0){
                        slotsToCreate[tag]++;
                        sorceryPointRestPool[choice] -= tag + 2;
                    }//end extra inc to double L1 slots
                    else if(tag == 1){
                        slotsToCreate[0]++;
                        sorceryPointRestPool[choice] -= 2;
                    }//end extra L1 when getting L2
                    slotsToCreate[tag]++;
                    if (tag <= 1) {//levels 1 and 2
                        sorceryPointRestPool[choice] -= tag + 2;
                    }//end if
                    else {
                        sorceryPointRestPool[choice] -= tag + 3;
                    }//end else
                }//increment if not 0

            }//end else if L5 and L6 except L4 slot increment case

            else if (sorcererLevel >= 6 && tag == 3 && sorceryPointRestPool[choice] >= 10) {
                //allows level 4 generation rules special case
                slotsToCreate[3]++;
                slotsToCreate[0] += 2;
                sorceryPointRestPool[choice] -= 10;
            }//end else if L6 and up slot L4 increment special case

            else if (sorcererLevel >= 7 && tag == 4 && sorceryPointRestPool[choice] >= 10) {
                //allows level 4 generation rules special case
                slotsToCreate[4]++;
                slotsToCreate[1]++;
                sorceryPointRestPool[choice] -= 10;
            }//end else if L6 and up slot L4 increment special case

            for (int index = 0; index < slotCounters.length; index++) {
                slotCounters[index].setText(Integer.toString(slotsToCreate[index]));
                spPool.setText((Integer.toString(sorceryPointRestPool[choice])));
            }//end update display
        }//end if not automated level
    }//end spellchooser


    private void reset(){
        for(int index = 0; index < slotCounters.length; index++){
            slotCounters[index].setText("0");
            slotsToCreate[index] = 0;
        }//end traverse array
        System.arraycopy(maxSorceryPointRestPool , 0 , sorceryPointRestPool , 0 , sorceryPointRestPool.length);
        spPool.setText("0");
    }//end reset counters

    public void confirmSlots(View view){
        Intent intent = new Intent();
        intent.putExtra("slotsToCreate" , slotsToCreate);
        setResult(RESULT_OK , intent);
        finish();
    }//end confirmSlots

    public void cancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }//end cancel button
}//end Activity