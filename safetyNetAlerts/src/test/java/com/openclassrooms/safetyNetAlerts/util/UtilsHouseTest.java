package com.openclassrooms.safetyNetAlerts.util;

import com.openclassrooms.safetyNetAlerts.model.*;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsHouseTest {

    private final List<House> houses = UtilsForTest.housesForTest();

    @Test
    void putHouseInMap() {
        List<MyMap> maps = new ArrayList<>();

        UtilsHouse.putHouseInMap(maps, new Firestation(houses.get(0).getStreet(), "1"), houses);
        UtilsHouse.putHouseInMap(maps, new Firestation(houses.get(1).getStreet(), "1"), houses);
        assertEquals(UtilsForTest.mapsForTest().get(0), maps.get(0));

        UtilsHouse.putHouseInMap(maps, new Firestation(houses.get(1).getStreet(), "2"), houses);
        assertEquals(UtilsForTest.mapsForTest(), maps);
    }

    @Test
    void putDetailsInContact() {
        Contact contact = new Contact();
        Set<String> phones = UtilsForTest.myPersonsForTest().stream().map(MyPerson::getPhone).collect(Collectors.toSet());

        UtilsHouse.putDetailsInContact(houses, contact);
        assertEquals(phones, contact.getPhoneNumbers());
    }
}