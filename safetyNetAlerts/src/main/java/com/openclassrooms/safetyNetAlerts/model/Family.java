package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a family with children.
 */

@EqualsAndHashCode(callSuper = true)
@Data
@JsonFilter("familyFilter")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Family extends Address {

    @JsonFilter("childrenFilter")
    private List<MyPerson> children;
    @JsonFilter("adultsFilter")
    private List<MyPerson> adults;

    public Family(String address, String city, int zip) {
        super(address, city, zip);
        this.children = new ArrayList<>();
        this.adults = new ArrayList<>();
    }

    public Family(String address, String city, int zip, List<MyPerson> children, List<MyPerson> adults) {
        super(address, city, zip);
        this.children = children;
        this.adults = adults;
    }

    public void addFamilyMember(MyPerson myPerson) {
        if (myPerson.getAge() < 18) {
            this.children.add(myPerson);
        } else {
            this.adults.add(myPerson);
        }
    }

}
