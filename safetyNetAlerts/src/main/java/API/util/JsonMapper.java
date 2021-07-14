package API.util;

import API.model.JsonObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Reads and writes in Json data file.
 */
@Configuration
@Component
public class JsonMapper {

    /**
     * Data file path.
     */
    @Value("${jsonPath}")
    private String path;

    private static final Logger logger = LogManager.getLogger(JsonObject.class);

    /**
     * Reads in json data file
     *
     * @return Returns a JsonObject if the reading succeed.
     */
    public JsonObject readJson() {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonObject jsonObject = new JsonObject();

        try {
            //convert json string to object
            jsonObject = objectMapper.readValue(new File(path), JsonObject.class);
            logger.debug("Json file correctly read.");
        } catch (IOException e) {
            logger.error("Error while JSON file reading.");
        }

        return jsonObject;
    }

    /**
     * Writes in data file
     *
     * @param jsonObject The JsonObject to be added in data file.
     */
    public void writeJson(JsonObject jsonObject) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //convert Object to json string
            objectMapper.writeValue(new File(path), jsonObject);
            logger.debug("Json file correctly written.");
        } catch (IOException e) {
            logger.error("Error while JSON file writing.");
        }

    }

}

