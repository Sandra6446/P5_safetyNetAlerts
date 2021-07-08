package com.openclassrooms.safetyNetAlerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.safetyNetAlerts.dao.PersonsDAO;
import com.openclassrooms.safetyNetAlerts.exceptions.AlreadyInDataFileException;
import com.openclassrooms.safetyNetAlerts.exceptions.NotFoundInDataFileException;
import com.openclassrooms.safetyNetAlerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
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

@WebMvcTest(controllers = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonsDAO personsDAO;

    private Person personForTest;

    @BeforeEach
    private void setUpPerTest() {
        personForTest = new Person("John", "Boyd", "1509 Culver St", "Culver", 97451, "841-874-6513", "jaboyd@email.com");
    }

    @Test
    public void testadd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        Mockito.when(personsDAO.save(ArgumentMatchers.any(Person.class))).thenReturn(personForTest);
        mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.when(personsDAO.save(ArgumentMatchers.any(Person.class))).thenThrow(AlreadyInDataFileException.class);
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

        Mockito.when(personsDAO.update(ArgumentMatchers.any(Person.class))).thenReturn(personForTest);
        mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.when(personsDAO.update(ArgumentMatchers.any(Person.class))).thenThrow(NotFoundInDataFileException.class);
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

        Mockito.when(personsDAO.remove(ArgumentMatchers.any(Person.class))).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.when(personsDAO.remove(ArgumentMatchers.any(Person.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
