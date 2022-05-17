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

    @Transactional
    public void createMonitoredEndpoint(MonitoredEndpointDto dto, User owner) {
        NullParameterException.checkNullThrowNullParameter(dto.getMonitoredInterval(), "monitored interval");
        NullParameterException.checkNullThrowNullParameter(dto.getName(), "name");
        NullParameterException.checkNullThrowNullParameter(dto.getHttpMethodEnum(), "http method");
        NullParameterException.checkNullThrowNullParameter(dto.getUrl(), "url");

        // create the business object from dto ere
        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint(dto);

        monitoredEndpoint.setOwner(owner);
        monitoredEndpointDao.save(monitoredEndpoint);
    }

    @PostFilter("authentication.getCredentials() == filterObject.owner.userAccessToken")
    public List<MonitoredEndpoint> getByName(String name, User owner) {
        List<MonitoredEndpoint> res = monitoredEndpointDao.findByName(name);
        return res;
    }

    public List<MonitoredEndpoint> getAllByOwner(User owner) {
        List<MonitoredEndpoint> res = monitoredEndpointDao.findByOwner(owner);
        return res;
    }



    @Transactional
    public void deleteMonitoredEndpoint(MonitoredEndpointDto dto, User owner) {

    }

    public MonitoredEndpoint getMonitoredEndpointByUrlAndOwner(String url, User owner) {
        Optional<MonitoredEndpoint> res = monitoredEndpointDao.findByUrlAndOwner(url, owner);
        if (res.isEmpty()) {
            throw NotFoundException.create("Monitored Endpoint", "url: " + url);
        }
        return res.get();
    }
}
