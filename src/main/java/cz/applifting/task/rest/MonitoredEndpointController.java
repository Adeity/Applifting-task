package cz.applifting.task.rest;

import cz.applifting.task.dto.MonitoredEndpointDto;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.service.MonitoredEndpointService;
import cz.applifting.task.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/monitoredEndpoint")
public class MonitoredEndpointController {
    private final MonitoredEndpointService service;
    private final UserService userService;

    @Autowired
    public MonitoredEndpointController(MonitoredEndpointService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    /**
     * GET Monitored Endpoint
     */
    @GetMapping(value = "")
    public List<MonitoredEndpoint> getAllMonitoredEndpoints() {
        return this.service.findAll();
    }

    /**
     * GET Monitored Endpoint By name
     */
    @GetMapping(value = "/{name}")
    public List<MonitoredEndpoint> getMonitoredEndpointByName(@PathVariable String name) {
        return this.service.getByName(name);
    }

    /**
     * DELETE Monitored Endpoint
     */
    @DeleteMapping(value = "/delete")
    public void getMonitoredEndpointByName(@RequestBody MonitoredEndpointDto dto) {
        service.deactivateMonitoredEndpoint(dto.getUrl(), userService.getAuthenticatedUser());
    }

    /**
     * UPDATE Monitored Endpoint
     */
    @PatchMapping(value = "/edit", consumes = (MediaType.APPLICATION_JSON_VALUE))
    public MonitoredEndpoint updateMonitoredEndpoint(@RequestBody MonitoredEndpointDto dto) {
        return service.createMonitoredEndpoint(dto, userService.getAuthenticatedUser());
    }

    /**
     * CREATE Monitored Endpoint
     */
    @PostMapping(value = "/create", consumes = (MediaType.APPLICATION_JSON_VALUE))
    public MonitoredEndpoint createMonitoredEndpoint(@RequestBody MonitoredEndpointDto dto) {
        return service.createMonitoredEndpoint(dto, userService.getAuthenticatedUser());
    }
}
