package com.example.coffeelockcheesetracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HackBookOfShadows extends AppCompatActivity {

    private Spinner spellChoice;
    private TextView spellDescription;
    private ScrollView spellDescriptionScrollWindow;
    private Spinner levelChoice;
    private TextView atWhatLevel;

    private TextView intCheckText;
    private EditText intCheck;
    private TextView bookIntModifier;
    private Button manualRollIntCheck;
    private Button autoRollIntCheck;
    private Button openAdditionalModifierDialog;

    private TextView intCheckSF;
    private EditText wildMagicRoll;
    private Button useManualWildMagicRoll;
    private Button autoWildMagicRoll;

    private TextView wildMagicEffect;
    private TextView wildMagicSupplementalText1;
    private TextView wildMagicSupplementalText2;
    private Button returnButton;

    private String[] wildMagicSurgeTable;
    private int warlockSpellSlotLevel;
    private int[] availableSlots;
    private int INTMod;
    private int[] bookData;
    private ArrayList<Spell> hackSpells;
    private List<String> hackSpellNames;
    private Spell chosenSpell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hack_book_of_shadows);

        Toolbar hackBOSAppBar = (Toolbar) findViewById(R.id.toolbarHackBookOfShadows);
        setSupportActionBar(hackBOSAppBar);
        ActionBar ab = getSupportActionBar();
        //ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        INTMod = intent.getIntExtra("int_mod" , 0);
        warlockSpellSlotLevel = intent.getIntExtra("warlock_slot_level" , 0);
        availableSlots = intent.getIntArrayExtra("available_slots");

        wildMagicSurgeTable = new String[50];
        fillWildMagicTable();
        bookData = new int[2];//0=spell level 1=restore all SP if 1
        chosenSpell = new Spell();

        hackSpells = new ArrayList<Spell>();
        hackSpellNames = new ArrayList<String>();
        fillSpellSpinnerArrayLists();

        spellDescriptionScrollWindow = (ScrollView)findViewById(R.id.bookSpellDescriptionScrollWindow);
        spellDescription = (TextView) findViewById(R.id.bookSpellDescription);
        atWhatLevel = (TextView)findViewById(R.id.bookAtWhatLevel);

        intCheckText = (TextView)findViewById(R.id.bookMakeIntCheckText);
        intCheck = (EditText)findViewById(R.id.bookINTCheck);
        bookIntModifier = (TextView)findViewById(R.id.bookIntModifier);
        bookIntModifier.setText("+" + INTMod);
        manualRollIntCheck = (Button)findViewById(R.id.bookUseManualINTRoll);
        autoRollIntCheck = (Button)findViewById(R.id.bookAutoINTRoll);
        openAdditionalModifierDialog = (Button)findViewById(R.id.bookExtraModifierButton);

        intCheckSF = (TextView)findViewById(R.id.bookIntCheckSF);
        wildMagicRoll = (EditText)findViewById(R.id.bookWildMagicRoll);
        useManualWildMagicRoll = (Button)findViewById(R.id.bookUseManualWildMagicRoll);
        autoWildMagicRoll = (Button)findViewById(R.id.bookAutoWildMagicRoll);

        wildMagicEffect = (TextView)findViewById(R.id.bookWildMagicEffect);
        wildMagicSupplementalText1 = (TextView)findViewById(R.id.bookWildMagicSupplementalText1);
        wildMagicSupplementalText2 = (TextView)findViewById(R.id.bookWildMagicSupplementalText2);
        returnButton = (Button)findViewById(R.id.bookReturnButton);


        spellChoice = (Spinner) findViewById(R.id.bookSpellSpinner);
        ArrayAdapter<String> bookSpellArrayAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , hackSpellNames){
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
        bookSpellArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spellChoice.setAdapter(bookSpellArrayAdapter);
        spellChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    setWildMagicVisibility(false);
                    levelChoice.setSelection(0);
                    spellDescriptionScrollWindow.setVisibility(View.VISIBLE);
                    spellDescription.setText(hackSpells.get(i-1).toString());
                    chosenSpell = hackSpells.get(i-1);
                    if(chosenSpell.getLevel() < 1){
                        levelChoice.setVisibility(View.INVISIBLE);
                        atWhatLevel.setVisibility(View.INVISIBLE);
                        setIntCheckVisibility(true);
                        bookData[0] = 0;
                    }//make levelChoice disappear if cantrip
                    else if(chosenSpell.getLevel() > warlockSpellSlotLevel){
                        levelChoice.setVisibility(View.INVISIBLE);
                        atWhatLevel.setText(getResources().getString(R.string.book_overload_warning));
                        bookData[0] = -1;
                        setIntCheckVisibility(true);
                    }
                    else{
                        levelChoice.setVisibility(View.VISIBLE);
                        atWhatLevel.setText(getResources().getString(R.string.book_which_level));
                        atWhatLevel.setVisibility(View.VISIBLE);
                        setIntCheckVisibility(false);
                    }//make it reappear but cloak intcheck
                }//end if something other than hint
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        levelChoice = (Spinner) findViewById(R.id.bookLevelSpinner);
        String[] levelStrings = new String[]{"level..." , "1" , "2" , "3" , "4" , "5" , "Warlock L" + warlockSpellSlotLevel};
        List<String> levelList = new ArrayList<>(Arrays.asList(levelStrings));
        ArrayAdapter<String> levelChoiceArrayAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , levelList){
            @Override
            public boolean isEnabled(int position){
                if(position < chosenSpell.getLevel()){
                    return false;
                }
                else if(position == 6 && availableSlots[10] == 0){
                    return false;
                }
                else if(position !=6 &&(availableSlots[position - 1] + availableSlots[position + 4] == 0)){
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
                if(position < chosenSpell.getLevel()){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else if(position == 6 && availableSlots[10] == 0){
                    tv.setTextColor(Color.GRAY);
                }
                else if(position !=6 &&(availableSlots[position - 1] + availableSlots[position + 4] == 0)){
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        levelChoiceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelChoice.setAdapter(levelChoiceArrayAdapter);
        levelChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i > 0){
                    if(i == 6){
                        bookData[0] = 11;
                    }
                    else{
                        bookData[0] = i;//levels match position here except warlock
                    }

                    setIntCheckVisibility(true);
                }//end if something other than hint
             }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }//end onCreate

    public void useThisRollButton(View view){
        spellDescriptionScrollWindow.setVisibility(View.GONE);
        spellChoice.setVisibility(View.GONE);
        levelChoice.setVisibility(View.GONE);
        if(bookData[0] < 0){//correct for overpowered spells
            atWhatLevel.setText(chosenSpell.getName() + " Level " + chosenSpell.getLevel());
        }
        else if(bookData[0] > 5){
            atWhatLevel.setText(chosenSpell.getName() + " Level " + warlockSpellSlotLevel);
        }
        else{
            atWhatLevel.setText(chosenSpell.getName() + " Level " + bookData[0]);
        }
        atWhatLevel.setVisibility(View.VISIBLE);
        String intCheckString = intCheck.getText().toString();
        if(intCheckString.length() > 0){
            int intCheck = Integer.parseInt(intCheckString) + Integer.parseInt(bookIntModifier.getText().toString());
            if(chosenSpell.getLevel() > warlockSpellSlotLevel){
                if(intCheck >= 20){
                    intCheckSF.setText(getResources().getString(R.string.book_roll_wild_magic_forced_success));
                }
                else{
                    intCheckSF.setText(getResources().getString(R.string.book_roll_wild_magic_forced_failure));
                }
                setWildMagicVisibility(true);
            }//end spell higher level than can be cast
            else if(chosenSpell.isClassSpell()){
                if(intCheck >= (12 + chosenSpell.getLevel())){
                    intCheckSF.setText(getResources().getString(R.string.book_roll_wild_magic_success));
                    setWildMagicVisibility(false);
                    intCheckSF.setVisibility(View.VISIBLE);
                    returnButton.setVisibility(View.VISIBLE);
                }//succeed
                else{
                    intCheckSF.setText(getResources().getString(R.string.book_roll_wild_magic_fail));
                    setWildMagicVisibility(true);
                    returnButton.setVisibility(View.INVISIBLE);
                }//fail

            }//end class spell
            else{
                if(intCheck >= (15 + chosenSpell.getLevel())){
                    intCheckSF.setText(getResources().getString(R.string.book_roll_wild_magic_success));
                    setWildMagicVisibility(false);
                    intCheckSF.setVisibility(View.VISIBLE);
                    returnButton.setVisibility(View.VISIBLE);
                }//succeed
                else{
                    intCheckSF.setText(getResources().getString(R.string.book_roll_wild_magic_fail));
                    setWildMagicVisibility(true);
                    returnButton.setVisibility(View.INVISIBLE);
                }//fail
            }//end not class spell
        }//end if anything entered

    }//end usethisrollbutton

    public void rollIntCheckForMeButton(View view){
        intCheck.setText(Integer.toString((int)(Math.random() * 20) + 1));
    }//end roll for me int

    public void createAddModDialog(View view) {
        DialogFragment newFragment = new AddModifierDialog();
        newFragment.show(getSupportFragmentManager(), "addMod");
    }//end create addModdialog


    public void setModifierFromDialog(String mod){
        int newMod = INTMod + Integer.parseInt(mod);
        if(newMod >= 0){
            bookIntModifier.setText("+" + newMod);
        }//end if forgot +
        else{
            bookIntModifier.setText(Integer.toString(newMod));
        }
    }//end getmodfromdialog

    public void useThisRollWildMagicButton(View view){
        wildMagicSupplementalText1.setText("");
        wildMagicSupplementalText2.setText("");
        returnButton.setVisibility(View.VISIBLE);
        String wildMagicRollString = wildMagicRoll.getText().toString();
        int itemPosition;
        if(wildMagicRollString.length() > 0){
            int wildMagicRoll = Integer.parseInt(wildMagicRollString);
            itemPosition = wildMagicRoll / 2;
            if(wildMagicRoll % 2 == 0){
                itemPosition--;
            }//end correction for even numbers
            wildMagicEffect.setText(wildMagicSurgeTable[itemPosition]);
            wildMagicEffect.setVisibility(View.VISIBLE);
            if(itemPosition == 3){
                wildMagicSupplementalText1.setText("FIREBALL\n" + getResources().getString(R.string.fireball));
            }
            else if(itemPosition == 4 || itemPosition == 25){
                wildMagicSupplementalText1.setText("MAGIC MISSILE\n" + getResources().getString(R.string.magic_missile));
            }
            else if(itemPosition == 6){
                wildMagicSupplementalText1.setText("CONFUSION\n" + getResources().getString(R.string.confusion));
            }
            else if(itemPosition == 9){
                wildMagicSupplementalText1.setText("GREASE\n" + getResources().getString(R.string.grease));
                wildMagicSupplementalText2.setText(getResources().getString(R.string.prone));
            }
            else if(itemPosition == 11){
                wildMagicSupplementalText1.setText("REMOVE CURSE\n" + getResources().getString(R.string.remove_curse));
            }
            else if(itemPosition == 18){
                wildMagicSupplementalText1.setText("The mysterious flumphs drift through the Underdark, propelled through the air by the jets whose sound gives them their name. A flumph glows faintly, reflecting its moods in its color. Soft pink means it is amused, deep blue is sadness, green expresses curiosity, and crimson is anger.\n Intelligent and Wise. Flumphs communicate telepathically. Though they resemble jellyfish, flumphs are sentient beings of great intelligence and wisdom, possessing advanced knowledge of religion, philosophy, mathematics, and countless other subjects.");
                wildMagicSupplementalText2.setText(getResources().getString(R.string.frightened));
            }
            else if(itemPosition == 20){
                wildMagicSupplementalText1.setText(getResources().getString(R.string.incapacitated));
            }
            else if(itemPosition == 22){
                wildMagicSupplementalText1.setText("LEVITATE\n" + getResources().getString(R.string.levitate));
            }
            else if(itemPosition == 31){
                wildMagicSupplementalText1.setText("FOG CLOUD\n" + getResources().getString(R.string.fog_cloud));
            }
            else if(itemPosition == 33){
                wildMagicSupplementalText1.setText(getResources().getString(R.string.frightened));
            }
            else if(itemPosition == 34 || itemPosition == 44){
                wildMagicSupplementalText1.setText(getResources().getString(R.string.invisible));
            }
            else if(itemPosition == 36){
                wildMagicSupplementalText1.setText(getResources().getString(R.string.poisoned));
            }
            else if(itemPosition == 37){
                wildMagicSupplementalText1.setText(getResources().getString(R.string.blinded));
            }
            else if(itemPosition == 38){
                wildMagicSupplementalText1.setText("POLYMORPH\n" + getResources().getString(R.string.polymorph));
            }
            else if(itemPosition == 42){
                wildMagicSupplementalText1.setText("MIRROR IMAGE\n" + getResources().getString(R.string.mirror_image));
            }
            else if(itemPosition == 43){
                wildMagicSupplementalText1.setText("FLY\n" + getResources().getString(R.string.fly));
            }
            else if(itemPosition == 45){
                wildMagicSupplementalText1.setText("REINCARNATE\n" + getResources().getString(R.string.reincarnate));
            }
            else if(itemPosition == 49){
                bookData[1] = 1;
            }

        }
    }//end use this roll wild magic

    public void rollWildMagicForMeButton(View view){
        wildMagicRoll.setText(Integer.toString((int)(Math.random() * 100) + 1));
    }//end roll for me int


    private void setIntCheckVisibility(boolean visible){
        if(visible){
            intCheckText.setVisibility(View.VISIBLE);
            intCheck.setVisibility(View.VISIBLE);
            bookIntModifier.setVisibility(View.VISIBLE);
            manualRollIntCheck.setVisibility(View.VISIBLE);
            autoRollIntCheck.setVisibility(View.VISIBLE);
            openAdditionalModifierDialog.setVisibility(View.VISIBLE);
        }
        else{
            intCheckText.setVisibility(View.INVISIBLE);
            intCheck.setVisibility(View.INVISIBLE);
            bookIntModifier.setVisibility(View.INVISIBLE);
            manualRollIntCheck.setVisibility(View.INVISIBLE);
            autoRollIntCheck.setVisibility(View.INVISIBLE);
            openAdditionalModifierDialog.setVisibility(View.INVISIBLE);

        }
    }//end intcheck visibility

    private void setWildMagicVisibility(boolean visible){
        if(visible){
            intCheckSF.setVisibility(View.VISIBLE);
            wildMagicRoll.setVisibility(View.VISIBLE);
            useManualWildMagicRoll.setVisibility(View.VISIBLE);
            autoWildMagicRoll.setVisibility(View.VISIBLE);

        }
        else{
            intCheckSF.setVisibility(View.INVISIBLE);
            wildMagicRoll.setVisibility(View.INVISIBLE);
            useManualWildMagicRoll.setVisibility(View.INVISIBLE);
            autoWildMagicRoll.setVisibility(View.INVISIBLE);

        }
    }//end wildmagic visibility

    private void fillWildMagicTable(){
        wildMagicSurgeTable[0] = "Roll on this table at the start of each of your turns for the next minute, ignoring this result on subsequent rolls.";
        wildMagicSurgeTable[1] = "For the next minute, you can see any invisible creature if you have line of sight to it.";
        wildMagicSurgeTable[2] = "A modron chosen and controlled by the DM appears in an unoccupied space within 5 feet of you, then disappears 1 minute later.";
        wildMagicSurgeTable[3] = "You cast fireball as a 3rd-level spell centered on yourself.";
        wildMagicSurgeTable[4] = "You cast magic missile as a 5th-level spell.";
        wildMagicSurgeTable[5] = "Roll a d10. Your height changes by a number of inches equal to the roll. If the roll is odd, you shrink. If the roll is even, you grow.";
        wildMagicSurgeTable[6] = "You cast confusion centered on yourself.";
        wildMagicSurgeTable[7] = "For the next minute, you regain 5 hit points at the start of each of your turns.";
        wildMagicSurgeTable[8] = "You grow a long beard made of feathers that remains until you sneeze, at which point the feathers explode out from your face.";
        wildMagicSurgeTable[9] = "You cast grease centered on yourself.";
        wildMagicSurgeTable[10] = "Creatures have disadvantage on saving throws against the next spell you cast in the next minute that involves a saving throw.";
        wildMagicSurgeTable[11] = "Your skin turns a vibrant shade of blue. A remove curse spell can end this effect.";
        wildMagicSurgeTable[12] = "An eye appears on your forehead for the next minute. During that time, you have advantage on Wisdom (Perception) checks that rely on sight.";
        wildMagicSurgeTable[13] = "For the next minute, all your spells with a casting time of 1 action have a casting time of 1 bonus action.";
        wildMagicSurgeTable[14] = "You teleport up to 60 feet to an unoccupied space of your choice that you can see.";
        wildMagicSurgeTable[15] = "You are transported to the Astral Plane until the end of your next turn, after which time you return to the space you previously occupied or the nearest unoccupied space if that space is occupied.";
        wildMagicSurgeTable[16] = "Maximize the damage of the next damaging spell you cast within the next minute.";
        wildMagicSurgeTable[17] = "Roll a d10. Your age changes by a number of years equal to the roll. If the roll is odd, you get younger (minimum 1 year old). If the roll is even, you get older.";
        wildMagicSurgeTable[18] = "1d6 flumphs controlled by the DM appear in unoccupied spaces within 60 feet of you and are frightened of you. They vanish after 1 minute.";
        wildMagicSurgeTable[19] = "You regain 2d10 hit points.";
        wildMagicSurgeTable[20] = "You turn into a potted plant until the start of your next turn. While a plant, you are incapacitated and have vulnerability to all damage. If you drop to 0 hit points, your pot breaks, and your form reverts.";
        wildMagicSurgeTable[21] = "For the next minute, you can teleport up to 20 feet as a bonus action on each of your turns.";
        wildMagicSurgeTable[22] = "You cast levitate on yourself.";
        wildMagicSurgeTable[23] = "A unicorn controlled by the DM appears in a space within 5 feet of you, then disappears 1 minute later.";
        wildMagicSurgeTable[24] = "You can’t speak for the next minute. Whenever you try, pink bubbles float out of your mouth.";
        wildMagicSurgeTable[25] = "A spectral shield hovers near you for the next minute, granting you a +2 bonus to AC and immunity to magic missile.";
        wildMagicSurgeTable[26] = "You are immune to being intoxicated by alcohol for the next 5d6 days.";
        wildMagicSurgeTable[27] = "Your hair falls out but grows back within 24 hours.";
        wildMagicSurgeTable[28] = "For the next minute, any flammable object you touch that isn’t being worn or carried by another creature bursts into flame.";
        wildMagicSurgeTable[29] = "You regain your lowest-level expended spell slot.";
        wildMagicSurgeTable[30] = "For the next minute, you must shout when you speak.";
        wildMagicSurgeTable[31] = "You cast fog cloud centered on yourself.";
        wildMagicSurgeTable[32] = "Up to three creatures you choose within 30 feet of you take 4d10 lightning damage.";
        wildMagicSurgeTable[33] = "You are frightened by the nearest creature until the end of your next turn.";
        wildMagicSurgeTable[34] = "Each creature within 30 feet of you becomes invisible for the next minute. The invisibility ends on a creature when it attacks or casts a spell.";
        wildMagicSurgeTable[35] = "You gain resistance to all damage for the next minute.";
        wildMagicSurgeTable[36] = "A random creature within 60 feet of you becomes poisoned for 1d4 hours.";
        wildMagicSurgeTable[37] = "You glow with bright light in a 30-foot radius for the next minute. Any creature that ends its turn within 5 feet of you is blinded until the end of its next turn.";
        wildMagicSurgeTable[38] = "You cast polymorph on yourself. If you fail the saving throw, you turn into a sheep for the spell’s duration.";
        wildMagicSurgeTable[39] = "Illusory butterflies and flower petals flutter in the air within 10 feet of you for the next minute.";
        wildMagicSurgeTable[40] = "You can take one additional action immediately.";
        wildMagicSurgeTable[41] = "Each creature within 30 feet of you takes 1d10 necrotic damage. You regain hit points equal to the sum of the necrotic damage dealt.";
        wildMagicSurgeTable[42] = "You cast mirror image.";
        wildMagicSurgeTable[43] = "You cast fly on a random creature within 60 feet of you.";
        wildMagicSurgeTable[44] = "You become invisible for the next minute. During that time, other creatures can’t hear you. The invisibility ends if you attack or cast a spell.";
        wildMagicSurgeTable[45] = "If you die within the next minute, you immediately come back to life as if by the reincarnate spell.";
        wildMagicSurgeTable[46] = "Your size increases by one size category for the next minute.";
        wildMagicSurgeTable[47] = "You and all creatures within 30 feet of you gain vulnerability to piercing damage for the next minute.";
        wildMagicSurgeTable[48] = "You are surrounded by faint, ethereal music for the next minute.";
        wildMagicSurgeTable[49] = "You regain all expended sorcery points.";
    }//end long ugly process of entering the wild magic table

    private void fillSpellSpinnerArrayLists(){
        hackSpells.add(new Spell("Light" , 0 , getString(R.string.light) , true));
        hackSpells.add(new Spell("Sacred Flame" , 0 , getString(R.string.sacred_flame) , true));
        hackSpells.add(new Spell("Thaumaturgy" , 0 , getString(R.string.thaumaturgy) , true));
        hackSpells.add(new Spell("Burning Hands" , 1 , getString(R.string.burning_hands) , true));
        hackSpells.add(new Spell("Charm Person" , 1 , getString(R.string.charm_person) , true));
        hackSpells.add(new Spell("Command" , 1 , getString(R.string.command) , true));
        hackSpells.add(new Spell("Disguise Self" , 1 , getString(R.string.disguise_self) , true));
        hackSpells.add(new Spell("False Life" , 1 , getString(R.string.false_life) , true));
        hackSpells.add(new Spell("Feather Fall" , 1 , getString(R.string.feather_fall) , true));
        hackSpells.add(new Spell("Identify" , 1 , getString(R.string.identify) , false));
        hackSpells.add(new Spell("Inflict Wounds" , 1 , getString(R.string.inflict_wounds) , true));
        hackSpells.add(new Spell("Mage Armor" , 1 , getString(R.string.mage_armor) , true));
        hackSpells.add(new Spell("Magic Missile" , 1 , getString(R.string.magic_missile) , true));
        hackSpells.add(new Spell("Shield of Faith" , 1 , getString(R.string.shield_of_faith) , true));
        hackSpells.add(new Spell("Sleep" , 1 , getString(R.string.sleep) , true));
        hackSpells.add(new Spell("Unseen Servant" , 1 , getString(R.string.unseen_servant) , true));
        hackSpells.add(new Spell("Hold Person" , 2 , getString(R.string.hold_person) , true));
        hackSpells.add(new Spell("Spiritual Weapon" , 2 , getString(R.string.spiritual_weapon) , true));
        hackSpells.add(new Spell("Animate Dead" , 3 , getString(R.string.animate_dead) , true));
        hackSpells.add(new Spell("Speak With Dead" , 3 , getString(R.string.speak_with_dead) , true));

        hackSpellNames.add("Choose your Spell...");
        for(int index = 0; index < hackSpells.size(); index++){//index will always be 1 greater than hackSpells due to hint space
            hackSpellNames.add(hackSpells.get(index).getName());
        }//end fill name list for spinner
    }//end creating all the spells in the book and making a list of their names for the spinner

    public void returnButton(View view){
        Intent intent = new Intent();
        intent.putExtra("book_data" , bookData);
        setResult(RESULT_OK , intent);
        finish();
    }//end return button
}//end activity