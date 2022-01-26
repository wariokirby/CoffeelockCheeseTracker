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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddModifierDialog extends DialogFragment {
    private int dieToRoll;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_modifier, null);
        EditText modifier = (EditText)view.findViewById(R.id.addModModifier);

        Spinner dieChoice = (Spinner) view.findViewById(R.id.addModDieSpinner); //moved to top to allow for visbility operations
        String[] dieStrings = new String[]{"Choose die" , "d4" , "d6" , "d8" , "d10"};
        List<String> dieList = new ArrayList<>(Arrays.asList(dieStrings));
        ArrayAdapter<String> dieChoiceArrayAdapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_spinner_item , dieList){
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
        dieChoiceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dieChoice.setAdapter(dieChoiceArrayAdapter);
        dieChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    switch (i){
                        case 1: dieToRoll = 4;
                                break;
                        case 2: dieToRoll = 6;
                            break;
                        case 3: dieToRoll = 8;
                            break;
                        case 4: dieToRoll = 10;
                            break;
                        default: dieToRoll = 0;
                    }//end die switch statement
                }//end if something other than hint
            }//end onitemSelected

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        builder.setView(view)
                .setMessage(getResources().getString(R.string.add_modifier_dialog_text))
                .setTitle("Add Modifier")
                .setPositiveButton("Add Modifier", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String modText = modifier.getText().toString();
                        if(modText.length() == 0){
                            modText = "0";
                            Toast.makeText(((HackBookOfShadows)getActivity()), "No modifier entered, nothing was added", Toast.LENGTH_SHORT).show();
                        }
                        ((HackBookOfShadows)getActivity()).setModifierFromDialog(modText);
                    }
                })
                .setNeutralButton("Roll for Me using selected die", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(dieToRoll > 0){
                            int randMod = (int)(Math.random() * dieToRoll)+1;
                            ((HackBookOfShadows)getActivity()).setModifierFromDialog(Integer.toString(randMod));
                        }//end if die selected
                    }//close dialog if no die selected
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }//end oncreate


}//end fragment
