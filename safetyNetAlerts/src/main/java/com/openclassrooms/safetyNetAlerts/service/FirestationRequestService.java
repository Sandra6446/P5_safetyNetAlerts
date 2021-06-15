package com.openclassrooms.safetyNetAlerts.service;

import com.openclassrooms.safetyNetAlerts.model.DataPerStation;
import dao.FirestationDAO;
import com.openclassrooms.safetyNetAlerts.model.Area;
import model.Firestation;
import com.openclassrooms.safetyNetAlerts.model.PersonInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataPerStationService {

    private static final FirestationDAO firestationDAO = new FirestationDAO();

    public static class DataPerStationBuilder {

        private String station;

        public DataPerStationBuilder withStation(String station) {
            this.station = station;
            return this;
        }

        public DataPerStation build() {
            DataPerStation dataPerStation = new DataPerStation();

            List<String> addresses = firestationDAO.getByStation(station)
                    .stream()
                    .map(Firestation::getAddress)
                    .collect(Collectors.toList());

            for (String address : addresses) {
                Area area = new AreaService.AreaBuilder().withAddress(address).build();
                dataPerStation.setArea(area);
                for (PersonInfo personInfo : area.getPersonInfos()) {
                    if (GetAge.getAge(personInfo.getMedicalRecord().getBirthdate()) < 18) {
                        dataPerStation.addAChild();
                    } else {
                        dataPerStation.addAnAdult();
                    }
                }
            }
            return dataPerStation;
        }
    }
}

