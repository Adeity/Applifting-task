package cz.applifting.task.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class MonitoringResult extends AbstractEntity{
    private LocalDateTime dateOfCheck;
    private Integer returnedHttpCode;
    private String returnedPayload;
    @ManyToOne
    private MonitoredEndpoint monitoredEndpoint;
}
