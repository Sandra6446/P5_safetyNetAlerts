package model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Past;
import java.util.List;

@Data
public class MedicalRecords {

    @Length(min = 1, message = "Cette donnée est obligatoire")
    private String firstName="";
    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String lastName="";
    @Past
    private String birthdate="";
    private List<String> medications=null;
    private List<String> allergies=null;
}
