package API.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a MedicalRecord from json data file.
 * A MedicalRecord is characterized by a firstName, a lastName, a birthdate, a list of medications and a list of allergies.
 * All these parameters are required.
 */
@Data
public class MedicalRecord {

    /**
     * The firstname of the Person to whom the MedicalRecord belongs
     */
    @NotNull
    @Size(min = 1, message = "firstName is required")
    @NotNull
    private String firstName;
    /**
     * The lastname of the Person to whom the MedicalRecord belongs
     */
    @NotNull
    @Size(min = 1, message = "lastName is required")
    private String lastName;
    /**
     * The birthdate of the Person to whom the MedicalRecord belongs
     */
    @NotNull
    @Size(min = 10, message = "birthdate must be in the format XX/XX/XXXX")
    @Size(max = 10, message = "birthdate must be in the format XX/XX/XXXX")
    private String birthdate;
    /**
     * The list of medications of the Person to whom the MedicalRecord belongs
     */
    private List<String> medications;
    /**
     * The list of allergies of the Person to whom the MedicalRecord belongs
     */
    private List<String> allergies;

    public MedicalRecord(String firstName, String lastName, String birthdate, List<String> medications, List<String> allergies) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.medications = medications;
        this.allergies = allergies;
    }

    public MedicalRecord() {
        this.firstName = "";
        this.lastName = "";
        this.birthdate = "";
        this.medications = new ArrayList<>();
        this.allergies = new ArrayList<>();
    }
}
