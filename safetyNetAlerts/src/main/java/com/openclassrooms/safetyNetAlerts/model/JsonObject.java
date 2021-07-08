package com.openclassrooms.safetyNetAlerts.model;

import lombok.Data;

import java.util.List;

/**
 * This class groups all the data of Json data file : a list of persons, a list of firestations and a list of medical records.
 */
@Data
public class JsonObject {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;

}

