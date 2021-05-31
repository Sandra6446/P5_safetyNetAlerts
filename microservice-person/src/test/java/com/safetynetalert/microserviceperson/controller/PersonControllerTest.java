package com.safetynetalert.microserviceperson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.microserviceperson.controller.PersonController;
import dao.PersonDAO;
import exceptions.AlreadyInDataFileException;
import exceptions.NotFoundInDataFileException;
import model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import util.JsonMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        personForTest.setAddress("Mon quartier");
        personForTest.setZip(64000);
        personForTest.setCity("Ma ville");
        personForTest.setPhone("123-456-7890");
        personForTest.setEmail("email@email.com");
    }

    @Test
    public void testadd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        when(personDAO.save(any(Person.class))).thenReturn(personForTest);
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isCreated());

        when(personDAO.save(any(Person.class))).thenThrow(AlreadyInDataFileException.class);
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isConflict());

        Person person = new Person();
        personAsString = objectMapper.writeValueAsString(person);
        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdate() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        when(personDAO.update(any(Person.class))).thenReturn(personForTest);
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isOk());

        when(personDAO.update(any(Person.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isNotFound());

        Person person = new Person();
        personAsString = objectMapper.writeValueAsString(person);
        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(personForTest);

        when(personDAO.remove(any(Person.class))).thenReturn(true);
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isOk());

        when(personDAO.remove(any(Person.class))).thenThrow(NotFoundInDataFileException.class);
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isNotFound());
    }

}
