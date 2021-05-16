package com.safetynetalert.microserviceperson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynetalert.microserviceperson.dao.PersonDAO;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
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

    private static Person person;

    @BeforeEach
    private void setUpPerTest() {
        person = new Person();
        person.setFirstName("FirstNameTest");
        person.setLastName("LastNameTest");
        person.setAddress("AddressTest");
        person.setCity("CityTest");
        person.setZip(64300);
        person.setPhone("123-456-7890");
        person.setEmail("test@email.com");
    }

    @Test
    public void testadd() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(person);
        when(personDAO.save(any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdate() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(person);
        when(personDAO.update(any(Person.class))).thenReturn(person);

        mockMvc.perform(put("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String personAsString = objectMapper.writeValueAsString(person);

        when(personDAO.remove(any(Person.class))).thenReturn(true);
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isOk());

        when(personDAO.remove(any(Person.class))).thenReturn(false);
        mockMvc.perform(delete("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(personAsString))
                .andExpect(status().isPreconditionFailed());
    }

}
