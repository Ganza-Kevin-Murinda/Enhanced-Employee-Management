package com.employeemanagement.employeemanagementsystem.model.comparator;

import com.employeemanagement.employeemanagementsystem.model.Employee;

import java.util.Comparator;

/**
 * Comparator to sort employees by performance rating (highest first)
 */

public class EmployeePerformanceComparator<T> implements Comparator<Employee<T>> {
    @Override
    public int compare(Employee<T> employee1, Employee<T> employee2) {
        // Comparing in reverse order (highest rating first)
        return Double.compare(employee2.getPerformanceRating(), employee1.getPerformanceRating());

    }
}
