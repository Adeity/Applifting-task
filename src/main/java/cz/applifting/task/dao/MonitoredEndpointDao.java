package cz.applifting.task.dao;

import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MonitoredEndpointDao extends JpaRepository<MonitoredEndpoint, Integer> {
    List<MonitoredEndpoint> findByName(String name);
    List<MonitoredEndpoint> findByOwner(User owner);

    Optional<MonitoredEndpoint> findByUrlAndOwner(String url, User owner);
}
