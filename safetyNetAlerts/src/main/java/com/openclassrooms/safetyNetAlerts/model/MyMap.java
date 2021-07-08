package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents an address.
 */

@Data
@JsonFilter("myMapFilter")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MyMap extends MyFirestation {

    private List<House> houses;

    public MyMap(String station) {
        super(station);
        this.houses = new ArrayList<>();
    }

    public MyMap(String station, int nbOfAdults, int nbOfChildren, List<House> houses) {
        super(station, nbOfAdults, nbOfChildren);
        this.houses = houses;
    }

    public void addHouses(List<House> houses) {
        this.houses.addAll(houses);
    }

    public void filterByAddress(String address) {
        this.houses = houses.stream().filter(house -> house.getStreet().equals(address)).collect(Collectors.toList());
    }

}
