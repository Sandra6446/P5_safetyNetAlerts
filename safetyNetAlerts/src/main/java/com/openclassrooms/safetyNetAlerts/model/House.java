package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the list of all the people living in the same place, without distinction between adults and children.
 *
 * @see Family
 */

@EqualsAndHashCode(callSuper = true)
@Data
@JsonFilter("houseFilter")
public class House extends Address {

    /**
     * A list of all the house's residents
     */
    private List<MyPerson> residents;

    public House(String address, String city, int zip) {
        super(address, city, zip);
        this.residents = new ArrayList<>();
    }

    public House(String address, String city, int zip, List<MyPerson> residents) {
        super(address, city, zip);
        this.residents = residents;
    }

    /**
     * Adds a house's member
     *
     * @param myPerson The MyPerson to be added
     */
    public void addResident(MyPerson myPerson) {
        this.residents.add(myPerson);
    }

}
