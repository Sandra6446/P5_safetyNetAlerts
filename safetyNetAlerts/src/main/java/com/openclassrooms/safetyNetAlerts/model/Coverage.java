package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import java.util.List;

@Data
@JsonFilter(value = "areaFilter")
public class Area extends HouseHold {

    private List<Firestation> firestations;

    public void setPersonInfo(PersonInfo personInfo) {
        this.personInfos.add(personInfo);
    }

}