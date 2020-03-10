package com.alten.springboot.taskmanagerclient.controller;

import com.alten.springboot.taskmanagerclient.model.EmployeeDto;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.http.HttpHeaders;

public class CurrentSessionInfo {


    private static HttpHeaders headers;
    private static EmployeeDto user;

    public CurrentSessionInfo(HttpHeaders headers, EmployeeDto user) {
        super();
        this.headers = headers;
        this.user = user;

    }

    public static HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public static EmployeeDto getUser() {
        return user;
    }

    public void setUser(EmployeeDto user) {
        this.user = user;
    }

    public static boolean isAdmin() {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

}
