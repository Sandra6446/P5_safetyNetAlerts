package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.MyMap;
import com.openclassrooms.safetyNetAlerts.service.CollectDataService;
import com.openclassrooms.safetyNetAlerts.service.DataServiceForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FirestationRequestController.class)
public class FirestationRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FirestationRequestController firestationRequestController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<MyMap> maps;
    private static DataServiceForTest dataServiceForTest;

    @BeforeAll
    private static void setUp() {
        dataServiceForTest = new DataServiceForTest();
        maps = dataServiceForTest.buildMapsForTest();
    }

    @Test
    public void getDataPerFirestation() throws Exception {

        when(collectDataService.buildMyMaps()).thenReturn(maps);

        LinkedHashMap<String, Object> resident = new LinkedHashMap<>();
        resident.put("firstName", dataServiceForTest.getAdultMyMap2().getFirstName());
        resident.put("lastName", dataServiceForTest.getAdultMyMap2().getLastName());
        resident.put("phone", dataServiceForTest.getAdultMyMap2().getPhone());
        resident.put("age", dataServiceForTest.getAdultMyMap2().getAge());

        // Check filter
            mockMvc.perform(get("/firestation?stationNumber=2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].houses[0].residents", is(new ArrayList<>(Collections.singletonList(resident)))))
                    .andExpect(jsonPath("$[0].station").doesNotHaveJsonPath())
                    .andExpect(jsonPath("$[0].nbOfAdults",is(1)))
                    .andExpect(jsonPath("$[0].nbOfChildren",is(0)));

        // No station at this address;
            mockMvc.perform(get("/firestation?stationNumber=nowhere"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());

        // Empty address
        assertThrows(BadRequestException.class, () -> firestationRequestController.getDataPerFirestation(""));
    }
}