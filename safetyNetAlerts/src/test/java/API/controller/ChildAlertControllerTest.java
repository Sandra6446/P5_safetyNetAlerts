package API.controller;

import API.exceptions.BadRequestException;
import API.model.Family;
import API.service.CollectDataService;
import API.util.UtilsForTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChildAlertController.class)
public class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChildAlertController childAlertController;

    @MockBean
    private CollectDataService collectDataService;

    private static List<Family> families;

    @BeforeAll
    private static void setUp() {
        // A list of two Family, @see familiesForTest
        families = UtilsForTest.familiesForTest();
    }

    @Test
    public void getChildInfos() throws Exception {
        when(collectDataService.buildFamilies()).thenReturn(families);


        // Family with child, filter check
        LinkedHashMap<String, Serializable> child = new LinkedHashMap<>();
        child.put("firstName", families.get(0).getChildren().get(0).getFirstName());
        child.put("lastName", families.get(0).getChildren().get(0).getLastName());
        child.put("age", families.get(0).getChildren().get(0).getAge());

        LinkedHashMap<String, Serializable> adult = new LinkedHashMap<>();
        adult.put("firstName", families.get(0).getAdults().get(0).getFirstName());
        adult.put("lastName", families.get(0).getAdults().get(0).getLastName());

        mockMvc.perform(get("/childAlert?address=" + families.get(0).getStreet()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].children", is(new ArrayList<>(Collections.singletonList(child)))))
                .andExpect(jsonPath("$[0].adults", is(new ArrayList<>(Collections.singletonList(adult)))));

        // Family without child
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