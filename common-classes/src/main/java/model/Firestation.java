package model;

import lombok.Data;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *  This class represents a firestation.
 *  A firestation is characterized by an address and a station number.
 *  All these parameters are required.
 */
@Data
public class Firestation {

    @NotNull
    @Size(min = 1, message = "Cette donnée est obligatoire")
    private String address;
    @NotNull
    @Size (min = 1, message = "Cette donnée est obligatoire")
    private String station;

}
