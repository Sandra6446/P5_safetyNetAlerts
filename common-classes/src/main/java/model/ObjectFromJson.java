package model;

import lombok.Data;

import java.util.List;


@Data
public class ObjectFromJson {

    private List<Person> persons;
    private List<FireStation> firestations;
    private List<MedicalRecords> medicalrecords;

}

