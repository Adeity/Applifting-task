package cz.applifting.task.model;

public enum HttpMethodEnum {
    GET("HTTP_GET"), POST("HTTP_POST"), PUT("HTTP_PUT"), DELETE("HTTP_DELETE");

    private final String name;

    HttpMethodEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
