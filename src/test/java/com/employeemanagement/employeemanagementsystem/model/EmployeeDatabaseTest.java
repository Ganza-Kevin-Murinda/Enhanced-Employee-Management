package com.employeemanagement.employeemanagementsystem.model;

import com.employeemanagement.employeemanagementsystem.exceptions.EmployeeNotFoundException;
import com.employeemanagement.employeemanagementsystem.exceptions.InvalidDepartmentException;
import com.employeemanagement.employeemanagementsystem.exceptions.InvalidSalaryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the EmployeeDatabase class
 */
class EmployeeDatabaseTest {
    private EmployeeDatabase<Integer> employeeDB;

    // Test employees
    private Employee<Integer> employee1;
    private Employee<Integer> employee2;
    private Employee<Integer> employee3;

    @BeforeEach
    void setUp() {
        // Initialize the database before each test
        employeeDB = new EmployeeDatabase<>();

        // Create test employees
        employee1 = new Employee<>(101, "Kevin Ganza", EEmployeeDepartment.IT, 6000.0, 2.2, 5, true);
        employee2 = new Employee<>(102, "Hyguette Imfura", EEmployeeDepartment.FINANCE, 5500.0, 3.8, 3, true);
        employee3 = new Employee<>(103, "Kelly Gwiza", EEmployeeDepartment.HR, 7000.0, 4.5, 8, true);
    }

    @Test
    @DisplayName("Add valid employee")
    void testAddValidEmployee() throws InvalidSalaryException {
        // Add employee1
        employeeDB.addEmployee(employee1);

        // Verify it was added successfully
        assertEquals(1, employeeDB.getTotalEmployeeCount());

        // Try to get the employee
        try {
            Employee<Integer> retrievedEmployee = employeeDB.getEmployee(101);
            assertEquals("Kevin Ganza", retrievedEmployee.getEmployeeName());
        } catch (EmployeeNotFoundException e) {
            fail("Employee should have been found");
        }
    }

    @Test
    @DisplayName("Add employee with negative salary should throw InvalidSalaryException")
    void testAddEmployeeWithNegativeSalary() {
        Employee<Integer> employee = new Employee<>(102, "Grace Keza", EEmployeeDepartment.FINANCE, -5000.0, 3.5, 5, true);
        // Attempt to add and expect exception
        assertThrows(InvalidSalaryException.class, () -> {
            employeeDB.addEmployee(employee);
        });

        // Verify employee wasn't added
        assertEquals(0, employeeDB.getTotalEmployeeCount());
    }

    @Test
    @DisplayName("Add duplicate employee should throw IllegalArgumentException")
    void testAddDuplicateEmployee() throws InvalidSalaryException {
        // Add employee1
        employeeDB.addEmployee(employee1);

        // Create another employee with the same ID
        Employee<Integer> duplicateEmployee = new Employee<>(101, "Duplicate Employee",
                EEmployeeDepartment.MARKETING, 5000.0, 3.5, 4, true);

        // Attempt to add duplicate and expect exception
        assertThrows(IllegalArgumentException.class, () -> {
            employeeDB.addEmployee(duplicateEmployee);
        });

        // Verify only one employee exists
        assertEquals(1, employeeDB.getTotalEmployeeCount());
    }

