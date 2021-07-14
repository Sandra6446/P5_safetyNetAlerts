package API.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents a firestation in json file.
 * A firestation is characterized by an address and a station number.
 * All these parameters are required.
 */
@Data
public class Firestation {

    /**
     * The firestation's street
     */
    @NotNull
    @Size(min = 1, message = "address is required")
    @Size(max = 500)
    private String address;
    /**
     * The firestation's number
     */
    @NotNull
    @Size(min = 1, message = "station is required")
    @Size(max = 5)
    private String station;

    public Firestation(String address, String station) {
        this.address = address;
        this.station = station;
    }

    public Firestation() {
        this.address = "";
        this.station = "";
    }

}
