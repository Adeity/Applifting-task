## Applifting Java BE task - implementation
### What I have done
I created a Java Spring Boot application using multi-tier architecture.
### Tasks done
- CRUD of Monitored URLs 
- I simulate periodical monitoring of URLs by sending an HTTP request. I have done this becaue I don't know how to schedule a task based on the "monitored interval" attribute, so I used an extra endpoint as a workaround. Perhaps this way another node in the microservice architecture could be sending those requests to check on registered URLs.
- I successfully implemented listing of 10 last monitored results for each particular monitored URL
- Users must authenticate themselves by sending their access token in "Applifting-Authorization" HTTP header.
- Authorization was also implemented. Each user is authorized to view only his/hers MonitoredEndpoints and MonitoredResults.
- I have written unit tests using JUnit with Mockito library.
### Endpoints
#### /monitoredEndpoint
- GET / - lists all MonitoredEndpoint entities available to authenticated user. Response body may look like this:
```
[
    {
        "id": 4,
        "name": "starwars second movie",
        "url": "https://swapi.dev/api/films/2/?format=json",
        "dateOfCreation": "2022-05-17T12:42:11.201976",
        "dateOfLastCheck": "2022-05-17T13:15:52.865519",
        "monitoringInterval": 2,
        "owner": {
            "id": 2,
            "username": "Applifting",
            "email": "info@applifting.cz",
            "userAccessToken": "93f39e2f-80de-4033-99ee-249d92736a25"
        },
        "httpMethodEnum": "GET",
        "active": false
    },
    ... and so on
```
- GET /{name} - list MonitoredEndpoint by its name attribute
- DELETE /delete - delete (actually deactivate) MonitoredEndpoint by URL and user who owns it.
- PATCH /edit - edit existintg MonitoredEndpoint
- POST /create - Create new MonitoredEndpoint. This endpoint has a precondition for url parameter. It must include protocol. So for example "google.com" is not a valid url. POST body may look like this:
```
{
    "url": "https://swapi.dev/api/3/",
    "name": "sw planet 3",
    "monitoredInterval": 2,
    "httpMethodEnum": "GET"
}
```
#### /monitoring
- GET /list - lists last 10 MonitoringResult for each MonitoringEndpoint owned by authenticated user. Response body may look like this:
```
[
    {
        "id": 58,
        "dateOfCheck": "2022-05-17T13:18:36.333555",
        "returnedHttpCode": 200,
        "returnedPayload": "{\"name\":\"Yavin IV\",\"rotation_period\":\"24\",\"orbital_period\":\"4818\",\"diameter\":\"10200\",\"climate\":\"temperate, tropical\",\"gravity\":\"1 standard\",\"terrain\":\"jungle, rainforests\",\"surface_water\":\"8\",\"population\":\"1000\",\"residents\":[],\"films\":[\"https://swapi.dev/api/films/1/\"],\"created\":\"2014-12-10T11:37:19.144000Z\",\"edited\":\"2014-12-20T20:58:18.421000Z\",\"url\":\"https://swapi.dev/api/planets/3/\"}",
        "monitoredEndpoint": {
            "id": 11,
            "name": "sw planet 3",
            "url": "https://swapi.dev/api/planets/3?/format=json",
            "dateOfCreation": "2022-05-17T12:47:45.477568",
            "dateOfLastCheck": "2022-05-17T13:18:46.470161",
            "monitoringInterval": 2,
            "owner": {
                "id": 3,
                "username": "Batman",
                "email": "batman@example.com",
                "userAccessToken": "dcb20f8a-5657-4f1b-9f7f-ce65739b359e"
            },
            "httpMethodEnum": "GET",
            "active": false
        }
    },
    {
        "id": 59,
        "dateOfCheck": "2022-05-17T13:18:38.261487",
        "returnedHttpCode": 200,
        "returnedPayload": "{\"name\":\"Yavin IV\",\"rotation_period\":\"24\",\"orbital_period\":\"4818\",\"diameter\":\"10200\",\"climate\":\"temperate, tropical\",\"gravity\":\"1 standard\",\"terrain\":\"jungle, rainforests\",\"surface_water\":\"8\",\"population\":\"1000\",\"residents\":[],\"films\":[\"https://swapi.dev/api/films/1/\"],\"created\":\"2014-12-10T11:37:19.144000Z\",\"edited\":\"2014-12-20T20:58:18.421000Z\",\"url\":\"https://swapi.dev/api/planets/3/\"}",
        "monitoredEndpoint": {
            "id": 11,
            "name": "sw planet 3",
            "url": "https://swapi.dev/api/planets/3?/format=json",
            "dateOfCreation": "2022-05-17T12:47:45.477568",
            "dateOfLastCheck": "2022-05-17T13:18:46.470161",
            "monitoringInterval": 2,
            "owner": {
                "id": 3,
                "username": "Batman",
                "email": "batman@example.com",
                "userAccessToken": "dcb20f8a-5657-4f1b-9f7f-ce65739b359e"
            },
            "httpMethodEnum": "GET",
            "active": false
        }
    },
    ...
```
- POST /check - make a request to MonitoredEndpoint and create a MonitoringResult. POST body may look like this.
```
{
    "url": "https://swapi.dev/api/planets/6/?format=json"
}
```
### Validation
User can create new MonitoredEndpoints so it is crucial that this is the entity for which input must be validated. 
When user wishes to register new MonitoredEndpoint I validate that the name, monitoredInterval of input DTO are set.
Also before persisting I validate that the url given is a valid one and that it doesn't return 404 Not found HTTP status.
### Entity Model
I used the suggested entity model from the original task sheet. I added an "active" flag attribute to MonitoredEndpoint which gets set to false if user wishes to delete that URL.<br>
Also I added "httpMethod" attribute to better differentiate between endpoints. However I only allow HTTP GET for the sake of this task.
![Entity diagram](images/applifting_task.drawio_new.png)
### Notes about DB
- The values of User were hardcoded using SQL Insert command.
- Column returnedPayload was altered to be VARCHAR(10000) instead of VARCHAR(255) so I wouldn't receive an error if returned payload was too large.
### How to start using Docker
- Pull the source code
- Fill your DB connection values into application.properties
    - spring.datasource.url must be corresponding to mysql docker image, e.g jdbc:mysql://mysql-al:3306/applifting
- Build from the root of this project using command: docker build -t applifting-task:1.0 .
- Run using: docker-compose -f docker-compose.yaml up -d
### Conclusion
I really enjoyed implementing this task. I better learned how Spring Boot handles authentication which ended up being very simple for this project. <Br>
I also got to work with MySQL for the first time as I mostly use PostgreSQL. I think MySQL workbench is a much friendlier DB client than pgAdmin. <br>
I implemented this task how I know so I hope you will enjoy reading through my code and hopefully we will be in touch.
