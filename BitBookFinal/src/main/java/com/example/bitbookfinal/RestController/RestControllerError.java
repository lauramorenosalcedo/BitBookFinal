package com.example.bitbookfinal.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
public class RestControllerError {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ResponseEntity<String> errorRest() { //If the url is not included in one of the RestControllers or is incorrect, it returns: "URL not found"
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL not found");
    }
}