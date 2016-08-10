package ru.pkorobeinikov.restservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// see https://spring.io/blog/2013/11/01/exception-handling-in-spring-mvc

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Not Found")
public class ResourceNotFoundException extends RuntimeException {
}
