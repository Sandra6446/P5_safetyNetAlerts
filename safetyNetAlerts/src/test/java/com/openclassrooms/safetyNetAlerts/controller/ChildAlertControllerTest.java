package com.openclassrooms.safetyNetAlerts.controller;

import com.openclassrooms.safetyNetAlerts.exceptions.BadRequestException;
import com.openclassrooms.safetyNetAlerts.model.*;
import com.openclassrooms.safetyNetAlerts.service.CollectDataService;
import com.openclassrooms.safetyNetAlerts.service.DataServiceForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.Serializable;
import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = ChildAlertController.class)
public class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChildAlertController childAlertController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<Family> families;
    private static DataServiceForTest dataServiceForTest;

    @BeforeAll
    private static void setUp() {
        dataServiceForTest = new DataServiceForTest();
        families = dataServiceForTest.buildFamiliesForTest();
    }

    @Test
    public void getChildInfos() throws Exception {
        when(collectDataService.buildFamilies()).thenReturn(families);

        LinkedHashMap<String, Serializable> child = new LinkedHashMap<>();
        child.put("firstName",dataServiceForTest.getChildMyMap1().getFirstName());
        child.put("lastName",dataServiceForTest.getChildMyMap1().getLastName());
        child.put("age",dataServiceForTest.getChildMyMap1().getAge());

        LinkedHashMap<String, Serializable> adult = new LinkedHashMap<>();
        adult.put("firstName",dataServiceForTest.getAdultMyMap1().getFirstName());
        adult.put("lastName",dataServiceForTest.getAdultMyMap1().getLastName());

        // family with child, filter check
            mockMvc.perform(get("/childAlert?address=" + families.get(0).getStreet()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].children",is(new ArrayList<>(Collections.singletonList(child)))))
                    .andExpect(jsonPath("$[0].adults",is(new ArrayList<>(Collections.singletonList(adult)))));

        // family without child
        families.get(0).setChildren(new ArrayList<>());
            mockMvc.perform(get("/childAlert?address=" + families.get(1).getStreet()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());

        // No family at this address
            mockMvc.perform(get("/childAlert?address=nowhere"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());

        // Empty address
        assertThrows(BadRequestException.class, () -> childAlertController.getChildInfos(""));
    }
}