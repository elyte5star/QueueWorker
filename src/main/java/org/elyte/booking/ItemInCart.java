package org.elyte.booking;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemInCart implements Serializable{

    @Serial
    private static final long serialVersionUID = 1234567L;
    
    private String pid;
    private String name;
    private String image;
    private String details;
    private String description;
    private String category;
    private Double price;
    private Integer stockQuantity;
    private int quantity;
    private Double calculatedPrice;
    private String productDiscount=null;

    
}
