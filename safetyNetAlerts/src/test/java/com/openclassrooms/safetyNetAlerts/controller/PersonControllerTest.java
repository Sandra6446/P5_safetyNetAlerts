package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.dao.PersonDAO;
import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import com.openclassrooms.safetyNetAlerts.model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.openclassrooms.safetyNetAlerts.util.JsonMapper;

import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JsonMapper jsonMapper;

    @MockBean
    private PersonDAO personDAO;

    private static Person personForTest;

    @BeforeAll
    public static void setUp() {
        personForTest = new Person();
        personForTest.setFirstName("Sandra");
        personForTest.setLastName("M");
        personForTest.setAddress("Mon adresse");
        personForTest.setCity("Ma ville");
        personForTest.setZip(64000);
        personForTest.setPhone("123-456-7890");
        personForTest.setEmail("email@email.com");
    }

    @Test
    public void testadd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        Mockito.when(personDAO.save(ArgumentMatchers.any(Person.class))).thenReturn(personForTest);
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.when(personDAO.save(ArgumentMatchers.any(Person.class))).thenThrow(AlreadyInDataFileException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isConflict());

        Person person = new Person();
        personAsString = objectMapper.writeValueAsString(person);
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        Mockito.when(personDAO.update(ArgumentMatchers.any(Person.class))).thenReturn(personForTest);
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.when(personDAO.update(ArgumentMatchers.any(Person.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Person person = new Person();
        personAsString = objectMapper.writeValueAsString(person);
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        Mockito.when(personDAO.remove(ArgumentMatchers.any(Person.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.when(personDAO.remove(ArgumentMatchers.any(Person.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
