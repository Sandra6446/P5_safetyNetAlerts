package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.dao.FirestationDAO;
import com.openclassrooms.safetyNetAlerts.dao.PersonDAO;
import com.openclassrooms.safetyNetAlerts.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoverageByStationService {

    private static final FirestationDAO firestationDAO = new FirestationDAO();
    private static final PersonDAO personDAO = new PersonDAO();

    public static class CoverageByStationBuilder {

        private String station;

        public CoverageByStationBuilder withStation(String station) {
            this.station = station;
            return this;
        }

        public FirestationCoverage build() {

            List<String> addresses = firestationDAO.getByStation(station).stream().map(Firestation::getAddress).collect(Collectors.toList());
            FirestationCoverage firestationCoverage = new FirestationCoverage();
            List<Person> people = new ArrayList<>();

            if (!addresses.isEmpty()) {

                for (String address : addresses) {
                    people.addAll(personDAO.getByAddress(address));
                }

                List<PersonInfo> personInfos = new ArrayList<>();

                for (Person person : people) {
                    personInfos.add(new PersonInfoService.PersonInfoBuilder().withPerson(person).build());
                }

                firestationCoverage.setPersonInfos(personInfos);

                firestationCoverage.setNbOfAdult((int) personInfos.stream().filter(personInfo -> personInfo.getAge() < 18).count());
                firestationCoverage.setNbOfChild((int) personInfos.stream().filter(personInfo -> personInfo.getAge() >= 18).count());

            }

            return firestationCoverage;
        }

    }
}
