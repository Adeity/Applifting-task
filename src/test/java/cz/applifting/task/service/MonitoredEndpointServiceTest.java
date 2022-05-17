package cz.applifting.task.service;

import cz.applifting.task.dao.MonitoredEndpointDao;
import cz.applifting.task.dto.MonitoredEndpointDto;
import cz.applifting.task.exceptions.BadUrlException;
import cz.applifting.task.exceptions.NotFoundException;
import cz.applifting.task.exceptions.NullParameterException;
import cz.applifting.task.model.HttpMethodEnum;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(
        MockitoExtension.class
)
class MonitoredEndpointServiceTest {
    @InjectMocks
    MonitoredEndpointService monitoredEndpointService;
    @Mock
    MonitoredEndpointDao monitoredEndpointDao;
    @Mock
    RequestSenderService requestSenderService;


    @Test
    @DisplayName("MonitoredEndpoint gets deactivated and returned")
    void deactivateEndpoint(){
        // arrange
        String url = "url";
        User owner = new User();
        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();

        Mockito.when(monitoredEndpointDao.findByUrlAndOwner(url, owner)).thenReturn(Optional.of(monitoredEndpoint));

        // act
        MonitoredEndpoint res = monitoredEndpointService.deactivateMonitoredEndpoint(url, owner);

        // verify
        Mockito.verify(monitoredEndpointDao, Mockito.times(1)).save(monitoredEndpoint);

        assertEquals(monitoredEndpoint, res);
        assertFalse(res.isActive());
    }

    @Test
    @DisplayName("MonitoredEndpoint is not found so throw error")
    void deactivateEndpointCantFind(){
        // arrange
        String url = "url";
        User owner = new User();
        MonitoredEndpoint monitoredEndpoint = new MonitoredEndpoint();

        Mockito.when(monitoredEndpointDao.findByUrlAndOwner(url, owner)).thenReturn(Optional.empty());

        // act, verify
        assertThrows(NotFoundException.class, () -> monitoredEndpointService.deactivateMonitoredEndpoint(url, owner));
    }

    @Test
    @DisplayName("Create MonitoringEndpoint fails because DTO is missing attributes")
    void createMonitoringEndpointInvalidAttributes() {
        //  arrange
        User user = new User();
        MonitoredEndpointDto dto = new MonitoredEndpointDto();

        // act, verify
        assertThrows(NullParameterException.class, () -> monitoredEndpointService.createMonitoredEndpoint(dto, user));
    }

    @Test
    @DisplayName("Create MonitoringEndpoint because URL is invalid")
    void createMonitoringEndpointInvalidURL() {
        //  arrange
        User user = new User();
        MonitoredEndpointDto dto = new MonitoredEndpointDto();
        dto.setMonitoredInterval(3);
        dto.setName("My awesome dto");
        dto.setUrl("BAD_URL");
        dto.setHttpMethodEnum(HttpMethodEnum.GET);

        Mockito.when(requestSenderService.urlIsInvalid(dto.getUrl())).thenReturn(true);


        // act, verify
        assertThrows(BadUrlException.class, () -> monitoredEndpointService.createMonitoredEndpoint(dto, user));
    }

    @Test
    @DisplayName("Create MonitoringEndpoint is successful")
    void createMonitoringEndpointIsSuccessful() {
        //  arrange
        User user = new User();
        MonitoredEndpointDto dto = new MonitoredEndpointDto();
        dto.setMonitoredInterval(3);
        dto.setName("My awesome dto");
        dto.setUrl("BAD_URL");
        dto.setHttpMethodEnum(HttpMethodEnum.GET);

        // act
        monitoredEndpointService.createMonitoredEndpoint(dto, user);

        // verify
        Mockito.verify(monitoredEndpointDao, Mockito.times(1)).save(Mockito.any());
    }
}