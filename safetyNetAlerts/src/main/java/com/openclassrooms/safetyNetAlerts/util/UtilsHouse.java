package com.openclassrooms.safetyNetAlerts.util;

import com.openclassrooms.safetyNetAlerts.model.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Organises data related to Houses
 *
 * @see House
 */
public class UtilsHouse {

    /**
     * Places houses in myMaps
     *
     * @param myMaps      The list of myMaps to be completed
     * @param firestation The firestation for which we create the myMap
     * @param houses      The list of houses to be placed in myMaps
     */
    public static void putHouseInMap(List<MyMap> myMaps, Firestation firestation, List<House> houses) {

        List<House> myHouses = houses.stream().filter(house -> house.getStreet().equals(firestation.getAddress())).collect(Collectors.toList());
        boolean mapExists = false;

        for (MyMap myMap : myMaps) {
            if (myMap.getStation().equals(firestation.getStation())) {
                for (House house : myHouses) {
                    myMap.addChildren((int) house.getResidents().stream().filter(personne -> personne.getAge() < 18).count());
                    myMap.addAdults((int) house.getResidents().stream().filter(personne -> personne.getAge() >= 18).count());
                }
                myMap.addHouses(myHouses);
                mapExists = true;
            }
        }

        if (!mapExists) {
            MyMap myMap = new MyMap(firestation.getStation());
            for (House house : myHouses) {
                myMap.addChildren((int) house.getResidents().stream().filter(personne -> personne.getAge() < 18).count());
                myMap.addAdults((int) house.getResidents().stream().filter(personne -> personne.getAge() >= 18).count());
            }
            myMap.addHouses(myHouses);
            myMaps.add(myMap);
        }
    }

    /**
     * Retrieves phone numbers and emails from a house list
     *
     * @param houses  The list of houses containing the phone numbers and emails to be retrieved
     * @param contact The object that will contain the data
     */
    public static void putDetailsInContact(List<House> houses, Contact contact) {
        for (House house : houses) {
            for (MyPerson myPerson : house.getResidents()) {
                contact.addPhone(myPerson.getPhone());
                contact.addEmail(myPerson.getEmail());
            }
        }
    }
}
