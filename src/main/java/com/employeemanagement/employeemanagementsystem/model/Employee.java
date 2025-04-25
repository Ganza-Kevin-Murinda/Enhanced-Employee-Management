package com.employeemanagement.employeemanagementsystem.model;

/**
* Generic Employee class that stores employee information
* <T> Type of the employee ID (can be Integer, String, UUID)
* Defining the natural sorting order of the Employee objects.
*/

import java.util.Objects;

public class Employee<T> implements Comparable<Employee<T>> {

    private T employeeId;
    private String employeeName;
    private EEmployeeDepartment employeeDepartment;
    private double employeeSalary;
    private double performanceRating;
    private int yearsOfExperience;
    private boolean isActive;

    // Constructor
    public Employee(T employeeId, String employeeName, EEmployeeDepartment employeeDepartment, double employeeSalary, double performanceRating, int yearsOfExperience, boolean isActive) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeDepartment = employeeDepartment;
        this.employeeSalary = employeeSalary;
        this.performanceRating = performanceRating;
        this.yearsOfExperience = yearsOfExperience;
        this.isActive = isActive;
    }

    public T getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(T employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public EEmployeeDepartment getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(EEmployeeDepartment employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public double getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(double employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public double getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(double performanceRating) {
        this.performanceRating = performanceRating;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // implements a comparable interface to sort employees by years of experience (descending order)
    @Override
    public int compareTo(Employee<T> other){
        // Negative(-) for descending
        return other.yearsOfExperience - this.yearsOfExperience;
    }

    // checks if two employee objects are the same
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        //type casting the object to employee object
        Employee<?> employee = (Employee<?>) obj;
        return Objects.equals(employeeId, employee.employeeId);
    }

    // creates a hash code for an employee object for fast searching and storage
    @Override
    public int hashCode() {
        return Objects.hash(employeeId);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + employeeId +
                ", name='" + employeeName + '\'' +
                ", department='" + employeeDepartment + '\'' +
                ", salary=" + employeeSalary +
                ", rating=" + performanceRating +
                ", experience=" + yearsOfExperience +
                ", active=" + isActive +
                '}';
    }
}
