package model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 *  This class represents a firestation.
 *  A firestation is characterized by an address and a station number.
 *  All these parameters are required.
 */
@Data
public class Firestation {

    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String address;
    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String station;

}
