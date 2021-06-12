package com.openclassrooms.safetyNetAlerts.model;

import lombok.Data;
import model.PersonInfo;

import java.util.List;

@Data
public class DataPerStation {

    private List<PersonInfo> personInfos;
    private int nbOfAdult = 0;
    private int nbOfChild = 0;

    public void addAnAdult() {
        this.nbOfAdult+=1;
    }

    public void addAChild() {
        this.nbOfChild+=1;
    }
}
