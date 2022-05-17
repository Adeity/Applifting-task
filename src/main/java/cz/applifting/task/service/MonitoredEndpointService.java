package cz.applifting.task.service;

import cz.applifting.task.dao.MonitoredEndpointDao;
import cz.applifting.task.dao.UserDao;
import cz.applifting.task.dto.MonitoredEndpointDto;
import cz.applifting.task.exceptions.NotFoundException;
import cz.applifting.task.exceptions.NullParameterException;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MonitoredEndpointService {
    private final UserDao userDao;
    private final MonitoredEndpointDao monitoredEndpointDao;

    @Autowired
    public MonitoredEndpointService(UserDao userDao, MonitoredEndpointDao monitoredEndpointDao) {
        this.userDao = userDao;
        this.monitoredEndpointDao = monitoredEndpointDao;
    }

    /**
     * Create Monitored Endpoint from user input dto
     */
    @Transactional
    public MonitoredEndpoint createMonitoredEndpoint(MonitoredEndpointDto dto, User owner) {
        NullParameterException.checkNullThrowNullParameter(dto.getMonitoredInterval(), "monitored interval");
        NullParameterException.checkNullThrowNullParameter(dto.getName(), "name");
        NullParameterException.checkNullThrowNullParameter(dto.getHttpMethodEnum(), "http method");
        NullParameterException.checkNullThrowNullParameter(dto.getUrl(), "url");


        // create the business object from dto
        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint(dto);
        // if it already exists, then set id to new object so existing entity gets updated
        Optional<MonitoredEndpoint> monitoredEndpointOptional = monitoredEndpointDao.findByUrlAndOwner(dto.getUrl(), owner);
        monitoredEndpointOptional.ifPresent(endpoint -> monitoredEndpoint.setId(endpoint.getId()));

        monitoredEndpoint.setOwner(owner);
        monitoredEndpointDao.save(monitoredEndpoint);
        return monitoredEndpoint;
    }

    /**
     * Delete/deactivate the endpoint.
     * @param url - url of endpoint
     * @param owner - user owner of MonitoredEndpoint
     */
    @Transactional
    public void deactivateMonitoredEndpoint(String url, User owner) {
        Optional<MonitoredEndpoint> m = monitoredEndpointDao.findByUrlAndOwner(url, owner);
        if (m.isPresent()) {
            MonitoredEndpoint res = m.get();
            res.setActive(false);
            monitoredEndpointDao.save(res);
        }
        throw NotFoundException.create("MonitoredEndpoint", "url " + url);
    }

    /**
     * GET all MonitoredEndpoints owned by authenticated user
     */
    @PostFilter("authentication.getCredentials() == filterObject.owner.userAccessToken")
    public List<MonitoredEndpoint> findAll() {
        return monitoredEndpointDao.findAll();
    }


    /**
     * GET all MonitoredEndpoints owned by authenticated user with name @name
     */
    @PostFilter("authentication.getCredentials() == filterObject.owner.userAccessToken")
    public List<MonitoredEndpoint> getByName(String name) {
        List<MonitoredEndpoint> res = monitoredEndpointDao.findByName(name);
        return res;
    }

    /**
     * Find MonitoredEndpoint by its url and owner
     * @param url - url of MonitoredEndpoint
     * @param owner - user owner of MonitoredEndpoint
     */
    public MonitoredEndpoint getMonitoredEndpointByUrlAndOwner(String url, User owner) {
        Optional<MonitoredEndpoint> res = monitoredEndpointDao.findByUrlAndOwner(url, owner);
        if (res.isEmpty()) {
            throw NotFoundException.create("Monitored Endpoint", "url: " + url);
        }
        return res.get();
    }
}
