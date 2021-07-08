package com.openclassrooms.safetyNetAlerts.model;

import lombok.Data;

import java.util.List;

/**
 * This class represents the medical record of a person.
 */
@Data
public class MyMedicalRecord {

    private List<String> medications;
    private List<String> allergies;

    public MyMedicalRecord(MedicalRecord medicalRecord) {
        this.medications = medicalRecord.getMedications();
        this.allergies = medicalRecord.getAllergies();
    }

    public MyMedicalRecord(List<String> medications, List<String> allergies) {
        this.medications = medications;
        this.allergies = allergies;
    }
}
