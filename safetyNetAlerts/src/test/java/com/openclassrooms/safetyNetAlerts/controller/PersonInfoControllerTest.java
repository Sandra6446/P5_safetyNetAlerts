package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.House;
import com.openclassrooms.safetyNetAlerts.service.CollectDataService;
import com.openclassrooms.safetyNetAlerts.service.DataServiceForTest;
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
    private static DataServiceForTest dataServiceForTest;

    @BeforeAll
    private static void setUp() {
        dataServiceForTest = new DataServiceForTest();
        houses = dataServiceForTest.buildHousesForTest();
    }

    @Test
    public void getPersonInfos() throws Exception {

        when(collectDataService.buildHouses()).thenReturn(houses);

        LinkedHashMap<String, List> myMedicalRecord = new LinkedHashMap<>();
        myMedicalRecord.put("medications",dataServiceForTest.getChildMyMap1().getMyMedicalRecord().getMedications());
        myMedicalRecord.put("allergies",dataServiceForTest.getChildMyMap1().getMyMedicalRecord().getAllergies());

        LinkedHashMap<String, Object> child = new LinkedHashMap<>();
        child.put("lastName",dataServiceForTest.getChildMyMap1().getLastName());
        child.put("email",dataServiceForTest.getChildMyMap1().getEmail());
        child.put("age",dataServiceForTest.getChildMyMap1().getAge());
        child.put("myMedicalRecord",myMedicalRecord);

            mockMvc.perform(get("/personInfo?firstName=" + dataServiceForTest.getChildMyMap1().getFirstName() + "&lastName=" + dataServiceForTest.getChildMyMap1().getLastName()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].residents[0]",is(child)))
                    .andExpect(jsonPath("$[0].street",is(dataServiceForTest.getAddressMyMap1().getStreet())))
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