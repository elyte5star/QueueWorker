package org.elyte.booking;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShippingDetails implements Serializable{

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String fullName;
    private String streetAddress;
    private String country;
    private String state;
    private String bemail;
    private String zip;

    
}
