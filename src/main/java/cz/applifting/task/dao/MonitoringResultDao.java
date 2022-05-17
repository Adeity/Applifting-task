package cz.applifting.task.dao;

import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.MonitoringResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonitoringResultDao extends JpaRepository<MonitoringResult, Integer> {
}
