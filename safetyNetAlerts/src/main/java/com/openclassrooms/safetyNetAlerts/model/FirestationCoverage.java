package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class FirestationCoverage {

    @JsonFilter(value = "personInfoFilter")
    List<PersonInfo> personInfos;
    int nbOfAdult;
    int nbOfChild;
}
