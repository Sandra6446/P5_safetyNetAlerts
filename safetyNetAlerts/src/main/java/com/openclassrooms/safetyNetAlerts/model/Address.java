package com.openclassrooms.safetyNetAlerts.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * This class represents an address.
 * An address is characterized by an address, a city and a zip.
 */

@Data
public class Address {

    private String address;
    private String city;
    private int zip;

    public Address(String address, String city, int zip) {
        this.address = address;
        this.city = city;
        this.zip = zip;
    }

    public Address() {
    }
}
