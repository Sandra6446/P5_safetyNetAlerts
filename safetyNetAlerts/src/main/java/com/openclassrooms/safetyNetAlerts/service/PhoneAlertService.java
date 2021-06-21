package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.model.*;

import java.util.HashSet;
import java.util.Set;

public class PhoneAlertService {

    public static class PhoneAlertBuilder {

        private String station;

        public PhoneAlertBuilder withStation(String station) {
            this.station = station;
            return this;
        }

        public PhoneAlert build() {
            PhoneAlert phoneAlert = new PhoneAlert();
            FirestationCoverage firestationCoverage = new CoverageByStationService.CoverageByStationBuilder().withStation(station).build();
            if (!firestationCoverage.equals(new FirestationCoverage())) {
                Set<String> phoneNumbers = new HashSet<>();
                for (PersonInfo personInfo : firestationCoverage.getPersonInfos()) {
                    phoneNumbers.add(personInfo.getPhone());
                }
                phoneAlert.setPhoneNumbers(phoneNumbers);
            }
            return phoneAlert;
        }
    }
}
