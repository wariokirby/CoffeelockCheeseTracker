package com.example.coffeelockcheesetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private SlotEngine drake;

    private EditText warlockLevel;
    private EditText sorcererLevel;

    private TextView[] slotCounts;
    private TextView pactSlotLevel;
    private TextView sPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mainToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        //drake = new SlotEngine();
        MainViewModel model = new ViewModelProvider(this).get((MainViewModel.class));
        drake = model.loadEngine();

        warlockLevel = (EditText) findViewById(R.id.warlockLevel);
        warlockLevel.setHint(Integer.toString(drake.getWarlockLevel()));

        sorcererLevel = (EditText) findViewById(R.id.sorcererLevel);
        sorcererLevel.setHint(Integer.toString(drake.getSorcererLevel()));


        slotCounts = new TextView[11];
        slotCounts[0] = findViewById(R.id.countL1);
        slotCounts[1] = findViewById(R.id.countL2);
        slotCounts[2] = findViewById(R.id.countL3);
        slotCounts[3] = findViewById(R.id.countL4);
        slotCounts[4] = findViewById(R.id.countL5);
        slotCounts[5] = findViewById(R.id.s1);
        slotCounts[6] = findViewById(R.id.s2);
        slotCounts[7] = findViewById(R.id.s3);
        slotCounts[8] = findViewById(R.id.s4);
        slotCounts[9] = findViewById(R.id.s5);
        slotCounts[10] = findViewById(R.id.countWarlock);

        pactSlotLevel = findViewById(R.id.pactSlotLevel);
        sPoints = findViewById(R.id.sPoints);
        updateSlots();


    }//end onCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_app_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }//end onCreateoptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_undo:
                drake.undoFunction(12);
                updateSlots();
                return true;

            case R.id.action_load:
                SharedPreferences dataFileLoad = this.getPreferences(MODE_PRIVATE);
                int[] slotsLoad = new int[drake.getSlots().length];
                drake.setWarlockLevel(dataFileLoad.getInt("warlockLevel" , 0));
                drake.setSorcererLevel(dataFileLoad.getInt("sorcererLevel" , 0));
                drake.setTypeSlotsWarlock(dataFileLoad.getInt("typeSlotsWarlock" , 0));
                drake.setSorceryPoints(dataFileLoad.getInt("sorceryPoints" , 0));
                for(int index = 0; index < slotsLoad.length; index++){
                    slotsLoad[index] = dataFileLoad.getInt("slots_" + index , 0);
                }//end reconstruct array
                drake.setSlots(slotsLoad);
                updateSlots();
                return true;

            case R.id.action_save:
                SharedPreferences dataFileSave = this.getPreferences(MODE_PRIVATE);
                int[] slotsSave = drake.getSlots();
                SharedPreferences.Editor dataEditor = dataFileSave.edit();
                dataEditor.putInt("warlockLevel", drake.getWarlockLevel());
                dataEditor.putInt("sorcererLevel", drake.getSorcererLevel());
                dataEditor.putInt("typeSlotsWarlock", drake.getTypeSlotsWarlock());
                dataEditor.putInt("sorceryPoints", drake.getSorceryPoints());
                for (int index = 0; index < slotsSave.length; index++) {
                    dataEditor.putInt("slots_" + index, slotsSave[index]);
                }//end deconstruct array
                dataEditor.apply();
                Toast.makeText(this, "Your data has been saved", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_manual_override:
                Intent intent = new Intent(this , ManualOverride.class);
                startActivityForResult(intent , 2);
                return true;

            case R.id.action_about:
                DialogFragment newFragment = new AboutDialog();
                newFragment.show(getSupportFragmentManager(), "about");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }//end switch

    }//end onOptionsitemSelected

    public void saveCharacter(){
        SharedPreferences dataFileSave = this.getPreferences(MODE_PRIVATE);
        int[] slotsSave = drake.getSlots();
        SharedPreferences.Editor dataEditor = dataFileSave.edit();
        dataEditor.putInt("warlockLevel", drake.getWarlockLevel());
        dataEditor.putInt("sorcererLevel", drake.getSorcererLevel());
        dataEditor.putInt("typeSlotsWarlock", drake.getTypeSlotsWarlock());
        dataEditor.putInt("sorceryPoints", drake.getSorceryPoints());
        for (int index = 0; index < slotsSave.length; index++) {
            dataEditor.putInt("slots_" + index, slotsSave[index]);
        }//end deconstruct array
        dataEditor.apply();
        Toast.makeText(this, "Your data has been saved", Toast.LENGTH_SHORT).show();

    }

    public void updateSlots(){

        int[] slots = drake.getSlots();
        for(int index = 0; index < slotCounts.length; index++){
            slotCounts[index].setText(Integer.toString(slots[index]));
        }//end for
        pactSlotLevel.setText("L" + Integer.toString(drake.getTypeSlotsWarlock()));
        sPoints.setText(Integer.toString(drake.getSorceryPoints()));
        warlockLevel.setHint(Integer.toString(drake.getWarlockLevel()));
        sorcererLevel.setHint(Integer.toString(drake.getSorcererLevel()));
    }//end updateSlots

    public void setLevels(View view){
        String wLString = warlockLevel.getText().toString();
        int wL = -1;
        String sLString = sorcererLevel.getText().toString();
        int sL = -1;
        if(wLString.length() > 0){
            wL = Integer.parseInt(wLString);
        }//end if warlock not empty

        if(sLString.length() > 0){
            sL = Integer.parseInt(sLString);
        }//end if sorcerer not empty

        drake.setBothLevels(wL , sL);
        //warlockLevel.setHint(Integer.toString(drake.getWarlockLevel()));
        warlockLevel.getText().clear();
        //sorcererLevel.setHint(Integer.toString(drake.getSorcererLevel()));
        sorcererLevel.getText().clear();
        updateSlots();

    }//end setLevels

    public void createGenSPDialog(View view) {
        DialogFragment newFragment = new CreateSorceryPointsDialog(drake);
        newFragment.show(getSupportFragmentManager(), "genSPD");
    }//end create genspdialog

    public void runSlotGenActivity(View view){
        int[] sorceryPointRestPool = drake.getSorceryPointRestPool();
        int sorcererLevel = drake.getSorcererLevel();
        Intent intent = new Intent(this , SlotGeneratorActivity.class);
        intent.putExtra("sorceryPointRestPool" , sorceryPointRestPool);
        intent.putExtra("sorcererLevel" , sorcererLevel);
        startActivityForResult(intent , 1);
    }//end runSlotGenAct

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            drake.useLeftoverSP();
            drake.makeTempSlots(data.getIntArrayExtra("slotsToCreate"));
            drake.updateWarlockRules();
            updateSlots();
        }//end if slot data sent back
        if(requestCode == 2 && resultCode == RESULT_OK){
            //sorcererLevel.setHint(Integer.toString(data.getIntExtra("returnData" , 10)));
            //sorcererLevel.setHint(data.getStringExtra("returnData"));
            int[] overrideData = data.getIntArrayExtra("returnData");
            int[] newSlots = drake.getSlots();
            for(int index = 0; index < newSlots.length; index++){
                if(overrideData[index] != -1){
                    newSlots[index] = overrideData[index];
                }//end if find override data that isn't -1 default
            }//end for go through only the spell slot data
            drake.setSlots(newSlots);
            if(overrideData[11] != -1){
                drake.setWarlockLevel(overrideData[11]);
            }//end if warlock Level
            if(overrideData[12] != -1){
                drake.setSorcererLevel(overrideData[12]);
            }//end if sorcerer Level
            if(overrideData[13] != -1){
                drake.setTypeSlotsWarlock(overrideData[13]);
            }//end if warlock Type
            if(overrideData[14] != -1){
                drake.setSorceryPoints(overrideData[14]);
            }//end if SP
            updateSlots();
        }//end else if back from Manual override
        if(requestCode == 3 && resultCode == RESULT_OK){
            int[] bookData = data.getIntArrayExtra("book_data");
            int level = bookData[0];
            if(bookData[0] > 0){//copied from cast method adjusted for returned data
                int usedRegSlots = drake.removeSlots(1 , level);
                if(usedRegSlots == 1){
                    level += 5;
                }//end correct for using reg slots
                drake.undoFunction(level - 1);//buffer spellcasting use for future undo
            }//end detect if a spellslot was used
            if(bookData[1] == 1){
                drake.setSorceryPoints(drake.getSorcererLevel());
            }//end if Sp restored
            updateSlots();
        }//end if hack book data return



    }// end override onactivityresult

    public void useSP(View view){
        drake.undoFunction(11);//buffer Sp use for future undo
        int sp = drake.getSorceryPoints();
        if(sp > 0){
            drake.setSorceryPoints(sp - 1);
            sPoints.setText(Integer.toString(drake.getSorceryPoints()));
        }//end if any sp
    }//end useSP

    public void castSpell(View view){
        int level = Integer.parseInt((String)view.getTag());
        int usedRegSlots = drake.removeSlots(1 , level);
        if(usedRegSlots == 1){
            level += 5;
        }//end correct for using reg slots
        drake.undoFunction(level - 1);//buffer spellcasting use for future undo
        updateSlots();
    }//end cast spell

    public void openBook(View view){
        int INTMod = 0;
        Intent intent = new Intent(this , HackBookOfShadows.class);
        intent.putExtra("int_mod" , INTMod);
        intent.putExtra("warlock_slot_level" , drake.getTypeSlotsWarlock());
        intent.putExtra("available_slots" , drake.getSlots());
        startActivityForResult(intent , 3);
    }//end openBook

}//end class