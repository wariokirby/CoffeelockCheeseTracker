package com.example.coffeelockcheesetracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlotGeneratorDialog  extends DialogFragment {
    private SlotEngine engine;
    private Spinner restChoices;
    private int sorceryPointRestPool;
    private TextView[] slotCounters;
    private TextView spPool;
    private int[] slotsToCreate;


    public SlotGeneratorDialog(SlotEngine engine){
        this.engine = engine;
        slotCounters = new TextView[5];
        slotsToCreate = new int[5];
    }//end constructor

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_generate_spellslots, null);
        slotCounters[0] = view.findViewById(R.id.numL1);
        slotCounters[1] = view.findViewById(R.id.numL2);
        slotCounters[2] = view.findViewById(R.id.numL3);
        slotCounters[3] = view.findViewById(R.id.numL4);
        slotCounters[4] = view.findViewById(R.id.numL5);
        spPool = view.findViewById(R.id.spPool);

        restChoices = (Spinner) view.findViewById(R.id.generateSlots);
        // Initializing a String Array
        String[] slotGenChoices = new String[]{"Choose Rest Type..." , "Short Rest" , "Long rest" , "Down Time" , "Max Cheese"};
        List<String> slotGenChoicesList = new ArrayList<>(Arrays.asList(slotGenChoices));
        ArrayAdapter<String> slotGenArrayAdapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_spinner_item , slotGenChoicesList){
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
                int sorcererLevel = engine.getSorcererLevel();
                if (i > 0 && i < 4){
                    engine.useLeftoverSP();
                    //sorceryPointRestPool = engine.getSorceryPointRestPool(i);
                    if(sorcererLevel == 2){
                        slotsToCreate[0] = sorceryPointRestPool / 2;
                        slotCounters[0].setText(Integer.toString(slotsToCreate[0]));
                    }//end level 2
                    else{
                        spPool.setText(Integer.toString(sorceryPointRestPool));
                    }//set up slot selector
                }//end normal operation no cheese
                else if (i == 4){
                    engine.useLeftoverSP();
                   // sorceryPointRestPool = engine.getSorceryPointRestPoolMaxCheese(7);
                }//max cheese currently hard coded to 1 week down time 1 long rest per day

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view)
                .setMessage("Choose what kind of rest you are taking and, if more than one option, what spell slots to create. Excess Sorcery Points will automatically be converted to spell slots where possible.")
                .setTitle("Coffeelock Spell Slot Generator")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        engine.makeTempSlots(slotsToCreate);
                        ((MainActivity)getActivity()).updateSlots();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }//end oncreate

    public void spellChooser(View view){
        //int sorcererLevel = engine.getSorcererLevel();
        int tag = Integer.parseInt((String)view.getTag());
        if(tag >= 10){
            tag -= 10;
            if(slotsToCreate[tag] > 0){
                slotsToCreate[tag]--;
                if(tag <= 1){//levels 1 and 2
                    sorceryPointRestPool += tag + 2;
                }//end if
                else{
                    sorceryPointRestPool += tag + 3;
                }//end else
            }//decrement if not 0

        }//end if decrement
        else{
            if((tag <= 1 && sorceryPointRestPool > tag + 2) || (sorceryPointRestPool > tag + 3)){
                slotsToCreate[tag]++;
                if(tag <= 1){//levels 1 and 2
                    sorceryPointRestPool -= tag + 2;
                }//end if
                else{
                    sorceryPointRestPool -= tag + 3;
                }//end else
            }//increment if not 0

        }//end else increment
        for(int index = 0; index < slotCounters.length; index++){
            slotCounters[index].setText(Integer.toString(slotsToCreate[index]));
            spPool.setText((Integer.toString(sorceryPointRestPool)));
        }//end update display

    }//end spellchooser


}//end SGD class
