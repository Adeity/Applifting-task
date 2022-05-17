package cz.applifting.task.service;

import cz.applifting.task.dao.MonitoredEndpointDao;
import cz.applifting.task.dao.MonitoringResultDao;
import cz.applifting.task.exceptions.MonitoringResultException;
import cz.applifting.task.model.HttpMethodEnum;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.MonitoringResult;
import cz.applifting.task.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

    @Autowired
    public MonitoringResultService(MonitoringResultDao dao, MonitoredEndpointDao monitoredEndpointDao, MonitoredEndpointService monitoredEndpointService) {
        this.dao = dao;
        this.monitoredEndpointDao = monitoredEndpointDao;
        this.monitoredEndpointService = monitoredEndpointService;
    }


    /**
     * For each MonitoredEndpoint owned by user get last n entries of MonitoringResults
     * @param owner - user
     * @param limit - last n entries for each MonitoredEndpoint
     * @return list of Monitoring Result
     */
    public List<MonitoringResult> getLastNForEachURL(User owner, int limit) {
        //  Note: there might exist an SQL-query way to do this which I am not aware of
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
        MonitoringResult monitoringResult = sendRequest(monitoredEndpoint);
        monitoredEndpoint.setDateOfLastCheck(LocalDateTime.now());
        monitoredEndpointDao.save(monitoredEndpoint);
        dao.save(monitoringResult);
        return monitoringResult;
    }

    /**
     * Checks whether URL is found or if http request to URL failed
     * @param url to be queried
     * @return true if url is not found or request failed, false otherwise
     */
    private boolean urlIsNotFound(String url) {
        try {
            return this.sendGetRequest(url).statusCode() == 404;
        } catch (IOException | InterruptedException e) {
            return true;
        }
    }

    /**
     * This method sends a HTTP request according to MonitoredEndpoint (right now just GET supported), creates and returns MonitoringResult
     * @param monitoredEndpoint - monitored request
     * @throws MonitoringResultException if HTTP get request goes wrong
     * @return MonitoringResult of the request
     */
    private MonitoringResult sendRequest(MonitoredEndpoint monitoredEndpoint) {
        try {
            HttpResponse<String> response;
            MonitoringResult monitoringResult = new MonitoringResult();
            monitoringResult.setMonitoredEndpoint(monitoredEndpoint);
            monitoringResult.setDateOfCheck(LocalDateTime.now());
            //  only GET is supported for the sake of this task
            if (monitoredEndpoint.getHttpMethodEnum() == HttpMethodEnum.GET) {
                response = this.sendGetRequest(monitoredEndpoint.getUrl());
                monitoringResult.setReturnedHttpCode(response.statusCode());
                monitoringResult.setReturnedPayload(response.body());
            }
            return monitoringResult;
        } catch (IOException | InterruptedException e) {
            throw MonitoringResultException.create(monitoredEndpoint.getUrl(), monitoredEndpoint.getOwner().getUsername());
        }
    }

    private HttpResponse<String> sendGetRequest(String url) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        // Create HTTP request object
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        // Send HTTP request
        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());
        return response;
    }
}
