package com.employeemanagement.employeemanagementsystem.model;

import com.employeemanagement.employeemanagementsystem.model.comparator.EmployeePerformanceComparator;
import com.employeemanagement.employeemanagementsystem.model.comparator.EmployeeSalaryComparator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
* Manages employee records using a HashMap
* Provides: adding, removing, updating, searching and sorting employees operations
**/
public class EmployeeDatabase<T> {
    private Map<T, Employee<T>> employees;

    //constructor
    public EmployeeDatabase(){
        this.employees = new HashMap<>();
    }

    //CRUD Operations

    //Create
    public boolean addEmployee(Employee<T> employee){
        if(employees.containsKey(employee.getEmployeeId())){
            return false; // Employee with this ID already exist
        }
        employees.put(employee.getEmployeeId(), employee);
        return true;
    }

    //Retrieve All
    public List<Employee<T>> getAllEmployees(){
        return new ArrayList<>(employees.values());
    }

    //Retrieve employee by employeeId
    public Employee<T> getEmployee(T employeeId){
        return employees.get(employeeId);
    }

    //Update
    public boolean updateEmployeeDetails(T employeeId, String field, Object newValue){
        //checks if employee exist
        if(!employees.containsKey(employeeId)){
            return false; // employee not found
        }
        Employee<T> employee = employees.get(employeeId);

        try{

            switch (field.toLowerCase()){
                case "employeename":
                    employee.setEmployeeName((String) newValue);
                    break;
                case "employeedepartment":
                    employee.setEmployeeDepartment((EEmployeeDepartment) newValue);
                    break;
                case "employeesalary":
                    employee.setEmployeeSalary((Double) newValue);
                    break;
                case "performancerating":
                    employee.setPerformanceRating((Double) newValue);
                    break;
                case "yearofexperience":
                    employee.setYearsOfExperience((Integer) newValue);
                    break;
                case "active":
                    employee.setActive((Boolean) newValue);
                    break;
                default:
                    return false; //Invalid field
            }
            return true; // updated
        }catch (ClassCastException e){
            return false; // Invalid value type
        }
    }

    //Delete
    public boolean deleteEmployee(T employeeId){
        //checks if employee exist
        if(!employees.containsKey(employeeId)){
            return false; // Employee not found
        }
        employees.remove(employeeId);
        return true;
    }

    // Search and filter Operations

    //Finds employees by department
    public List<Employee<T>> findEmployeesByDepartment(String department){
        return employees.values().stream()
                .filter(employee -> employee.getEmployeeDepartment().name().equalsIgnoreCase(department))
                .collect(Collectors.toList());
    }

