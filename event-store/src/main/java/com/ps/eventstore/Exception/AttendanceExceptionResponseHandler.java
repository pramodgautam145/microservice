package com.ps.eventstore.Exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;


@ControllerAdvice
@Slf4j
public class AttendanceExceptionResponseHandler
        extends ResponseEntityExceptionHandler {

    private static final String INVALID_EMP_ID_TYPE = "Please Provide Correct swipe";
    private static final String INVALID_USERNAME = "Employee Details is not available";
    private final ObjectMapper objectMapper;

    @Autowired
    public AttendanceExceptionResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.registerModule(javaTimeModule);
    }

    /***
     *
     * This method intercepts StatementException thrown during execution of code and
     * return BAD_REQUEST status code
     * @param ex RuntimeException caught during execution of request
     * @param request the spring context request
     * @return ResponseEntity<Object> containing details about exception
     * @throws JsonProcessingException
     */
    @ExceptionHandler(value = RecordNotFoundException.class)
    protected ResponseEntity<Object> handleStatementException(
            RuntimeException ex, WebRequest request) throws JsonProcessingException {
        log.warn(ex.getMessage(), ex);
        String bodyOfResponse = objectMapper.writeValueAsString(getStatementErrorResponse(ex,
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                INVALID_EMP_ID_TYPE));
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }



    private ErrorResponse getStatementErrorResponse(Exception e, String status, String type) {
        return ErrorResponse.builder()
                .type(type)
                .title(e.getMessage())
                .status(status)
                .detail(e.getMessage())
                .date(LocalDateTime.now())
                .build();
    }

    private ErrorResponse getRecordErrorResponse(Exception e, String status, String type) //NOSONAR
    {
        return ErrorResponse.builder()
                .type(type)
                .title("No Record Found")
                .status(status)
                .detail("No Record Found")
                .date(LocalDateTime.now())
                .build();
    }
    @ExceptionHandler(value = EmployeeDetailsNotFoundException.class)
    protected ResponseEntity<Object> userNameNotFoundException(
            RuntimeException ex, WebRequest request) throws JsonProcessingException {
        String bodyOfResponse = objectMapper.writeValueAsString(getRecordErrorResponse(ex,
                String.valueOf(HttpStatus.BAD_REQUEST.value()),
                INVALID_USERNAME));
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
