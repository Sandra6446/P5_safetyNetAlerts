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

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PhoneAlertController.class)
public class PhoneAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PhoneAlertController phoneAlertController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<MyMap> maps;

    @BeforeAll
    private static void setUp() {
        DataServiceForTest dataServiceForTest = new DataServiceForTest();
        maps = dataServiceForTest.buildMapsForTest();
    }

    @Test
    public void getPhoneNumbers() throws Exception {
        when(collectDataService.buildMaps()).thenReturn(maps);

        mockMvc.perform(get("/phoneAlert?firestation=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumbers[0]", is("841-874-6544")))
                .andExpect(jsonPath("$.emails").doesNotHaveJsonPath());

        // Nobody's covered by this firestation
        mockMvc.perform(get("/phoneAlert?firestation=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        // Empty city
        assertThrows(BadRequestException.class, () -> phoneAlertController.getPhoneNumbers(""));
    }
}