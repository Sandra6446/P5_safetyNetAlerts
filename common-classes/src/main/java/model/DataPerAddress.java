package model;

import lombok.Data;

import java.util.List;

@Data
public class DataPerAddress {

    private List<Firestation> firestations;
    private List<Person> persons;

}