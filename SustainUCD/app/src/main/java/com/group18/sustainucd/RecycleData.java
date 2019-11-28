package com.group18.sustainucd;

import java.io.Serializable;

public class RecycleData implements Serializable {
    private int nameID;
    private int descriptionID;

    public RecycleData(int nameID, int descriptionID) {
        this.nameID = nameID;
        this.descriptionID = descriptionID;
    }

    public int getNameID() {
        return nameID;
    }

    public void setNameID(int nameID) {
        this.nameID = nameID;
    }

    public int getDescriptionID() {
        return descriptionID;
    }

    public void setDescriptionID(int descriptionID) {
        this.descriptionID = descriptionID;
    }
}
