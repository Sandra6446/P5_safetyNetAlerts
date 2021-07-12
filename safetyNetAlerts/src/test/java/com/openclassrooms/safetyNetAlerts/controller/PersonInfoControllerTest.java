package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.House;
import com.openclassrooms.safetyNetAlerts.model.MyPerson;
import com.openclassrooms.safetyNetAlerts.service.CollectDataService;
import com.openclassrooms.safetyNetAlerts.util.UtilsForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PersonInfoController.class)
public class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonInfoController personInfoController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<House> houses;

    @BeforeAll
    private static void setUp() {
        houses = UtilsForTest.housesForTest();
    }

    @Test
    public void getPersonInfos() throws Exception {

        when(collectDataService.buildHouses()).thenReturn(houses);

        LinkedHashMap<String, List> myMedicalRecord = new LinkedHashMap<>();
        MyPerson myPerson = houses.get(0).getResidents().get(0);
        myMedicalRecord.put("medications", myPerson.getMyMedicalRecord().getMedications());
        myMedicalRecord.put("allergies", myPerson.getMyMedicalRecord().getAllergies());

        LinkedHashMap<String, Object> child = new LinkedHashMap<>();
        child.put("lastName", myPerson.getLastName());
        child.put("email", myPerson.getEmail());
        child.put("age", myPerson.getAge());
        child.put("myMedicalRecord",myMedicalRecord);

        // Check filter
            mockMvc.perform(get("/personInfo?firstName=" + myPerson.getFirstName() + "&lastName=" + myPerson.getLastName()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].residents[0]",is(child)))
                    .andExpect(jsonPath("$[0].street",is(houses.get(0).getStreet())))
                    .andExpect(jsonPath("$[0].city").doesNotHaveJsonPath())
                    .andExpect(jsonPath("$[0].zip").doesNotHaveJsonPath());

        // Nobody has this name
            mockMvc.perform(get("/personInfo?firstName=John&lastName=Doe"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());

        // Empty address
        assertThrows(BadRequestException.class, () -> personInfoController.getPersonInfos("","Doe"));
        assertThrows(BadRequestException.class, () -> personInfoController.getPersonInfos("John",""));
    }
}