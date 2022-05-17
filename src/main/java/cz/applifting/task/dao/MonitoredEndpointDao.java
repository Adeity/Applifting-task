package cz.applifting.task.dao;

import cz.applifting.task.model.MonitoredEndpoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoredEndpointDao extends JpaRepository<MonitoredEndpoint, Integer> {
}
