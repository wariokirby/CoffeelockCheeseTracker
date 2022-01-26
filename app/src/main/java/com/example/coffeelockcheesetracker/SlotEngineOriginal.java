package com.example.coffeelockcheesetracker;

public class SlotEngineOriginal {
    private int warlockLevel;
    private int sorcererLevel;
    private int[] slots;
    private int typeSlotsWarlock;
    private int sorceryPoints;
    private int[] undoBuffer = {-1 , -1 , -1 , -1 , -1};

    public SlotEngineOriginal(){
        warlockLevel = 3;
        sorcererLevel = 2;
        slots = new int[11];//0-4 coffeelock slots, 5-9 Sorcerer slots, 10 warlock slots
        typeSlotsWarlock = 1;
        sorceryPoints = 0;

        updateWarlockRules();
        updateSorcererRules();

    }//end constructor

    public int getWarlockLevel(){
        return warlockLevel;
    }//end getWL
    public void setWarlockLevel(int warlockLevel) {
        this.warlockLevel = warlockLevel;
    }//end setWL

    public int getSorcererLevel(){
        return sorcererLevel;
    }//end getSL
    public void setSorcererLevel(int sorcererLevel) {
        this.sorcererLevel = sorcererLevel;
    }//end setSL

    public void setBothLevels(int warlockLevel , int sorcererLevel){
        if(warlockLevel >= 0){
            this.warlockLevel = warlockLevel;
            updateWarlockRules();
        }//end if warlock changed
        if(sorcererLevel >= 0){
            this.sorcererLevel = sorcererLevel;
            this.sorceryPoints = sorcererLevel;
            updateSorcererRules();
        }//end if sorcerer changed

    }//end setLevels

    public int[] getSlots(){
        return slots;
    }//end getSlots
    public void setSlots(int[] slots) {
        this.slots = slots;
    }//end setSlots

    public int getTypeSlotsWarlock(){
        return typeSlotsWarlock;
    }//end getTSW
    public void setTypeSlotsWarlock(int typeSlotsWarlock) {
        this.typeSlotsWarlock = typeSlotsWarlock;
    }//end setTSW

    public int getSorceryPoints() {
        return sorceryPoints;
    }//end getSP
    public void setSorceryPoints(int sorceryPoints) {
        this.sorceryPoints = sorceryPoints;
    }//end setSP

    public void updateWarlockRules(){
        if(warlockLevel == 0){
            slots[10] = 0;
            typeSlotsWarlock = 1;
        }//end no warlock slots
        else if(warlockLevel == 1){
            slots[10] = 1;
            typeSlotsWarlock = 1;
        }//end L1 slots
        else if(warlockLevel == 2){
            slots[10] = 2;
            typeSlotsWarlock = 1;
        }//end L2 slots
        else if(warlockLevel <= 4){
            slots[10] = 2;
            typeSlotsWarlock = 2;
        }//end L3-4 slots
        else if(warlockLevel <= 6){
            slots[10] = 2;
            typeSlotsWarlock = 3;
        }//end L5-6 slots
        else if(warlockLevel <= 8){
            slots[10] = 2;
            typeSlotsWarlock = 4;
        }//end L7-8 slots
        else if(warlockLevel <= 10){
            slots[10] = 2;
            typeSlotsWarlock = 5;
        }//end L9-10 slots
        else if(warlockLevel <= 16){
            slots[10] = 3;
            typeSlotsWarlock = 5;
        }//end L11-16 slots
        else if(warlockLevel <= 20){
            slots[10] = 4;
            typeSlotsWarlock = 5;
        }//end L17-20 slots
        else{
            slots[10] = -1;
            typeSlotsWarlock = -1;
        }//end invalid level
    }//end updateWarlockRules

