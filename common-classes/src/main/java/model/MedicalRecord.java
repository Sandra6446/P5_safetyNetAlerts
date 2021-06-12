package model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *  This class represents the medical record of a person.
 *  The medical record is characterized by a first name, a last name, a birthdate, a list of medications and a list of allergies.
 *
 */
@Data
public class MedicalRecord {

    @NotNull
    @Size(min = 1, message = "Cette donnée est obligatoire")
    @NotNull
    private String firstName;
    @NotNull
    @Size (min = 1, message = "Cette donnée est obligatoire")
    private String lastName;
    @NotNull
    @Size (min = 10, message = "Cette donnée doit être au format XX/XX/XXXX")
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

}
