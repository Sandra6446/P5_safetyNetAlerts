package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class PersonInfo extends Person {

    @JsonFilter(value = "medicalRecordFilter")
    private MedicalRecord medicalRecord;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    int age = 0;

    public void hideAge() {
        this.age = 0;
    }

}


