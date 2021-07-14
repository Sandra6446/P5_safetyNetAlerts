package API.util;

import API.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsHouseTest {

    private final List<House> houses = UtilsForTest.housesForTest();

    @Test
    void putHouseInMap() {
        List<MyMap> maps = new ArrayList<>();

        UtilsHouse.putHouseInMyMap(maps, new Firestation(houses.get(0).getStreet(), "1"), houses);
        UtilsHouse.putHouseInMyMap(maps, new Firestation(houses.get(1).getStreet(), "1"), houses);
        Assertions.assertEquals(UtilsForTest.myMapsForTest().get(0).getHouses(), maps.get(0).getHouses());

    }

    @Test
    void putDetailsInContact() {
        Contact contact = new Contact();
        Set<String> phones = UtilsForTest.myPersonsForTest().stream().map(MyPerson::getPhone).collect(Collectors.toSet());

        UtilsHouse.putDetailsInContact(houses, contact);
        assertEquals(phones, contact.getPhoneNumbers());
    }
}