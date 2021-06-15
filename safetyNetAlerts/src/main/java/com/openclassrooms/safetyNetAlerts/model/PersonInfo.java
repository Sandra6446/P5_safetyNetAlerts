package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;
import model.MedicalRecord;
import model.Person;

@Data
public class Adult extends Person {

    private MedicalRecord medicalRecord;

}


