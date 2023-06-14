package ru.skillbox.users.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.skillbox.users.controller.CityController;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    final static Logger logger = LoggerFactory.getLogger(CityController.class);

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(final UserNotFoundException e) {
        logger.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = CityNotFoundException.class)
    public ResponseEntity<String> handleCityNotFoundException(final CityNotFoundException e) {
        logger.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = SubscriptionNotFoundException.class)
    public ResponseEntity<String> handleSubscriptionNotFoundException(final SubscriptionNotFoundException e) {
        logger.info(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}