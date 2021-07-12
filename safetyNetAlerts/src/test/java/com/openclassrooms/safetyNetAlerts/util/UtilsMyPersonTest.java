package com.openclassrooms.safetyNetAlerts.util;

import com.openclassrooms.safetyNetAlerts.model.Family;
import com.openclassrooms.safetyNetAlerts.model.House;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsMyPersonTest {

    @Test
    void putMyPersonInAHouse() {
        List<House> houses = new ArrayList<>();

        UtilsMyPerson.putMyPersonInAHouse(houses,UtilsForTest.myPersonsForTest().get(0), UtilsForTest.addressesForTest().get(0));
        UtilsMyPerson.putMyPersonInAHouse(houses,UtilsForTest.myPersonsForTest().get(1), UtilsForTest.addressesForTest().get(0));
        assertEquals(UtilsForTest.housesForTest().get(0),houses.get(0));

        UtilsMyPerson.putMyPersonInAHouse(houses,UtilsForTest.myPersonsForTest().get(2), UtilsForTest.addressesForTest().get(1));
        assertEquals(UtilsForTest.housesForTest(),houses);
    }

    @Test
    void putMyPersonInAFamily() {
        List<Family> families = new ArrayList<>();

        UtilsMyPerson.putMyPersonInAFamily(families,UtilsForTest.myPersonsForTest().get(0), UtilsForTest.addressesForTest().get(0));
        UtilsMyPerson.putMyPersonInAFamily(families,UtilsForTest.myPersonsForTest().get(1), UtilsForTest.addressesForTest().get(0));
        assertEquals(UtilsForTest.familiesForTest().get(0),families.get(0));

        UtilsMyPerson.putMyPersonInAFamily(families,UtilsForTest.myPersonsForTest().get(2), UtilsForTest.addressesForTest().get(1));
        assertEquals(UtilsForTest.familiesForTest(),families);
    }
}