    public void updateSorcererRules(){
        if(sorcererLevel == 0){
            for(int index = 5; index < slots.length - 1; index++){
                slots[index] = 0;
            }//end for
            sorceryPoints = 0;
        }//end L0
        else if(sorcererLevel == 1){
            slots[5] = 2;
            for(int index = 6; index < slots.length - 1; index++){
                slots[index] = 0;
            }//end for
            sorceryPoints = 0;
        }//end L1
        else if(sorcererLevel == 2){
            slots[5] = 3;
            for(int index = 6; index < slots.length - 1; index++){
                slots[index] = 0;
            }//end for
        }//end L2
        else if(sorcererLevel == 3){
            slots[5] = 4;
            slots[6] = 2;
            for(int index = 7; index < slots.length - 1; index++){
                slots[index] = 0;
            }//end for
        }//end L3
        else if(sorcererLevel == 4){
            slots[5] = 4;
            slots[6] = 3;
            for(int index = 7; index < slots.length - 1; index++){
                slots[index] = 0;
            }//end for
        }//end L4
        else if(sorcererLevel == 5){
            slots[5] = 4;
            slots[6] = 3;
            slots[7] = 2;
            for(int index = 8; index < slots.length - 1; index++){
                slots[index] = 0;
            }//end for
        }//end L5
        else if(sorcererLevel == 6){
            slots[5] = 4;
            slots[6] = 3;
            slots[7] = 3;
            for(int index = 8; index < slots.length - 1; index++){
                slots[index] = 0;
            }//end for
        }//end L6
        else if(sorcererLevel == 7){
            slots[5] = 4;
            slots[6] = 3;
            slots[7] = 3;
            slots[8] = 1;
            slots[9] = 0;
        }//end L7
        else if(sorcererLevel == 8){
            slots[5] = 4;
            slots[6] = 3;
            slots[7] = 3;
            slots[8] = 2;
            slots[9] = 0;
        }//end L8
        else if(sorcererLevel == 9){
            slots[5] = 4;
            slots[6] = 3;
            slots[7] = 3;
            slots[8] = 3;
            slots[9] = 1;
        }//end L9
        else if(sorcererLevel == 10){
            slots[5] = 4;
            slots[6] = 3;
            slots[7] = 3;
            slots[8] = 3;
            slots[9] = 2;
        }//end L10
        else{
            for(int index = 5; index < slots.length - 1; index++){
                slots[index] = -1;
            }//end for
        }//end invalid level
        if(sorcererLevel >= 2){
            sorceryPoints = sorcererLevel;
        }//end update SP
    }//end update sorcerer

    public void useLeftoverSP(){
        //use left over SPs
        if(sorceryPoints == 2){
            slots[0]++;
        }//end LO2
        else if(sorceryPoints == 3){
            slots[1]++;
        }//end LO3
        else if(sorceryPoints == 4){
            slots[0] += 2;
        }//end LO4
        else if(sorceryPoints == 5){
            slots[2]++;
        }//end LO5
        else if(sorceryPoints == 6){
            slots[3]++;
        }//end LO6
        else if(sorceryPoints == 7){
            slots[4]++;
        }//end LO7
        else if(sorceryPoints == 8){
            slots[3]++;
            slots[0]++;
        }//end LO8
        else if(sorceryPoints == 9){
            slots[4]++;
            slots[0]++;
        }//end LO9
        else{

            return;
        }//end outside of range
        sorceryPoints = 0;

    }//end use lefover SP

    public int removeSlots(int howMany , int level){//return key: -1 insufficient slots, 0 enough slots, 1 used regular sorcerer slots
        if(level == 11){
            if(slots[10] > 0){
                slots[10]--;
                return 0;
            }//end if any left
            else{
                return -1;
            }//end none left
        }//end fix for warlock slots

        else if(howMany <= (slots[level - 1] + slots[level+4]) * level){
            if((slots[level - 1] * level) >= howMany){
                slots[level - 1] -= howMany/level;
                //slots[10] = 9;
                if(howMany % level != 0){
                    slots[level - 1]--;
                }//in case it makes more slots than asked
                return 0;
            }//enough temp slots
            else{
                howMany -= slots[level - 1] * level;
                slots[level-1] = 0;
                slots[level + 4] -= howMany/level;
                if(howMany % level != 0){
                    slots[level + 4]--;
                }//in case it makes more slots than asked
                return 1;
            }//take from reg slots
        }//enough total slots
        else{
            return -1;
        }//insufficient slots
    }//end removeSlots

    public void spGenerator(int numSP , int whichSlot){//which slot is slot level - 1
        whichSlot++;
        numSP++;
        int success = removeSlots(numSP , whichSlot);
        if(success >= 0){
            sorceryPoints += numSP;
            if (sorceryPoints > sorcererLevel){
                sorceryPoints = sorcererLevel;
            }//can\t go above max SP
        }//add SP if removeslots works
    }//end spGen

