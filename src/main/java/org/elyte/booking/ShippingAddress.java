package org.elyte.booking;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShippingAddress implements Serializable{

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String billingFullName;
    private String bemail;
    private String baddress;
    private String bcountry;
    private String bzip;
    private String bcity;

    
}
