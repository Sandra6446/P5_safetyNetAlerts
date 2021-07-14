package API.model;

import lombok.Data;

/**
 * Represents a fire station.
 * @see Firestation
 */

@Data
public class MyFirestation {

    /**
     * The fire station number
     */
    private String station;
    /**
     * The number of adults covered by the fire station
     */
    private int nbOfAdults;
    /**
     * The number of children covered by the fire station
     */
    private int nbOfChildren;

    public MyFirestation(String station) {
        this.station = station;
        this.nbOfChildren = 0;
        this.nbOfAdults = 0;
    }

    public MyFirestation() {
        this.station = "";
        this.nbOfAdults = 0;
        this.nbOfChildren = 0;
    }

    public MyFirestation(String station, int nbOfAdults, int nbOfChildren) {
        this.station = station;
        this.nbOfAdults = nbOfAdults;
        this.nbOfChildren = nbOfChildren;
    }

    /**
     * Adds adults in MyFirestation
     * @param count The number of adults to be added
     */
    public void addAdults(int count) {
        this.nbOfAdults += count;
    }

    /**
     * Adds children in MyFirestation
     * @param count The number of children to be added
     */
    public void addChildren(int count) {
        this.nbOfChildren += count;
    }

}

