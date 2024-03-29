package org.elyte.booking;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;




@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cart implements Serializable{
    @Serial
    private static final long serialVersionUID = 1234567L;

    private Set<CreateProductRequest> itemsList;

    private int itemsQuantity;
    
}
