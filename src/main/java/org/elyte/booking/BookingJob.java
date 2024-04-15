package org.elyte.booking;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingJob {
    private String userid;
    private BigDecimal totalPrice;
    private List<ItemInCart> cart;
    private ShippingDetails shippingAddress;
    
}
