package model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

@Data
@JsonFilter("personInfo")
public class PersonInfo {

    @JsonFilter("personInPersonInfo")
    private Person person;
    private MedicalRecord medicalRecord;

}
