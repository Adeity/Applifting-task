package cz.applifting.task.rest;

import cz.applifting.task.dto.CheckEndpointCommandDto;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.MonitoringResult;
import cz.applifting.task.model.User;
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

    public List<MonitoredEndpoint> getLastTenForEachURL(User owner) {
        return service.getLastTenForEachURL(userService.getAuthenticatedUser());
    }

    /**
     * Endpoint where commands to monitor a MonitoredEndpoint (to create MonitoringResult) get sent
     * @param dto
     * @return MonitoringResult
     */
    @PostMapping(value = "/check", consumes = (MediaType.APPLICATION_JSON_VALUE))
    public MonitoringResult checkEndpoint(@RequestBody CheckEndpointCommandDto dto){
        log.debug(dto.getUrl());
        log.debug(dto.toString());
//        return null;
        return service.checkEndpoint(dto.getUrl(), userService.getAuthenticatedUser());
    }
}
