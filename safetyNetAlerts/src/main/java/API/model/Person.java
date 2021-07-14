package API.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents a person in json data file.
 * A person is characterized by a firstname, a lastname, an address, a city, a zip, a phone and an email.
 * All these parameters are required.
 */

@Data
public class Person {

    /**
     * The Person's firstname
     */
    @NotNull
    @Size(min = 1, message = "firstName is required")
    private String firstName;
    /**
     * The Person's lastname
     */
    @NotNull
    @Size(min = 1, message = "lastName is required")
    private String lastName;
    /**
     * The Person's street
     */
    @NotNull
    @Size(min = 1, message = "address is required")
    private String address;
    /**
     * The Person's city
     */
    @NotNull
    @Size(min = 1, message = "city is required")
    private String city;
    /**
     * The Person's zip
     */
    @Min(value = 10000, message = "zip must have 5 digits")
    private int zip;
    /**
     * The Person's phone number
     */
    @NotNull
    @Size(min = 12, message = "phone must be in the format xxx-xxx-xxxx")
    private String phone;
    /**
     * The Person's email address
     */
    @NotNull
    @Email
    private String email;

    public Person(String firstName, String lastName, String address, String city, int zip, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
    }

    public Person() {
        this.firstName = "";
        this.lastName = "";
        this.address = "";
        this.city = "";
        this.zip = 0;
        this.phone = "";
        this.email = "";
    }
}


