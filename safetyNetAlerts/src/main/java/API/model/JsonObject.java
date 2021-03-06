package API.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Groups all the data of json data file : a list of Person, a list of Firestation and a list of MedicalRecord.
 */
@Data
public class JsonObject {

    /**
     * The list of Person from json data file
     */
    private List<Person> persons;
    /**
     * The list of Firestation from json data file
     */
    private List<Firestation> firestations;
    /**
     * The list of MedicalRecord from json data file
     */
    private List<MedicalRecord> medicalrecords;

    public JsonObject() {
        this.persons = new ArrayList<>();
        this.firestations = new ArrayList<>();
        this.medicalrecords = new ArrayList<>();
    }

}

