package cz.applifting.task.rest;

import cz.applifting.task.dto.MonitoredEndpointDto;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.service.MonitoredEndpointService;
import cz.applifting.task.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping(value = "")
    public String testMapping(){
        Object o = SecurityContextHolder.getContext().getAuthentication();
        return "tested";
    }

    @GetMapping(value = "/{name}")
    public List<MonitoredEndpoint> getMonitoredEndpointByName(@PathVariable String name) {
        return this.service.getByName(name, userService.getAuthenticatedUser());
    }

    @DeleteMapping(value = "/delete")
    public MonitoredEndpoint getMonitoredEndpointByName(@RequestBody MonitoredEndpointDto dto) {
        return null;
    }

    @PostMapping(value = "/create", consumes = (MediaType.APPLICATION_JSON_VALUE))
    public void createMonitoredEndpoint(@RequestBody MonitoredEndpointDto dto) {
        service.createMonitoredEndpoint(dto, userService.getAuthenticatedUser());
    }
}
