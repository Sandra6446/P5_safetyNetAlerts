package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.dao.FirestationDAO;
import com.openclassrooms.safetyNetAlerts.model.CoverageByAddress;

public class CoverageByAddressService {

    private static final FirestationDAO firestationDAO = new FirestationDAO();

    public static class CoverageByAddressBuilder {

        private String address;

        public CoverageByAddressBuilder withAddress(String address) {
            this.address = address;
            return this;
        }

        public CoverageByAddress build() {
            CoverageByAddress coverageByAddress = new CoverageByAddress(new HouseHoldService.HouseHoldBuilder().withAddress(address).build());
            coverageByAddress.setFirestations(firestationDAO.getByAddress(address));
            return coverageByAddress;
        }

    }
}
