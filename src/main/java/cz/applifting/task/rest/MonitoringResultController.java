package cz.applifting.task.rest;

import cz.applifting.task.dto.CheckEndpointCommandDto;
import cz.applifting.task.model.MonitoringResult;
import cz.applifting.task.service.MonitoringResultService;
import cz.applifting.task.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/monitoring")
public class MonitoringResultController {
    private final UserService userService;
    private final MonitoringResultService service;

    public MonitoringResultController(UserService userService, MonitoringResultService service) {
        this.userService = userService;
        this.service = service;
    }

    /**
     * Endpoint to list 10 last MonitoredResults for each Monitored URL
     */
    @GetMapping(value = "/list")
    public List<MonitoringResult> getLastTenForEachURL() {
        return service.getLastNForEachURL(userService.getAuthenticatedUser(), 10);
    }

    /**
     * Endpoint where commands to monitor a MonitoredEndpoint (to create MonitoringResult) get sent
     */
    @PostMapping(value = "/check", consumes = (MediaType.APPLICATION_JSON_VALUE))
    public MonitoringResult checkEndpoint(@RequestBody CheckEndpointCommandDto dto){
        return service.checkEndpoint(dto.getUrl(), userService.getAuthenticatedUser());
    }
}