    @Test
    @DisplayName("Get non-existent employee should throw EmployeeNotFoundException")
    void testGetNonExistentEmployee() {
        // Attempt to get employee that doesn't exist
        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeDB.getEmployee(999);
        });
    }

    @Test
    @DisplayName("Delete employee successfully")
    void testDeleteEmployee() throws InvalidSalaryException, EmployeeNotFoundException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee3);

        // Verify initial count
        assertEquals(2, employeeDB.getTotalEmployeeCount());

        // Delete one employee
        employeeDB.deleteEmployee(101);

        // Verify count decreased
        assertEquals(1, employeeDB.getTotalEmployeeCount());

        // Verify correct employee remains
        Employee<Integer> remainingEmployee = employeeDB.getEmployee(103);
        assertEquals("Kelly Gwiza", remainingEmployee.getEmployeeName());

        // Verify deleted employee can't be retrieved
        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeDB.getEmployee(101);
        });
    }

    @Test
    @DisplayName("Delete non-existent employee should throw EmployeeNotFoundException")
    void testDeleteNonExistentEmployee() {
        // Attempt to delete employee that doesn't exist
        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeDB.deleteEmployee(999);
        });
    }

    @Test
    @DisplayName("Update employee details successfully")
    void testUpdateEmployeeDetails() throws InvalidSalaryException, EmployeeNotFoundException, InvalidDepartmentException {
        // Add employee
        employeeDB.addEmployee(employee1);

        // Update employee name
        employeeDB.updateEmployeeDetails(101, "employeeName", "Kevin Updated");

        // Update employee salary
        employeeDB.updateEmployeeDetails(101, "employeeSalary", 6500.0);

        // Verify updates
        Employee<Integer> updatedEmployee = employeeDB.getEmployee(101);
        assertEquals("Kevin Updated", updatedEmployee.getEmployeeName());
        assertEquals(6500.0, updatedEmployee.getEmployeeSalary());
    }

    @Test
    @DisplayName("Update employee with negative salary should throw InvalidSalaryException")
    void testUpdateEmployeeWithInvalidSalary() throws InvalidSalaryException {
        // Add employee
        employeeDB.addEmployee(employee1);

        // Attempt to update with negative salary
        assertThrows(InvalidSalaryException.class, () -> {
            employeeDB.updateEmployeeDetails(101, "employeeSalary", -5000.0);
        });

        // Verify salary wasn't updated
        Employee<Integer> employee;
        try {
            employee = employeeDB.getEmployee(101);
            assertEquals(6000.0, employee.getEmployeeSalary());
        } catch (EmployeeNotFoundException e) {
            fail("Employee should exist");
        }
    }

    @Test
    @DisplayName("Update employee with invalid department should throw InvalidDepartmentException")
    void testUpdateEmployeeWithInvalidDepartment() throws InvalidSalaryException {
        // Add employee
        employeeDB.addEmployee(employee3);

        // Attempt to update with null department
        assertThrows(InvalidDepartmentException.class, () -> {
            employeeDB.updateEmployeeDetails(103, "employeeDepartment", null);
        });
    }

    @Test
    @DisplayName("Find employees by department")
    void testFindEmployeesByDepartment() throws InvalidSalaryException, InvalidDepartmentException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Find employees in ENGINEERING department
        List<Employee<Integer>> itEmployees = employeeDB.findEmployeesByDepartment("IT");

        // Verify result
        assertEquals(1, itEmployees.size());
        assertEquals("Kevin Ganza", itEmployees.getFirst().getEmployeeName());
    }

    @Test
    @DisplayName("Find employees by invalid department should throw InvalidDepartmentException")
    void testFindEmployeesByInvalidDepartment() {
        // Attempt to find employees with invalid department
        assertThrows(InvalidDepartmentException.class, () -> {
            employeeDB.findEmployeesByDepartment("ENGINEERING");
        });
    }

    @Test
    @DisplayName("Find employees by name")
    void testFindEmployeesByName() throws InvalidSalaryException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Find employees with "Jo" in their name (should match John and Johnson)
        List<Employee<Integer>> keEmployees = employeeDB.findEmployeesByName("ke");

        // Verify result
        assertEquals(2, keEmployees.size());

        // Verify case insensitivity
        List<Employee<Integer>> gwizaEmployees = employeeDB.findEmployeesByName("gwiza");
        assertEquals(1, gwizaEmployees.size());
        assertEquals("Kelly Gwiza", gwizaEmployees.getFirst().getEmployeeName());
    }

    @Test
    @DisplayName("Find employees by minimum rating")
    void testFindEmployeesByMinRating() throws InvalidSalaryException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Find employees with rating >= 4.0
        List<Employee<Integer>> highRatedEmployees = employeeDB.findEmployeesByMinRating(3.0);

        // Verify result
        assertEquals(2, highRatedEmployees.size());
    }

    @Test
    @DisplayName("Find employees by salary range")
    void testFindEmployeesBySalaryRange() throws InvalidSalaryException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Find employees with salary between 5900 and 7500
        List<Employee<Integer>> midRangeEmployees = employeeDB.findEmployeesBySalaryRange(5900, 7500);

        // Verify result
        assertEquals(2, midRangeEmployees.size());
    }

    @Test
    @DisplayName("Find employees by invalid salary range should throw InvalidSalaryException")
    void testFindEmployeesByInvalidSalaryRange() {
        // Attempt to find employees with negative minimum salary
        assertThrows(InvalidSalaryException.class, () -> {
            employeeDB.findEmployeesBySalaryRange(-5000, 5000);
        });

        // Attempt to find employees with min > max
        assertThrows(InvalidSalaryException.class, () -> {
            employeeDB.findEmployeesBySalaryRange(6000, 5000);
        });
    }

    @Test
    @DisplayName("Get employees sorted by experience")
    void testGetEmployeesSortedByExperience() throws InvalidSalaryException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Get sorted by experience (highest first)
        List<Employee<Integer>> sortedEmployees = employeeDB.getEmployeesSortedByExperience();

        // Verify order
        assertEquals(3, sortedEmployees.size());
        assertEquals(103, sortedEmployees.get(0).getEmployeeId());
        assertEquals(101, sortedEmployees.get(1).getEmployeeId());
        assertEquals(102, sortedEmployees.get(2).getEmployeeId());
    }

    @Test
    @DisplayName("Get employees sorted by salary")
    void testGetEmployeesSortedBySalary() throws InvalidSalaryException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Get sorted by salary (highest first)
        List<Employee<Integer>> sortedEmployees = employeeDB.getEmployeesSortedBySalary();

        // Verify order
        assertEquals(3, sortedEmployees.size());
        assertEquals(103, sortedEmployees.get(0).getEmployeeId());
        assertEquals(101, sortedEmployees.get(1).getEmployeeId());
        assertEquals(102, sortedEmployees.get(2).getEmployeeId());
    }

    @Test
    @DisplayName("Get employees sorted by performance")
    void testGetEmployeesSortedByPerformance() throws InvalidSalaryException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Get sorted by performance (highest first)
        List<Employee<Integer>> sortedEmployees = employeeDB.getEmployeesSortedByPerformance();

        // Verify order
        assertEquals(3, sortedEmployees.size());
        assertEquals(103, sortedEmployees.get(0).getEmployeeId());
        assertEquals(102, sortedEmployees.get(1).getEmployeeId());
        assertEquals(101, sortedEmployees.get(2).getEmployeeId());
    }

    @Test
    @DisplayName("Empty database edge cases for sorting and searching")
    void testEmptyDatabaseOperations() throws InvalidDepartmentException, InvalidSalaryException {
        // Test on empty database

        // Get all employees
        List<Employee<Integer>> allEmployees = employeeDB.getAllEmployees();
        assertTrue(allEmployees.isEmpty());

        // Get sorted employees
        List<Employee<Integer>> sortedByExp = employeeDB.getEmployeesSortedByExperience();
        assertTrue(sortedByExp.isEmpty());

        List<Employee<Integer>> sortedBySalary = employeeDB.getEmployeesSortedBySalary();
        assertTrue(sortedBySalary.isEmpty());

        List<Employee<Integer>> sortedByPerformance = employeeDB.getEmployeesSortedByPerformance();
        assertTrue(sortedByPerformance.isEmpty());

        // Find employees
        List<Employee<Integer>> byDept = employeeDB.findEmployeesByDepartment("IT");
        assertTrue(byDept.isEmpty());

        List<Employee<Integer>> byName = employeeDB.findEmployeesByName("Kelly");
        assertTrue(byName.isEmpty());

        List<Employee<Integer>> byRating = employeeDB.findEmployeesByMinRating(4.0);
        assertTrue(byRating.isEmpty());

        List<Employee<Integer>> bySalary = employeeDB.findEmployeesBySalaryRange(5000, 7000);
        assertTrue(bySalary.isEmpty());
    }

    @Test
    @DisplayName("Give salary raise to high performers")
    void testGiveSalaryRaiseToHighPerformers() throws InvalidSalaryException, EmployeeNotFoundException {
        // Add employees
        employeeDB.addEmployee(employee1); // Rating 2.2
        employeeDB.addEmployee(employee2); // Rating 3.8
        employeeDB.addEmployee(employee3); // Rating 4.5

        // Give raises (2% to employees with rating >= 3.5)
        int raisedCount = employeeDB.giveSalaryRaiseToHighPerformers();

        // Verify count
        assertEquals(2, raisedCount);

        // Verify new salaries (original * 1.02)
        assertEquals(5610.0, employeeDB.getEmployee(102).getEmployeeSalary());
        assertEquals(7140.0, employeeDB.getEmployee(103).getEmployeeSalary());
    }

    @Test
    @DisplayName("Calculate average salary by department")
    void testCalculateAverageSalaryByDepartment() throws InvalidSalaryException, InvalidDepartmentException {
        // Add employees
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Add another employee in IT
        Employee<Integer> employee4 = new Employee<>(104, "Another IT",
                EEmployeeDepartment.IT, 8000.0, 3.9, 6, true);
        employeeDB.addEmployee(employee4);

        // Calculate averages
        double itAvg = employeeDB.calculateAverageSalaryByDepartment("IT");
        double financeAvg = employeeDB.calculateAverageSalaryByDepartment("FINANCE");

        // Verify averages
        assertEquals(7000.0, itAvg); // (6000 + 8000) / 2
        assertEquals(5500.0, financeAvg);

        // Test department with no employees
        double marketingAvg = employeeDB.calculateAverageSalaryByDepartment("MARKETING");
        assertEquals(0.0, marketingAvg);
    }

    @Test
    @DisplayName("Get top paid employees")
    void testGetTopPaidEmployees() throws InvalidSalaryException {
        // Add employees
        employeeDB.addEmployee(employee1); // $6000
        employeeDB.addEmployee(employee2); // $5500
        employeeDB.addEmployee(employee3); // $7000

        // Get top 2 paid employees
        List<Employee<Integer>> topPaid = employeeDB.getTopPaidEmployees(2);

        // Verify result
        assertEquals(2, topPaid.size());
        assertEquals(103, topPaid.get(0).getEmployeeId());
        assertEquals(101, topPaid.get(1).getEmployeeId());
    }

    @Test
    @DisplayName("Get active employee count")
    void testGetActiveEmployeeCount() throws InvalidSalaryException, EmployeeNotFoundException, InvalidDepartmentException {
        // Add employees - all active initially
        employeeDB.addEmployee(employee1);
        employeeDB.addEmployee(employee2);
        employeeDB.addEmployee(employee3);

        // Verify all active
        assertEquals(3, employeeDB.getActiveEmployeeCount());

        // Set one employee to inactive
        employeeDB.updateEmployeeDetails(102, "active", false);

        // Verify active count decreased
        assertEquals(2, employeeDB.getActiveEmployeeCount());
    }

}