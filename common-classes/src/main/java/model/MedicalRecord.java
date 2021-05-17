package model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Past;
import java.util.List;

/**
 *  This class represents the medical record of a person.
 *  The medical record is characterized by a first name, a last name, a birthdate, a list of medications and a list of allergies.
 *
 */
@Data
public class MedicalRecord {

    @Length(min = 1, message = "Cette donnée est obligatoire")
    private String firstName;
    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String lastName;
    @Past
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;
}
