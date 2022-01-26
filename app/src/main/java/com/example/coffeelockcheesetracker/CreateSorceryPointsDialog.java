package com.example.coffeelockcheesetracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateSorceryPointsDialog extends DialogFragment {
    private SlotEngine engine;
    private List<String> sorceryPointOptions;
    private List<String> slotOptions;
    private Spinner dialogSPSpinner;
    private Spinner dialogSlotSpinner;
    private int numSPGen;
    private int spellSlotChoice;

    public CreateSorceryPointsDialog(SlotEngine engine){
        this.engine = engine;
        numSPGen = 0;
        spellSlotChoice = 0;

    }//end constructor
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sorceryPointOptions = new ArrayList<>();
        sorceryPointOptions.add("1 Sorcery Point");
        sorceryPointOptions.add("2 Sorcery Points");
        slotOptions = new ArrayList<>();
        choiceCreator();

            // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_sorcery_points, null);

        dialogSPSpinner= (Spinner) view.findViewById(R.id.dialogSpSpinner);
        ArrayAdapter<String> dialogSPList = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_spinner_item , sorceryPointOptions);
        dialogSPList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogSPSpinner.setAdapter(dialogSPList);
        dialogSPSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numSPGen = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });//end SP spinner

        dialogSlotSpinner= (Spinner) view.findViewById(R.id.dialogSlotSpinner);
        ArrayAdapter<String> dialogSlotList = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_spinner_item , slotOptions);
        dialogSPList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dialogSlotSpinner.setAdapter(dialogSlotList);
        dialogSlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spellSlotChoice = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });//end slot spinner



        builder.setView(view)
                .setMessage("Choose how many points and which slots to use.  If insufficient spell slots, you will be given as many sorcery points and you have slots selected.")
                .setTitle("Sorcery Point Generator")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        engine.spGenerator(numSPGen , spellSlotChoice);
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

    public void choiceCreator(){
        int[] slotchecker = engine.getSlots();
        for (int index = 3; index <= engine.getSorcererLevel(); index++){
            sorceryPointOptions.add(index + "Sorcery Points");
        }//end fill Sp spinner
        for(int index = 0; index < 5; index++){
            slotOptions.add("Level " + (index + 1) + " " + (slotchecker[0] + slotchecker[index + 5]) + "slots");
        }//end for
        slotOptions.add("Pact Level " + engine.getTypeSlotsWarlock() + " " + (slotchecker[10]) + "slots");

    }//end choiceCreator
}//end dialog
