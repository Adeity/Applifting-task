package cz.applifting.task.service;

import cz.applifting.task.dao.MonitoredEndpointDao;
import cz.applifting.task.dao.MonitoringResultDao;
import cz.applifting.task.exceptions.MonitoringResultException;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.MonitoringResult;
import cz.applifting.task.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MonitoringResultService {
    @PersistenceContext
    private EntityManager entityManager;
    private final MonitoringResultDao dao;
    private final MonitoredEndpointDao monitoredEndpointDao;
    private final MonitoredEndpointService monitoredEndpointService;
    private final RequestSenderService requestSenderService;

    @Autowired
    public MonitoringResultService(MonitoringResultDao dao, MonitoredEndpointDao monitoredEndpointDao, MonitoredEndpointService monitoredEndpointService, RequestSenderService requestSenderService) {
        this.dao = dao;
        this.monitoredEndpointDao = monitoredEndpointDao;
        this.monitoredEndpointService = monitoredEndpointService;
        this.requestSenderService = requestSenderService;
    }


    /**
     * For each MonitoredEndpoint owned by user get last n entries of MonitoringResults
     * @param owner - user
     * @param limit - last n entries for each MonitoredEndpoint
     * @return list of Monitoring Result
     */
    public List<MonitoringResult> getLastNForEachURL(User owner, int limit) {
        //  Note: there might exist a single SQL-query way to do this which I am not aware of
        //  first get all user's MonitoredEndpoints
        List<MonitoredEndpoint> usersMonitoringEndpoints = monitoredEndpointDao.findByOwner(owner);
        //  for each MonitoredEndpoint append top n MonitoredResult
        List<MonitoringResult> result = new ArrayList<>();
        for (MonitoredEndpoint e : usersMonitoringEndpoints) {
            Query q = entityManager.createQuery("SELECT m FROM MonitoringResult m WHERE m.monitoredEndpoint = :monitoredEndpoint ORDER BY m.dateOfCheck", MonitoringResult.class).setMaxResults(limit);
            q.setParameter("monitoredEndpoint", e);
            List<MonitoringResult> res = q.getResultList();
            result.addAll(res);
        }
        return result;
    }

    /**
     * This method checks on a MonitoredEndpoint and creates a MonitoringResult based on result of HTTP request to said endpoint.
     * @param url - url of MonitoredEndpoint
     * @param owner - owner of MonitoredEndpoint
     * @return  created MonitoringResult
     */
    public MonitoringResult checkEndpoint(String url, User owner) {
        MonitoredEndpoint monitoredEndpoint = monitoredEndpointService.getMonitoredEndpointByUrlAndOwner(url, owner);
        if (!monitoredEndpoint.isActive()) {
            throw MonitoringResultException.create(monitoredEndpoint.getUrl(), owner.getUsername());
        }
        MonitoringResult monitoringResult = requestSenderService.sendRequest(monitoredEndpoint);
        monitoredEndpoint.setDateOfLastCheck(LocalDateTime.now());
        monitoredEndpointDao.save(monitoredEndpoint);
        dao.save(monitoringResult);
        return monitoringResult;
    }

}
