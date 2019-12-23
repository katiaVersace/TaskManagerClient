package com.alten.springboot.taskmanager_client.service;

import java.lang.annotation.Target;

import javax.ws.rs.HttpMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.ElementType;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod("PATCH")
public @interface PATCH {
}