    public int[] getSorceryPointRestPool(){
        int[] sorceryPointRestPool = new int[4];
        //short rest
        if(slots[10] * typeSlotsWarlock < sorcererLevel * slots[10]){
            sorceryPointRestPool[0] = slots[10] * typeSlotsWarlock;
        }//end if insufficient warlock slots to max out points
        else{
            sorceryPointRestPool[0] = sorcererLevel * slots[10];
        }//end max points short rest
        //end short rest

        //long rest
        if(typeSlotsWarlock > sorcererLevel){
            if(warlockLevel <= 10){
                sorceryPointRestPool[1] = 6 * 2 * sorcererLevel + sorceryPointRestPool[0];
            }//end warlock has 2 slots
            else if(warlockLevel <= 16){
                sorceryPointRestPool[1] = 6 * 3 * sorcererLevel + sorceryPointRestPool[0];
            }//end warlock has 3 slots
            else{
                sorceryPointRestPool[1] = 6 * 4 * sorcererLevel + sorceryPointRestPool[0];
            }//end warlock has 4 slots


        }//end each warlock slot maxes out SP
        else{
            if(warlockLevel <= 10){
                sorceryPointRestPool[1] += (6 * 2 * typeSlotsWarlock) + sorceryPointRestPool[0];
            }//end warlock has 2 slots
            else if(warlockLevel <= 16){
                sorceryPointRestPool[1] += 6 * 3 * typeSlotsWarlock + sorceryPointRestPool[0];
            }//end warlock has 3 slots
            else{
                sorceryPointRestPool[1] += 6 * 4 * typeSlotsWarlock + sorceryPointRestPool[0];
            }//end warlock has 4 slots

        }//end warlock slots do not max out SP
        // end long rest

        //down time
        if(warlockLevel <= 10){
            sorceryPointRestPool[2] += 7 * 2 * sorcererLevel + sorceryPointRestPool[0];
        }//end warlock has 2 slots
        else if(warlockLevel <= 16){
            sorceryPointRestPool[2] += 7 * 3 * sorcererLevel + sorceryPointRestPool[0];
        }//end warlock has 3 slots
        else{
            sorceryPointRestPool[2] += 7 * 4 * sorcererLevel + sorceryPointRestPool[0];
        }//end warlock has 4 slots
        //updateSorcererRules();

        //end down time
        //updateWarlockRules();
        return sorceryPointRestPool;

    }//end getSorceryPointRestPool

    /*public int getSorceryPointRestPoolMaxCheese(int numLongRests){
        int sorceryPointRestPool = 0;
        if(warlockLevel <= 16){
            sorceryPointRestPool += numLongRests * 7 * 2 * sorcererLevel;
        }//end warlock has 2 slots
        else{
            sorceryPointRestPool += numLongRests * 7 * 3 * sorcererLevel;
        }//end warlock has 3 slots
        return sorceryPointRestPool;
    }//end max cheese*/

    public void makeTempSlots(int[] tempSlots){
        for(int index = 0; index < 5; index++){
            slots[index] += tempSlots[index];
        }//end for fill in temp slots
    }//end makeTempSlots

    public void undoFunction(int level){//0-10 slots, 11 is SP, 12 is perform the undo if anything in the buffer >0
        //buffer changes
        if(level <= 11){
            for(int index = undoBuffer.length - 1; index > 0; index--){
                undoBuffer[index] = undoBuffer[index - 1];
            }//end push
            undoBuffer[0] = level;
        }//if buffering
        else{
            if(undoBuffer[0] == 11){
                sorceryPoints++;
            }//end undo SP
            else if(undoBuffer[0] < 0){
                return;
            }//end buffer empty
            else{
                slots[undoBuffer[0]]++;
            }//end undo cast
            for(int index = 0; index < undoBuffer.length - 1; index++){
                undoBuffer[index] = undoBuffer[index + 1];
            }//end pop
            undoBuffer[undoBuffer.length - 1] = -1;
        }//else undo last action casting and sorcery points use only

    }//end undo


}//end class

