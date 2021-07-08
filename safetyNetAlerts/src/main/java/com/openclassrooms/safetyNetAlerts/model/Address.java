package com.openclassrooms.safetyNetAlerts.model;

import lombok.Data;

/**
 * Represents an address
 */

@Data
public class Address {

    /**
     * The street of the address
     */
    private String street;
    /**
     * The city of the address
     */
    private String city;
    /**
     * The zip of the address
     */
    private int zip;

    public Address(String address, String city, int zip) {
        this.street = address;
        this.city = city;
        this.zip = zip;
    }
}
