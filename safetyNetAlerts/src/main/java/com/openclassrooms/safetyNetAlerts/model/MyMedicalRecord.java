package com.openclassrooms.safetyNetAlerts.model;

import lombok.Data;

import java.util.List;

/**
 * Represents the medical record of a MyPerson
 *
 * @see MedicalRecord
 */
@Data
public class MyMedicalRecord {

    /**
     * The list of MyPerson's medications
     */
    private List<String> medications;
    /**
     * The list of MyPerson's allergies
     */
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
