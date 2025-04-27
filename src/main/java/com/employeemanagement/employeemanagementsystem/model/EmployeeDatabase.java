package com.employeemanagement.employeemanagementsystem.model;

import com.employeemanagement.employeemanagementsystem.exceptions.EmployeeNotFoundException;
import com.employeemanagement.employeemanagementsystem.exceptions.InvalidDepartmentException;
import com.employeemanagement.employeemanagementsystem.exceptions.InvalidSalaryException;
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

    //Validations

    // Validates Department Value
    private void validateDepartment(EEmployeeDepartment department) throws InvalidDepartmentException {
        if (department == null) {
            throw new InvalidDepartmentException("Department cannot be null");
        }

        // Check if department is one of the valid enum values
        try {
            EEmployeeDepartment.valueOf(department.name());
        } catch (IllegalArgumentException e) {
            throw new InvalidDepartmentException("Invalid department value: " + department +
                    ". Valid departments are: " + Arrays.toString(EEmployeeDepartment.values()));
        }
    }
    //Validates employee ID exists in database
    private void validateEmployeeExists(T employeeId) throws EmployeeNotFoundException {
        if (employeeId == null) {
            throw new EmployeeNotFoundException("Employee ID cannot be null");
        }

        if (!employees.containsKey(employeeId)) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found in database");
        }
    }

    //CRUD Operations

    //Create
    public void addEmployee(Employee<T> employee) throws InvalidSalaryException {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        if(employee.getEmployeeSalary() < 0 ){
            throw new InvalidSalaryException("Employee salary cannot be negative");
        }
        if(employees.containsKey(employee.getEmployeeId())){
            throw new IllegalArgumentException("Employee with ID " + employee.getEmployeeId() + " already exists");
        }
        employees.put(employee.getEmployeeId(), employee);
    }

    //Retrieve All
    public List<Employee<T>> getAllEmployees(){
        return new ArrayList<>(employees.values());
    }

    //Retrieve employee by employeeId
    public Employee<T> getEmployee(T employeeId) throws EmployeeNotFoundException {
        Employee<T> employee = employees.get(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found");
        }
        return employee;
    }

    //Update
    public void updateEmployeeDetails(T employeeId, String field, Object newValue) throws EmployeeNotFoundException, InvalidSalaryException, InvalidDepartmentException {
        //checks if employee exist
        validateEmployeeExists(employeeId);

        Employee<T> employee = employees.get(employeeId);

        try{

            switch (field.toLowerCase()){
                case "employeename":
                    if (newValue == null || ((String) newValue).trim().isEmpty()) {
                        throw new IllegalArgumentException("Employee name cannot be empty");
                    }
                    employee.setEmployeeName((String) newValue);
                    break;
                case "employeedepartment":
                    if (newValue == null) {
                        throw new InvalidDepartmentException("Department cannot be null");
                    }
                    try {
                        EEmployeeDepartment department = (EEmployeeDepartment) newValue;
                        validateDepartment(department);
                        employee.setEmployeeDepartment(department);
                    } catch(ClassCastException e) {
                        throw new InvalidDepartmentException("Invalid department value: " + newValue);
                    }
                    break;
                case "employeesalary":
                    double salary = (Double) newValue;
                    if (salary < 0){
                        throw new InvalidSalaryException("Salary cannot be negative");
                    }
                    employee.setEmployeeSalary(salary);
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
                    throw new IllegalArgumentException("Invalid field name: " + field);
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Invalid value type for field " + field + ": " + newValue +
                    " (" + (newValue != null ? newValue.getClass().getSimpleName() : "null") + ")");
        }
    }

    //Delete
    public void deleteEmployee(T employeeId) throws EmployeeNotFoundException {
        //checks if employee exist
        if(!employees.containsKey(employeeId)){
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found");
        }
        employees.remove(employeeId);
    }


    // Search and filter Operations

    //Finds employees by department
    public List<Employee<T>> findEmployeesByDepartment(String department) throws InvalidDepartmentException{
        if (department == null || department.trim().isEmpty()) {
            throw new InvalidDepartmentException("Department name cannot be empty");
        }

        try {
            // Validate department
            EEmployeeDepartment deptEnum = EEmployeeDepartment.valueOf(department.toUpperCase());

            return employees.values().stream()
                    .filter(Objects::nonNull)
                    .filter(employee -> employee.getEmployeeDepartment() != null &&
                            employee.getEmployeeDepartment().equals(deptEnum))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new InvalidDepartmentException("Invalid department: '" + department + "'. Valid departments are: " +
                    Arrays.toString(EEmployeeDepartment.values()));
        }

    }

    //Finds employees whose names contain the given search term
    public List<Employee<T>> findEmployeesByName(String searchTerm){
        // Validate search term
        if (searchTerm == null) {
            throw new IllegalArgumentException("Search term cannot be null");
        }

        String trimmedTerm = searchTerm.trim();
        if (trimmedTerm.isEmpty()) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        return employees.values().stream()
                .filter(Objects::nonNull)
                .filter(employee -> employee.getEmployeeName() != null &&
                        employee.getEmployeeName().toLowerCase().contains(trimmedTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    //Finds employees with performance rating at or above the specified minimum
    public List<Employee<T>> findEmployeesByMinRating(Double minRating){
        // Null check for rating
        if (minRating == null) {
            throw new IllegalArgumentException("Minimum rating cannot be null");
        }
        if (minRating < 0 || minRating > 5) {
            throw new IllegalArgumentException("Rating must be between 0 and 5, provided: " + minRating);
        }
        return employees.values().stream()
                .filter(Objects::nonNull)
                .filter(employee -> employee.getPerformanceRating() >= minRating)
                .collect(Collectors.toList());
    }

    //Finds employees with salary in the specified range
    public List<Employee<T>> findEmployeesBySalaryRange(double minSalary, double maxSalary) throws InvalidSalaryException{
        // Validate salary range
        if (minSalary < 0) {
            throw new InvalidSalaryException("Minimum salary cannot be negative: " + minSalary);
        }

        if (maxSalary < minSalary) {
            throw new InvalidSalaryException("Maximum salary (" + maxSalary +
                    ") cannot be less than minimum salary (" + minSalary + ")");
        }
        return employees.values().stream()
                .filter(Objects::nonNull)
                .filter(e -> e.getEmployeeSalary() >= minSalary && e.getEmployeeSalary() <= maxSalary)
                .collect(Collectors.toList());
    }


    // Sorting Operations

    //Gets employees sorted by years of experience
    public List <Employee<T>> getEmployeesSortedByExperience(){

        if (employees == null || employees.isEmpty()) {
            return new ArrayList<>();
        }

        List<Employee<T>> sortedList = employees.values().stream()
                .filter(Objects::nonNull)  // Filter out any null employees
                .collect(Collectors.toList());

        // Handle potential null values in the sorting process
        try {
            Collections.sort(sortedList); // Uses the compareTo method in Employee class
        } catch (Exception e) {
            // Log exception
            System.err.println("Error sorting employees by experience: " + e.getMessage());
        }

        return sortedList;
    }

    // Gets employees sorted by salary
    public List<Employee<T>> getEmployeesSortedBySalary() {

        if (employees == null || employees.isEmpty()) {
            return new ArrayList<>();
        }

        List<Employee<T>> sortedList = employees.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Handle potential null values
        try {
            sortedList.sort(new EmployeeSalaryComparator<>());
        } catch (Exception e) {
            System.err.println("Error sorting employees by salary: " + e.getMessage());
            // Already handled by our improved comparator, but keeping for defensive programming
        }

        return sortedList;
    }

    //Gets employees sorted by performance rating
    public List<Employee<T>> getEmployeesSortedByPerformance() {

        if (employees == null || employees.isEmpty()) {
            return new ArrayList<>();
        }

        List<Employee<T>> sortedList = employees.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        try {
            sortedList.sort(new EmployeePerformanceComparator<>());
        } catch (Exception e) {
            System.err.println("Error sorting employees by performance: " + e.getMessage());
            // Already handled by our improved comparator, but keeping for defensive programming
        }

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
    public double calculateAverageSalaryByDepartment(String department) throws InvalidDepartmentException{

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
