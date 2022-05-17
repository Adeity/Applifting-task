package cz.applifting.task.dto;

import cz.applifting.task.model.HttpMethodEnum;
import lombok.Data;

@Data
public class MonitoredEndpointDto {
    private String url;
    private String name;
    private Integer monitoredInterval;
    private HttpMethodEnum httpMethodEnum;
}
