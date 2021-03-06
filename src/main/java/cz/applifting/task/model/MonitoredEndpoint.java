package cz.applifting.task.model;


import cz.applifting.task.dto.MonitoredEndpointDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class MonitoredEndpoint extends AbstractEntity{
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private LocalDateTime dateOfCreation;
    private LocalDateTime dateOfLastCheck;
    private Integer monitoringInterval;
    @ManyToOne
    private User owner;
    @Column(nullable = false)
    private HttpMethodEnum httpMethodEnum;
    private boolean active = true;

    public MonitoredEndpoint() {
    }

    public MonitoredEndpoint(MonitoredEndpointDto dto) {
        this.setUrl(dto.getUrl());
        this.setName(dto.getName());
        this.setHttpMethodEnum(dto.getHttpMethodEnum());
        this.setMonitoringInterval(dto.getMonitoredInterval());
        this.setDateOfCreation(LocalDateTime.now());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public LocalDateTime getDateOfLastCheck() {
        return dateOfLastCheck;
    }

    public void setDateOfLastCheck(LocalDateTime dateOfLastCheck) {
        this.dateOfLastCheck = dateOfLastCheck;
    }

    public Integer getMonitoringInterval() {
        return monitoringInterval;
    }

    public void setMonitoringInterval(Integer monitoringInterval) {
        this.monitoringInterval = monitoringInterval;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public HttpMethodEnum getHttpMethodEnum() {
        return httpMethodEnum;
    }

    public void setHttpMethodEnum(HttpMethodEnum httpMethodEnum) {
        this.httpMethodEnum = httpMethodEnum;
    }
}
