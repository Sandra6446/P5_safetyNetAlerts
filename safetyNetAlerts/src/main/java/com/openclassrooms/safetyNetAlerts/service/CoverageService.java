package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.dao.FirestationDAO;
import com.openclassrooms.safetyNetAlerts.dao.PersonDAO;
import com.openclassrooms.safetyNetAlerts.model.Coverage;
import com.openclassrooms.safetyNetAlerts.model.Person;

public class AreaService {

    private static final PersonDAO personDAO = new PersonDAO();
    private static final FirestationDAO firestationDAO = new FirestationDAO();

    public static class AreaBuilder {

        private String address;

        public AreaBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public Coverage build() {
            Coverage area = new Coverage();
            area.setFirestations(firestationDAO.getByAddress(address));
            for (Person person : personDAO.getByAddress(address)) {
                    area.setPersonInfo(new PersonInfoService.PersonInfoBuilder().withFullName(person).build());
                }
            return area;
        }
    }
}
