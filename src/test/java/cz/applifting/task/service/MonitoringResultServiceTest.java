package cz.applifting.task.service;

import cz.applifting.task.dao.MonitoredEndpointDao;
import cz.applifting.task.dao.MonitoringResultDao;
import cz.applifting.task.exceptions.MonitoringResultException;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.MonitoringResult;
import cz.applifting.task.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(
        MockitoExtension.class
)
class MonitoringResultServiceTest {
    @InjectMocks
    MonitoringResultService monitoringResultService;
    @Mock
    MonitoredEndpointService monitoredEndpointService;
    @Mock
    RequestSenderService requestSenderService;
    @Mock
    MonitoringResultDao monitoringResultDao;
    @Mock
    MonitoredEndpointDao monitoredEndpointDao;

    @Test
    @DisplayName("Endpoint does not get checked because MonitoredEndpoint is not active")
    void endpointDoesntGetChecked(){
        //  arrange
        String url = "url";
        User user = new User();

        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();
        monitoredEndpoint.setActive(false);

        Mockito.when(monitoredEndpointService.getMonitoredEndpointByUrlAndOwner(url, user)).thenReturn(monitoredEndpoint);

        // assert and assert
        assertThrows(MonitoringResultException.class, () -> monitoringResultService.checkEndpoint(url, user));
    }

    @Test
    @DisplayName("Endpoint gets checked and new MonitoringResult gets saved")
    void endpointGetsSaved(){
        //  arrange
        String url = "url";
        User user = new User();
        MonitoringResult monitoringResult = new MonitoringResult();

        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();

        Mockito.when(monitoredEndpointService.getMonitoredEndpointByUrlAndOwner(url, user)).thenReturn(monitoredEndpoint);
        Mockito.when(requestSenderService.sendRequest(monitoredEndpoint)).thenReturn(monitoringResult);

        // act
        MonitoringResult actual = monitoringResultService.checkEndpoint(url, user);

        // assert
        assertEquals(monitoringResult, actual);
    }

}