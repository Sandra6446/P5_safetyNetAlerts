package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

/**
 * Represents a person.
 *
 * @see Person
 */

@Data
@JsonFilter("myPersonFilter")
public class MyPerson {

    /**
     * The MyPerson's firstname
     */
    private String firstName;
    /**
     * The MyPerson's lastname
     */
    private String lastName;
    /**
     * The MyPerson's phone number
     */
    private String phone;
    /**
     * The MyPerson's email address
     */
    private String email;
    /**
     * The MyPerson's age
     */
    private int age;
    /**
     * The MyPerson's MyMedicalRecord
     */
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