    //Finds employees whose names contain the given search term
    public List<Employee<T>> findEmployeesByName(String searchTerm){
        return employees.values().stream()
                .filter(employee -> employee.getEmployeeName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    //Finds employees with performance rating at or above the specified minimum
    public List<Employee<T>> findEmployeesByMinRating(Double minRating){
        return employees.values().stream()
                .filter(employee -> employee.getPerformanceRating() >= minRating)
                .collect(Collectors.toList());
    }

    //Finds employees with salary in the specified range
    public List<Employee<T>> findEmployeesBySalaryRange(double minSalary, double maxSalary) {
        return employees.values().stream()
                .filter(e -> e.getEmployeeSalary() >= minSalary && e.getEmployeeSalary() <= maxSalary)
                .collect(Collectors.toList());
    }

    //Find employees based on a custom filter
    public List<Employee <T>> findEmployees(Predicate<Employee<T>> filter){
        return employees.values().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    // Sorting Operations

    //Gets employees sorted by years of experience
    public List <Employee<T>> getEmployeesSortedByExperience(){
        List<Employee<T>> sortedList = new ArrayList<>(employees.values());
        Collections.sort(sortedList); //uses the compareTo method in Employee class
        return sortedList;
    }

    // Gets employees sorted by salary
    public List<Employee<T>> getEmployeesSortedBySalary() {
        List<Employee<T>> sortedList = new ArrayList<>(employees.values());
        sortedList.sort(new EmployeeSalaryComparator<>());
        return sortedList;
    }

    //Gets employees sorted by performance rating
    public List<Employee<T>> getEmployeesSortedByPerformance() {
        List<Employee<T>> sortedList = new ArrayList<>(employees.values());
        sortedList.sort(new EmployeePerformanceComparator<>());
        return sortedList;
    }

    //Gives a salary raise to employees with high performance
    public int giveSalaryRaiseToHighPerformers() {
        int count = 0;
        for (Employee<T> employee : employees.values()) {
            if (employee.getPerformanceRating() >= 3.5) {
                double newSalary = employee.getEmployeeSalary() * (1 + 2.0 / 100);
                employee.setEmployeeSalary(newSalary);
                count++;
            }
        }
        return count;
    }

    //Gets the highest-paid employees
    public List<Employee<T>> getTopPaidEmployees(int n) {
        return employees.values().stream()
                .sorted(new EmployeeSalaryComparator<>())
                .limit(n)
                .collect(Collectors.toList());
    }

    //Calculates the average salary in a department
    public double calculateAverageSalaryByDepartment(String department) {
        List<Employee<T>> departmentEmployees = findEmployeesByDepartment(department);

        if (departmentEmployees.isEmpty()) {
            return 0;
        }

        return departmentEmployees.stream()
                .mapToDouble(Employee::getEmployeeSalary)
                .average()
                .orElse(0);
    }

    //Gets the total number of employees in the database
    public int getTotalEmployeeCount() {
        return employees.size();
    }

    //Gets the number of active employees in the database
    public int getActiveEmployeeCount() {
        return (int) employees.values().stream()
                .filter(Employee::isActive)
                .count();
    }
    //Traverse all employees
    public Iterator<Employee<T>> getEmployeeIterator() {
        return employees.values().iterator();
    }

    // Console Display

    // Displays all employees using a for-each loop
    public void displayEmployeesWithForEach() {
        // Print header
        String headerFormat = "%-10s %-20s %-15s %-12s %-12s %-10s %-10s%n";
        System.out.println("\n=== Employee ===");
        System.out.printf(headerFormat, "ID", "Name", "Department", "Salary ($)", "Rating", "Experience", "Status");
        System.out.println("----------------------------------------------------------------------------------------------------");

        // Format and print each employee
        String rowFormat = "%-10s %-20s %-15s %-12.2f %-12.2f %-10d %-10s%n";
        for (Employee<T> employee : employees.values()) {
            System.out.printf(rowFormat,
                    employee.getEmployeeId().toString(),
                    truncateText(employee.getEmployeeName(), 20),
                    employee.getEmployeeDepartment().name(),
                    employee.getEmployeeSalary(),
                    employee.getPerformanceRating(),
                    employee.getYearsOfExperience(),
                    employee.isActive() ? "Active" : "Inactive"
            );
        }
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Total Employees: " + getTotalEmployeeCount());
        System.out.println("Total Active Employees: " + getActiveEmployeeCount());
    }

    // Generates and displays formatted employee reports using Stream API
    public void displayEmployeesWithStreams() {
        System.out.println("\n=== Employee Report ===");

        // Print header
        String headerFormat = "%-10s %-20s %-15s %-12s %-12s %-10s %-10s%n";
        System.out.printf(headerFormat, "ID", "Name", "Department", "Salary ($)", "Rating", "Experience", "Status");
        System.out.println("----------------------------------------------------------------------------------------------------");

        // Use Stream API to format and print each employee
        String rowFormat = "%-10s %-20s %-15s %-12.2f %-12.2f %-10d %-10s%n";
        employees.values().stream()
                .forEach(employee ->
                        System.out.printf(rowFormat,
                                employee.getEmployeeId().toString(),
                                truncateText(employee.getEmployeeName(), 20),
                                employee.getEmployeeDepartment().name(),
                                employee.getEmployeeSalary(),
                                employee.getPerformanceRating(),
                                employee.getYearsOfExperience(),
                                employee.isActive() ? "Active" : "Inactive")
                );

        System.out.println("----------------------------------------------------------------------------------------------------");

        // Additional report information using Stream API operations
        System.out.println("Total Active Employees: " + getActiveEmployeeCount());

        // Calculate and display average salary
        double avgSalary = employees.values().stream()
                .mapToDouble(Employee::getEmployeeSalary)
                .average()
                .orElse(0);
        System.out.printf("Average Salary: $%.2f%n", avgSalary);

        // Calculate and display average performance rating
        double avgRating = employees.values().stream()
                .mapToDouble(Employee::getPerformanceRating)
                .average()
                .orElse(0);
        System.out.printf("Average Performance Rating: %.2f%n", avgRating);

        // Count active employees
        long activeCount = employees.values().stream()
                .filter(Employee::isActive)
                .count();
        System.out.printf("Active Employees: %d (%.1f%%)%n",
                activeCount,
                employees.isEmpty() ? 0 : (activeCount * 100.0 / getActiveEmployeeCount()));

        // Department distribution
        System.out.println("\nDepartment Distribution:");
        employees.values().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Employee::getEmployeeDepartment,
                        java.util.stream.Collectors.counting()
                ))
                .forEach((dept, count) -> System.out.printf("  %s: %d employees%n", dept, count));
    }

    // Helper method to truncate text to a specified length
    private String truncateText(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
}
