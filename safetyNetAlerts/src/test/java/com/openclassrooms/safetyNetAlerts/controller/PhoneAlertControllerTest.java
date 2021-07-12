package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.MyMap;
import com.openclassrooms.safetyNetAlerts.service.CollectDataService;
import com.openclassrooms.safetyNetAlerts.util.UtilsForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
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
        maps = UtilsForTest.mapsForTest();
    }

    @Test
    public void getPhoneNumbers() throws Exception {
        when(collectDataService.buildMyMaps()).thenReturn(maps);

        // Check filter
        mockMvc.perform(get("/phoneAlert?firestation=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumbers[0]", is(maps.get(0).getHouses().get(0).getResidents().get(0).getPhone())))
                .andExpect(jsonPath("$.emails").doesNotHaveJsonPath());

        // Nobody's covered by this firestation
        mockMvc.perform(get("/phoneAlert?firestation=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        // Empty city
        assertThrows(BadRequestException.class, () -> phoneAlertController.getPhoneNumbers(""));
    }
}