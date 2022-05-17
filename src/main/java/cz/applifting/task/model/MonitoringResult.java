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

    public MonitoringResult() {
    }

    public LocalDateTime getDateOfCheck() {
        return dateOfCheck;
    }

    public void setDateOfCheck(LocalDateTime dateOfCheck) {
        this.dateOfCheck = dateOfCheck;
    }

    public Integer getReturnedHttpCode() {
        return returnedHttpCode;
    }

    public void setReturnedHttpCode(Integer returnedHttpCode) {
        this.returnedHttpCode = returnedHttpCode;
    }

    public String getReturnedPayload() {
        return returnedPayload;
    }

    public void setReturnedPayload(String returnedPayload) {
        this.returnedPayload = returnedPayload;
    }

    public MonitoredEndpoint getMonitoredEndpoint() {
        return monitoredEndpoint;
    }

    public void setMonitoredEndpoint(MonitoredEndpoint monitoredEndpoint) {
        this.monitoredEndpoint = monitoredEndpoint;
    }
}
