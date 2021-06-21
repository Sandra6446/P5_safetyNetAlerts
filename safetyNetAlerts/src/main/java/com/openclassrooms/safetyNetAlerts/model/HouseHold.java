
package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@JsonFilter(value = "HouseHoldFilter")
public class HouseHold extends Address {

    @JsonFilter(value = "personInfoFilter")
    private List<PersonInfo> houseHoldMembers;

    public void addHouseHoldMembers (List<PersonInfo> personInfos) {
        this.houseHoldMembers.addAll(personInfos);
    }

    public HouseHold(String address, String city, int zip, List<PersonInfo> houseHoldMembers) {
        super(address, city, zip);
        this.houseHoldMembers = houseHoldMembers;
    }

    public HouseHold() {
        this.houseHoldMembers = new ArrayList<>();
    }
}

