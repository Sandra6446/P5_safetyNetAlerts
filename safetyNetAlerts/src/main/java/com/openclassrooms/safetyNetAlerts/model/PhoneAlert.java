package com.openclassrooms.safetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhoneAlert {

    Set<String> phoneNumbers;

}
