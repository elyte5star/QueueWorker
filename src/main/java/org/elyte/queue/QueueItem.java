package org.elyte.queue;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.elyte.worker.Job;
import org.elyte.worker.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class QueueItem {

    @JsonProperty("Job")
    private Job job;

    @JsonProperty("Task")
    private Task task;
    
}
