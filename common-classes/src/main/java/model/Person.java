package model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;

/**
 *  This class represents a person.
 *  A person is characterized by a firstname, a lastname, an address, a city, a zip, a phone and an email.
 *  All these parameters are required.
 */
@Data
public class Person {

    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String firstName;
    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String lastName;
    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String address;
    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String city;
    @Min(value = 10000, message = "Le code postal doit comporter 5 chifres")
    private int zip;
    @Length(min = 12)
    private String phone;
    @Email
    private String email;

}


