package API.util;

import API.model.MyPerson;
import API.model.Address;
import API.model.Family;
import API.model.House;

import java.util.List;

/**
 * Organises data related to a MyPerson
 *
 * @see MyPerson
 */
public class UtilsMyPerson {

    /**
     * Places a MyPerson in a House
     *
     * @param houses   The list of House to be completed
     * @param myPerson The MyPerson to be placed in a House
     * @param address  The MyPerson's Address
     */
    public static void putMyPersonInAHouse(List<House> houses, MyPerson myPerson, Address address) {

        boolean houseExists = false;

        for (House house : houses) {
            if (house.getStreet().equals(address.getStreet()) & house.getZip() == address.getZip() & house.getCity().equals(address.getCity())) {
                house.addResident(myPerson);
                houseExists = true;
            }
        }

        if (!houseExists) {
            House house = new House(address.getStreet(), address.getCity(), address.getZip());
            house.addResident(myPerson);
            houses.add(house);
        }
    }

    /**
     * Places a MyPerson in a Family
     *
     * @param families The list of Family to be completed
     * @param myPerson The MyPerson to be placed in a House
     * @param address  The MyPerson's Address
     */
    public static void putMyPersonInAFamily(List<Family> families, MyPerson myPerson, Address address) {

        boolean familyExists = false;

        for (Family family : families) {
            if (family.getStreet().equals(address.getStreet()) & family.getZip() == address.getZip() & family.getCity().equals(address.getCity())) {
                family.addFamilyMember(myPerson);
                familyExists = true;
            }
        }

        if (!familyExists) {
            Family family = new Family(address.getStreet(), address.getCity(), address.getZip());
            family.addFamilyMember(myPerson);
            families.add(family);
        }
    }
}
