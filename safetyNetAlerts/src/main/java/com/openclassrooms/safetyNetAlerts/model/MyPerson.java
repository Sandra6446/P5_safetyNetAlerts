package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

/**
 * This class represents a person.
 */

@Data
@JsonFilter("myPersonFilter")
public class MyPerson {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private int age;
    private MyMedicalRecord myMedicalRecord;

    public MyPerson() {
        this.firstName = "";
        this.lastName = "";
        this.phone = "";
        this.email = "";
        this.age = 0;
        this.myMedicalRecord = null;
    }

    public MyPerson(String firstName, String lastName, String phone, String email, int age, MyMedicalRecord myMedicalRecord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.age = age;
        this.myMedicalRecord = myMedicalRecord;
    }
}


