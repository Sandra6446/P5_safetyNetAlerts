package com.openclassrooms.safetyNetAlerts.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class represents a person.
 * A person is characterized by a firstname, a lastname, an address, a city, a zip, a phone and an email.
 * All these parameters are required.
 */

@Data
public class Person {

    @NotNull
    @Size(min = 1, message = "Cette donnée est obligatoire")
    private String firstName;
    @NotNull
    @Size(min = 1, message = "Cette donnée est obligatoire")
    private String lastName;
    @NotNull
    @Size(min = 1, message = "Cette donnée est obligatoire")
    private String address;
    @NotNull
    @Size(min = 1, message = "Cette donnée est obligatoire")
    private String city;
    @Min(value = 10000, message = "Le code postal doit comporter 5 chifres")
    private int zip;
    @NotNull
    @Size(min = 12)
    private String phone;
    @NotNull
    @Email
    private String email;

}


