package com.employeemanagement.employeemanagementsystem.exceptions;
/**
 * Exception thrown when an invalid department is specified
 */
public class InvalidDepartmentException extends Exception {

    //Constructs a new InvalidDepartmentException with the specified detail message
    public InvalidDepartmentException(String message) {
        super(message);
    }

    //Constructs a new InvalidDepartmentException with the specified detail message and cause
    public InvalidDepartmentException(String message, Throwable cause) {
        super(message, cause);
    }
}
