package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonFilter(value = "coverageByAddressFilter")
public class CoverageByAddress extends HouseHold {

    @JsonFilter(value = "firestationsFilter")
    private List<Firestation> firestations;


    public CoverageByAddress(HouseHold houseHold) {
        super(houseHold.getAddress(),houseHold.getCity(),houseHold.getZip(),houseHold.getHouseHoldMembers());
        this.firestations = new ArrayList<>();
    }
}