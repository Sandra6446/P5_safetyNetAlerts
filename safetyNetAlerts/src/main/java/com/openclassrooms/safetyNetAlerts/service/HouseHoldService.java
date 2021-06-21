package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.dao.PersonDAO;
import com.openclassrooms.safetyNetAlerts.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HouseHoldService {

    private static final PersonDAO personDAO = new PersonDAO();

    public static class HouseHoldBuilder {

        private String address;
        private boolean onlyChild = false;

        public HouseHoldBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public HouseHoldBuilder onlyWithChild() {
            this.onlyChild = true;
            return this;
        }

        public HouseHold build() {
            HouseHold houseHold = new HouseHold();
            List<Person> persons = personDAO.getByAddress(address);

            houseHold.setAddress(address);
            /*
             houseHold.setCity(persons.get(0).getCity());
             houseHold.setZip(persons.get(0).getZip());
            */

            List<PersonInfo> children = new ArrayList<>();
            List<PersonInfo> adults = new ArrayList<>();
            for (Person person : persons) {
                PersonInfo personInfo = new PersonInfoService.PersonInfoBuilder().withPerson(person).build();
                if (personInfo.getAge()<18) {
                    children.add(personInfo);
                } else {
                    adults.add(personInfo);
                }
            }

            if (onlyChild) {
                if (!children.isEmpty()) {
                    houseHold.addHouseHoldMembers(children);
                    houseHold.addHouseHoldMembers(adults.stream().peek(personInfo -> personInfo.setAge(0)).collect(Collectors.toList()));
                }
            } else {
                houseHold.addHouseHoldMembers(children);
                houseHold.addHouseHoldMembers(adults);
            }

            return houseHold;
        }
    }
}

