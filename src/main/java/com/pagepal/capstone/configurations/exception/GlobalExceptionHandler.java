package com.pagepal.capstone.configurations.exception;

import com.pagepal.capstone.configurations.exception.custom.QuantityNotEnoughException;
import com.pagepal.capstone.configurations.exception.custom.TokenInvalidException;
import graphql.GraphQLError;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleRuntimeException(RuntimeException ex) {
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> extensions = Map.of("status", HttpStatus.NOT_FOUND.value());
        return GraphQLError.newError().errorType(ErrorType.NOT_FOUND).extensions(extensions).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleValidationException(ValidationException ex) {
        Map<String, Object> extensions = Map.of("status", HttpStatus.BAD_REQUEST.value());
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).extensions(extensions).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleValidationException(QuantityNotEnoughException ex) {
        Map<String, Object> extensions = Map.of("status", HttpStatus.BAD_REQUEST.value());
        return GraphQLError.newError().errorType(ErrorType.BAD_REQUEST).extensions(extensions).message(ex.getMessage()).build();
    }

    @GraphQlExceptionHandler
    public GraphQLError handleTokenInvalidationException(TokenInvalidException ex) {
        Map<String, Object> extensions = Map.of("status", HttpStatus.UNAUTHORIZED.value());
        return GraphQLError.newError().errorType(ErrorType.UNAUTHORIZED).extensions(extensions).message(ex.getMessage()).build();
    }
}
