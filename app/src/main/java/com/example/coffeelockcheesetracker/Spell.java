package com.example.coffeelockcheesetracker;

import java.util.Locale;

public class Spell {
    private String name;
    private int level;
    private String description;
    private boolean classSpell;

    public Spell(){
        name = "";
        level = -1;
        description = "";
        classSpell = false;
    }//end 0 arg

    public Spell(String name , int level , String description , boolean classSpell){
        this.name = name.toUpperCase(Locale.ROOT);
        this.level = level;
        this.description = description;
        this.classSpell = classSpell;
    }//end constructor

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public boolean isClassSpell() {
        return classSpell;
    }

    @Override
    public String toString() {
        String output = name + " level ";
        if(level == 0) {
            output += "cantrip";
        }
        else {
            output += level;
        }
        output +="\n" + description;
        return output;
    }//end toString
}//end class
