package com.openclassrooms.safetyNetAlerts.util;

import com.openclassrooms.safetyNetAlerts.model.Family;
import com.openclassrooms.safetyNetAlerts.model.House;
import com.openclassrooms.safetyNetAlerts.model.MyPerson;
import com.openclassrooms.safetyNetAlerts.service.DataServiceForTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsMyPersonTest {

    private final DataServiceForTest dataServiceForTest = new DataServiceForTest();
    private final MyPerson child = dataServiceForTest.getChildMyMap1();
    private final MyPerson adult = dataServiceForTest.getAdultMyMap1();
    private final MyPerson adult2 = dataServiceForTest.getAdultMyMap2();

    @Test
    void putMyPersonInAHouse() {
        List<House> houses = new ArrayList<>();

        UtilsMyPerson.putMyPersonInAHouse(houses,child,dataServiceForTest.getAddressMyMap1());
        UtilsMyPerson.putMyPersonInAHouse(houses,adult,dataServiceForTest.getAddressMyMap1());
        assertEquals(dataServiceForTest.buildHousesForTest().get(0),houses.get(0));

        UtilsMyPerson.putMyPersonInAHouse(houses,adult2,dataServiceForTest.getAddressMyMap2());
        assertEquals(dataServiceForTest.buildHousesForTest(),houses);
    }

    @Test
    void putMyPersonInAFamily() {
        List<Family> families = new ArrayList<>();

        UtilsMyPerson.putMyPersonInAFamily(families,child,dataServiceForTest.getAddressMyMap1());
        UtilsMyPerson.putMyPersonInAFamily(families,adult,dataServiceForTest.getAddressMyMap1());
        assertEquals(dataServiceForTest.buildFamiliesForTest().get(0),families.get(0));

        UtilsMyPerson.putMyPersonInAFamily(families,adult2,dataServiceForTest.getAddressMyMap2());
        assertEquals(dataServiceForTest.buildFamiliesForTest(),families);
    }
}