package model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class FireStation {

    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String address;
    @Length (min = 1, message = "Cette donnée est obligatoire")
    private String station;

}
