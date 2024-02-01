package org.elyte.worker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elyte.enums.Status;
import java.io.Serial;
import java.io.Serializable;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task implements Serializable{
    @Serial
    private static final long serialVersionUID = 1234567L;
    private String tid;
    private String created;
    private String startedAt;
    private String endedAt;
    private Status taskStatus;
    private String result;

}
