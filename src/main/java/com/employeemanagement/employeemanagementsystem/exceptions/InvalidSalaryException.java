package com.employeemanagement.employeemanagementsystem.exceptions;

/**
 * Exception thrown when an invalid salary value is provided
 */
public class InvalidSalaryException extends Exception {

    // Constructs a new InvalidSalaryException with the specified detail message
    public InvalidSalaryException(String message) {
        super(message);
    }

    // Constructs a new InvalidSalaryException with the specified detail message and cause
    public InvalidSalaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
