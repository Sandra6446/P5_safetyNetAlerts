package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the list of all the peaple living in the same place.
 */

@EqualsAndHashCode(callSuper = true)
@Data
@JsonFilter("houseFilter")
public class House extends Address {

    private List<MyPerson> residents;

    public House(String address, String city, int zip) {
        super(address, city, zip);
        this.residents = new ArrayList<>();
    }

    public House(String address, String city, int zip, List<MyPerson> residents) {
        super(address, city, zip);
        this.residents = residents;
    }

    public void addResident(MyPerson myPerson) {
        this.residents.add(myPerson);
    }

}
