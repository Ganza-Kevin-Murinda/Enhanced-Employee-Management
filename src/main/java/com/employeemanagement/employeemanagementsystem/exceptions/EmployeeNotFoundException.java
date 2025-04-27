package com.employeemanagement.employeemanagementsystem.exceptions;

/**
 * Exception thrown when an employee cannot be found in the database
 */
public class EmployeeNotFoundException extends Exception {

    //Constructs a new EmployeeNotFoundException with the specified detail message
    public EmployeeNotFoundException(String message) {
        super(message);
    }

    //Constructs a new EmployeeNotFoundException with the specified detail message and cause
    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
