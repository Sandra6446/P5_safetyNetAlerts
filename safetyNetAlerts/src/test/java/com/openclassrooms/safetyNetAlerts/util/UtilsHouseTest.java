package com.openclassrooms.safetyNetAlerts.util;

import com.openclassrooms.safetyNetAlerts.model.Contact;
import com.openclassrooms.safetyNetAlerts.model.Firestation;
import com.openclassrooms.safetyNetAlerts.model.House;
import com.openclassrooms.safetyNetAlerts.model.MyMap;
import com.openclassrooms.safetyNetAlerts.service.DataServiceForTest;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsHouseTest {

    private final DataServiceForTest dataServiceForTest = new DataServiceForTest();
    private final List<House> houses = dataServiceForTest.buildHousesForTest();

    @Test
    void putHouseInMap() {
        List<MyMap> maps = new ArrayList<>();

        UtilsHouse.putHouseInMap(maps, new Firestation(dataServiceForTest.getAddressMyMap1().getStreet(), "1"), houses);
        UtilsHouse.putHouseInMap(maps, new Firestation(dataServiceForTest.getAddressMyMap2().getStreet(), "1"), houses);
        assertEquals(dataServiceForTest.buildMapsForTest().get(0), maps.get(0));

        UtilsHouse.putHouseInMap(maps, new Firestation(dataServiceForTest.getAddressMyMap2().getStreet(), "2"), houses);
        assertEquals(dataServiceForTest.buildMapsForTest(), maps);
    }

    @Test
    void putDetailsInContact() {
        Contact contact = new Contact();
        Set<String> phones = new HashSet<>(Arrays.asList("841-874-6513", "841-874-6544"));

        UtilsHouse.putDetailsInContact(houses, contact);
        assertEquals(phones, contact.getPhoneNumbers());
    }
}