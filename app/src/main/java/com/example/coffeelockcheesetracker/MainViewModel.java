package com.example.coffeelockcheesetracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    //private MutableLiveData<SlotEngine> engineData;
    private SlotEngine engine;
    private MutableLiveData<String> warlockLevel;
    private MutableLiveData<String> sorcererLevel;
    private MutableLiveData<int[]> slotCounts;
    private MutableLiveData<String> pactSlotLevel;
    private MutableLiveData<String> sPoints;




    public MainViewModel(){
        engine = new SlotEngine();

        warlockLevel = new MutableLiveData<>();
        warlockLevel.setValue(Integer.toString(engine.getWarlockLevel()));

        sorcererLevel = new MutableLiveData<>();
        sorcererLevel.setValue(Integer.toString(engine.getSorcererLevel()));

        slotCounts = new MutableLiveData<>();
        slotCounts.setValue(engine.getSlots());

        pactSlotLevel = new MutableLiveData<>();
        pactSlotLevel.setValue(Integer.toString(engine.getTypeSlotsWarlock()));

        sPoints = new MutableLiveData<>();
        sPoints.setValue(Integer.toString(engine.getSorceryPoints()));




    }

    /*public LiveData<SlotEngine> getEngine(){
        if(engine == null){
            engineData = new MutableLiveData<SlotEngine>();
            loadEngine();
        }//end if
        return engine;
    }//end getEngine*/

    public SlotEngine loadEngine(){
        return engine;
    }

    public LiveData<String> getWarlockLevel(){
        return warlockLevel;
    }
    public LiveData<String> getSorcererLevel(){
        return sorcererLevel;
    }
    public LiveData<int[]> getSlots(){
        return slotCounts;
    }
    public LiveData<String> getPactSlotLevel(){
        return pactSlotLevel;
    }
    public LiveData<String> getSorceryPoints(){
        return sPoints;
    }

}
