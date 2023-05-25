package com.ps.eventstore.Exception;

public class EmployeeDetailsNotFoundException extends RuntimeException {
    public EmployeeDetailsNotFoundException(String message){
        super(message);
    }

    public EmployeeDetailsNotFoundException(Throwable cause) {
        super(cause);
    }
}
