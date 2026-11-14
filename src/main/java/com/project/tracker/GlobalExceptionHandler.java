package com.project.tracker;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String DUPLICATE_PROJECT_NAME_CONSTRAINT = "projects_name_key";
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ProblemDetail createProblemDetail(HttpStatus status, String detail, String title) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }

    private ConstraintViolationException getConstraintViolationException(DataIntegrityViolationException exception) {
        Throwable error = exception;
        while (error != null) {
            if (error instanceof ConstraintViolationException excp) {
                return excp;
            }
            error = error.getCause();
        }
        return null;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        ConstraintViolationException error = getConstraintViolationException(exception);
        if (error != null && Objects.equals(error.getConstraintName(), DUPLICATE_PROJECT_NAME_CONSTRAINT)) {
            return createProblemDetail(HttpStatus.CONFLICT, "project with this name already exists", "Project name conflict");
        } else {
            logger.error("Unhandled data integrity violation", exception);
            return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled data integrity violation", "database error");
        }

    }


}
