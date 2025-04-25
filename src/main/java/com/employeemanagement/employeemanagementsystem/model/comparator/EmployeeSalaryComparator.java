package com.employeemanagement.employeemanagementsystem.model.comparator;

import com.employeemanagement.employeemanagementsystem.model.Employee;

import java.util.Comparator;

/**
 * Comparator to sort employees by salary (highest first)
 */

public class EmployeeSalaryComparator<T> implements Comparator<Employee<T>> {
    @Override
    public int compare(Employee<T> employee1, Employee<T> employee2) {
        // Comparing in reverse order (highest first)
        return Double.compare(employee2.getEmployeeSalary(), employee1.getEmployeeSalary());
    }
}
