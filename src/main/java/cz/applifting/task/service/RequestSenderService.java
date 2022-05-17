package cz.applifting.task.service;

import cz.applifting.task.exceptions.BadUrlException;
import cz.applifting.task.exceptions.MonitoringResultException;
import cz.applifting.task.model.HttpMethodEnum;
import cz.applifting.task.model.MonitoredEndpoint;
import cz.applifting.task.model.MonitoringResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RequestSenderService {
    /**
     * Validates url using OWASP validation regex
     * @throws BadUrlException if url doesn't match
     */
    protected boolean urlIsInvalid(String url) {
        String URL_REGEX =
                "^((((https?|ftps?|gopher|telnet|nntp)://)|(mailto:|news:))" +
                        "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
                        "([).!';/?:,][[:blank:]])?$";
        Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

        Matcher matcher = URL_PATTERN.matcher(url);
        if (!matcher.matches()) {
            return true;
        }
        return false;
    }
    /**
     * Checks whether URL is found or if http request to URL failed
     * @param url to be queried
     * @return true if url is not found or request failed, false otherwise
     */
    protected boolean urlIsNotFound(String url) {
        try {
            boolean is404 = this.sendGetRequest(url).statusCode() == 404;
            return is404;
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * This method sends a HTTP request according to MonitoredEndpoint (right now just GET supported), creates and returns MonitoringResult
     * @param monitoredEndpoint - monitored request
     * @throws MonitoringResultException if HTTP get request goes wrong
     * @return MonitoringResult of the request
     */
    protected MonitoringResult sendRequest(MonitoredEndpoint monitoredEndpoint) {
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
        } catch (IOException | InterruptedException | IllegalArgumentException e) {
            throw MonitoringResultException.create(monitoredEndpoint.getUrl(), monitoredEndpoint.getOwner().getUsername());
        }
    }

    private HttpResponse<String> sendGetRequest(String url) throws IOException, InterruptedException, IllegalArgumentException {
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
