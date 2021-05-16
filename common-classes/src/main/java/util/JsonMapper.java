package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.ObjectFromJson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsonMapper {

    @Value("${chemin_json}")
    private String chemin;

    private static final Logger logger = LogManager.getLogger(ObjectFromJson.class);

    public ObjectFromJson readJson() {

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectFromJson objectFromJson = new ObjectFromJson();

        try {
            //convert json string to object
            objectFromJson = objectMapper.readValue(new File(chemin), ObjectFromJson.class);
            if (objectFromJson==null) {
                logger.error("Error, nothing to read in data file.");
            }
            logger.info("Json file correctly read.");
        } catch (IOException e) {
            logger.error("Error while JSON file reading.");
        }
        return objectFromJson;
    }

    public void writeJson(ObjectFromJson objectFromJson) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //convert Object to json string
            objectMapper.writeValue(new File(chemin), objectFromJson);
            logger.info("Json file correctly written.");
        } catch (IOException e) {
            logger.error("Error while JSON file writing.");
        }

    }

}

