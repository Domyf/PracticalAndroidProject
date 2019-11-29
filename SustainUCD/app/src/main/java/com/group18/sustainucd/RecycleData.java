package com.group18.sustainucd;

import java.io.Serializable;

/** This class represents the data used by MainInfoActivity, SubInfoActivity and MaterialInfoActivity.
 *  It has the ID of the material name written in strings.xml and the ID of the material description
 *  written in strings.xml */
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
