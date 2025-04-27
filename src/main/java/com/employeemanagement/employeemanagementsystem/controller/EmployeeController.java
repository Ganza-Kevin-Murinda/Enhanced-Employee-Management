package com.employeemanagement.employeemanagementsystem.controller;

import com.employeemanagement.employeemanagementsystem.exceptions.EmployeeNotFoundException;
import com.employeemanagement.employeemanagementsystem.exceptions.InvalidDepartmentException;
import com.employeemanagement.employeemanagementsystem.exceptions.InvalidSalaryException;
import com.employeemanagement.employeemanagementsystem.model.Employee;
import com.employeemanagement.employeemanagementsystem.model.EmployeeDatabase;
import com.employeemanagement.employeemanagementsystem.model.EEmployeeDepartment;
import com.employeemanagement.employeemanagementsystem.view.MainView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.Arrays;
import java.util.List;

/**
 * Controller class that handles interaction between views and the employee database
 */
public class EmployeeController {

    private EmployeeDatabase<Integer> employeeDB;
    private MainView mainView;

    //Constructor
    public EmployeeController(EmployeeDatabase<Integer> employeeDB, MainView mainView) {
        this.employeeDB = employeeDB;
        this.mainView = mainView;

        // Initialize the table with data
        refreshEmployeeTable();

        // Generate an initial ID
        generateAndDisplayNewId();

        // Set up event handlers
        setupEventHandlers();
    }

    //Set up all event handlers for the UI components
    private void setupEventHandlers() {
        // Form view event handlers
        mainView.getFormView().getAddButton().setOnAction(e -> addEmployee());
        mainView.getFormView().getUpdateButton().setOnAction(e -> updateEmployee());
        mainView.getFormView().getDeleteButton().setOnAction(e -> deleteEmployee());
        mainView.getFormView().getClearButton().setOnAction(e -> {
            mainView.getFormView().clearForm();
            generateAndDisplayNewId();
        });

        // Add event handler for ID field focus
        mainView.getFormView().getIdField().setOnMouseClicked(e -> {
            // Only generate new ID if the field is empty (to avoid overwriting during edit)
            if (mainView.getFormView().getIdField().getText().isEmpty()) {
                generateAndDisplayNewId();
            }
        });

        // Search view event handlers
        mainView.getSearchView().getSearchButton().setOnAction(e -> searchEmployees());
        mainView.getSearchView().getSortButton().setOnAction(e -> sortEmployees());
        mainView.getSearchView().getResetButton().setOnAction(e -> refreshEmployeeTable());
        mainView.getSearchView().getRefreshButton().setOnAction(e -> refreshEmployeeTable());

        // Table selection event handler
        mainView.getTableView().getEmployeeTable().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mainView.getFormView().displayEmployee(newSelection);
            }
        });

        // Salary management event handlers
        mainView.getSalaryManagementView().getSalaryRaiseButton().setOnAction(e -> giveRaisesToHighPerformers());
        mainView.getSalaryManagementView().getTopPaidButton().setOnAction(e -> showTopPaidEmployees());
        mainView.getSalaryManagementView().getAvgSalaryButton().setOnAction(e -> calculateAverageSalary());

        // Initialize department dropdown in salary management view
        mainView.getSalaryManagementView().populateDepartments(getDepartmentNames());

        // Console report button handler
        mainView.getTableView().getConsoleReportButton().setOnAction(e -> printEmployeeReportsToConsole());
    }

    //Refreshes the employee table with all employees
    public void refreshEmployeeTable() {
        List<Employee<Integer>> employees = employeeDB.getAllEmployees();
        mainView.getTableView().updateEmployeeTable(employees);
        mainView.getFormView().clearForm();
    }

    //Generates a unique random Id
    private Integer generateRandomId() {
        // Generate a random ID between 1000 and 9999
        int min = 1000;
        int max = 9999;
        return (int) (Math.random() * (max - min + 1) + min);
    }

    // Add a method to generate and display a new ID
    private void generateAndDisplayNewId() {
        Integer newId = generateRandomId();
        mainView.getFormView().getIdField().setText(newId.toString());
    }

    //Adds a new employee from the form data
    private void addEmployee() {
        try {
            // Parse form data
            Integer id = Integer.parseInt(mainView.getFormView().getIdField().getText());
            String name = mainView.getFormView().getNameField().getText();
            String departmentStr = mainView.getFormView().getDepartmentComboBox().getValue();
            Double salary = Double.parseDouble(mainView.getFormView().getSalaryField().getText());
            double rating = Double.parseDouble(mainView.getFormView().getRatingField().getText());
            int experience = Integer.parseInt(mainView.getFormView().getExperienceField().getText());
            boolean active = mainView.getFormView().getActiveCheckBox().isSelected();

            // Validate input
            if (name.isEmpty()) {
                showAlert("Error", "Invalid Input","Field Name cannot be empty!");
                return;
            }
            if (departmentStr == null) {
                showAlert("Error", "Invalid Input","Please select a department!");
                return;
            }
            EEmployeeDepartment department;
            try {
                department = EEmployeeDepartment.valueOf(departmentStr);
            } catch (IllegalArgumentException e) {
                showAlert("Error", "Department Error","Invalid department selection!");
                return;
            }

            if (rating < 0 || rating > 5) {
                showAlert("Error", "Rating Error","Rating must be between 0 and 5!");
                return;
            }

            if (experience < 0 || experience > 60) {
                showAlert("Error", "Invalid Input","Be Honest About Your Experience!");
                return;
            }

            // Create and add employee
            Employee<Integer> employee = new Employee<>(id, name, department, salary, rating, experience, active);

            try {
                employeeDB.addEmployee(employee);
                mainView.getFormView().clearForm();
                refreshEmployeeTable();
                showAlert("info", "Success","Employee added successfully!");
            } catch (InvalidSalaryException | IllegalArgumentException e) {
                showAlert("Error","System Error", e.getMessage());
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Input","All Fields are required!!\nAlso ensure fields like Salary, Years Of Experience and Rating contain valid numeric values!");
        }
    }

    //Updates an existing employee with form data
    private void updateEmployee() {
        try {
            // Get selected employee
            Employee<Integer> selectedEmployee = mainView.getTableView().getEmployeeTable().getSelectionModel().getSelectedItem();
            if (selectedEmployee == null) {
                showAlert("Error", "Invalid Input","Please select an employee to update!");
                return;
            }

            // Parse form data
            Integer id = Integer.parseInt(mainView.getFormView().getIdField().getText());

            // Ensure we're updating the same employee
            if (!id.equals(selectedEmployee.getEmployeeId())) {
                showAlert("Error", "Invalid Input","Cannot change employee ID!");
                return;
            }

            // Update all fields
            String name = mainView.getFormView().getNameField().getText();
            String departmentStr = mainView.getFormView().getDepartmentComboBox().getValue();
            double salary = Double.parseDouble(mainView.getFormView().getSalaryField().getText());
            double rating = Double.parseDouble(mainView.getFormView().getRatingField().getText());
            int experience = Integer.parseInt(mainView.getFormView().getExperienceField().getText());
            boolean active = mainView.getFormView().getActiveCheckBox().isSelected();

            // Validate input
            if (name.isEmpty()) {
                showAlert("Error", "Invalid Input","Please fill all fields!");
                return;
            }

            if (departmentStr.equals("Select Department")) {
                showAlert("Error", "Invalid Input","Please select a department!");
                return;
            }
            EEmployeeDepartment department;
            try {
                department = EEmployeeDepartment.valueOf(departmentStr);
            } catch (IllegalArgumentException e) {
                showAlert("Error", "Invalid Input","Invalid department selection!");
                return;
            }

            if (rating < 0 || rating > 5) {
                showAlert("Error", "Invalid Input","Rating must be between 0 and 5!");
                return;
            }

            if (experience < 0 || experience > 60) {
                showAlert("Error", "Invalid Input","Be Honest About Your Experience!");
                return;
            }

            try {
                // Update employee using updateEmployeeDetails method for each field
                employeeDB.updateEmployeeDetails(id, "employeeName", name);
                employeeDB.updateEmployeeDetails(id, "employeeDepartment", department);
                employeeDB.updateEmployeeDetails(id, "employeeSalary", salary);
                employeeDB.updateEmployeeDetails(id, "performanceRating", rating);
                employeeDB.updateEmployeeDetails(id, "yearOfExperience", experience);
                employeeDB.updateEmployeeDetails(id, "active", active);

                mainView.getFormView().clearForm();
                refreshEmployeeTable();
                showAlert("info", "Success","Employee updated successfully!");
            } catch (EmployeeNotFoundException | InvalidSalaryException | InvalidDepartmentException e) {
                showAlert("Error","System Error", e.getMessage());
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Input","Please ensure fields like Salary, Years Of Experience and Rating contain valid numeric values!");
        }
    }

    //Deletes the selected employee
    private void deleteEmployee() {
        Employee<Integer> selectedEmployee = mainView.getTableView().getEmployeeTable().getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Error", "Invalid Input","Please select an employee to delete!");
            return;
        }

        try {
            employeeDB.deleteEmployee(selectedEmployee.getEmployeeId());
            mainView.getFormView().clearForm();
            refreshEmployeeTable();
            showAlert("info", "Success","Employee deleted successfully!");
        } catch (EmployeeNotFoundException e) {
            showAlert("Error","System Error", e.getMessage());
        }
    }

    //Searches for employees based on the criteria
    private void searchEmployees() {
        String searchType = mainView.getSearchView().getSearchTypeComboBox().getValue();
        String searchTerm = mainView.getSearchView().getSearchField().getText();

        if (searchType == null || searchType.isEmpty()) {
            showAlert("error", "Invalid Input", "Please select a search type!");
            return;
        }

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            showAlert("error", "Invalid Input", "Please enter a search term!");
            return;
        }

        List<Employee<Integer>> results;

        try {
        switch (searchType) {
            case "Name":
                results = employeeDB.findEmployeesByName(searchTerm);
                break;
            case "Department":
                try {
                    results = employeeDB.findEmployeesByDepartment(searchTerm);
                } catch (InvalidDepartmentException e) {
                    showAlert("error", "Department Error", e.getMessage());
                    return;
                }
                break;
            case "Minimum Rating":
                try {
                    double minRating = Double.parseDouble(searchTerm);
                    if (minRating < 0 || minRating > 5) {
                        showAlert("error", "Rating Error", "Rating must be between 0 and 5!");
                        return;
                    }
                    results = employeeDB.findEmployeesByMinRating(minRating);
                } catch (NumberFormatException e) {
                    showAlert("error", "Invalid Input", "Please enter a valid number for rating (0-5)!");
                    return;
                }
                break;
            case "Salary Range":
                try {
                    String[] range = searchTerm.split("-");
                    if (range.length != 2) {
                        showAlert("error", "Format Error", "Salary range should be in format: min-max (Example: 30000-50000)");
                        return;
                    }

                    String minStr = range[0].trim();
                    String maxStr = range[1].trim();

                    if (minStr.isEmpty() || maxStr.isEmpty()) {
                        showAlert("error", "Format Error", "Both minimum and maximum values must be provided!");
                        return;
                    }

                    double minSalary = Double.parseDouble(minStr);
                    double maxSalary = Double.parseDouble(maxStr);

                    if (minSalary < 0) {
                        showAlert("error", "Salary Error", "Minimum salary cannot be negative!");
                        return;
                    }

                    if (maxSalary < minSalary) {
                        showAlert("error", "Salary Error", "Maximum salary cannot be less than minimum salary!");
                        return;
                    }

                    results = employeeDB.findEmployeesBySalaryRange(minSalary, maxSalary);
                } catch (NumberFormatException e) {
                    showAlert("error", "Invalid Input", "Please enter valid numbers for salary range!");
                    return;
                } catch (InvalidSalaryException e) {
                    showAlert("error", "Salary Error", e.getMessage());
                    return;
                }
                break;
            default:
                showAlert("error", "Configuration Error", "Invalid search type: " + searchType);
                return;
        }
            // Update table with search results
            mainView.getSearchView().clearForm();
            mainView.getTableView().updateEmployeeTable(results);

            // Show message if no results found
            if (results.isEmpty()) {
                showAlert("info", "Search Results", "No matching employees found.");
            }

        } catch (Exception e) {
            showAlert("error", "System Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    //Sorts employees based on the selected criteria
    private void sortEmployees() {
        String sortType = mainView.getSearchView().getSortComboBox().getValue();
        List<Employee<Integer>> sortedList;

        switch (sortType) {
            case "Experience":
                sortedList = employeeDB.getEmployeesSortedByExperience();
                break;
            case "Salary":
                sortedList = employeeDB.getEmployeesSortedBySalary();
                break;
            case "Performance":
                sortedList = employeeDB.getEmployeesSortedByPerformance();
                break;
            default:
                showAlert("Error", "Sort Error","Invalid sort type!");
                return;
        }

        // Update table with sorted results
        ObservableList<Employee<Integer>> employeeList = FXCollections.observableArrayList(sortedList);
        mainView.getTableView().updateEmployeeTable(sortedList);
    }

    //Shows an alert dialog
    private void showAlert(String type, String title, String message) {
        AlertType alertType = type.equalsIgnoreCase("error") ? AlertType.ERROR : AlertType.INFORMATION;
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //Gets all department names as strings
    private String[] getDepartmentNames() {
        return Arrays.stream(EEmployeeDepartment.values())
                .map(Enum::name)
                .toArray(String[]::new);
    }

    // Implement the salary management methods
    private void giveRaisesToHighPerformers() {
        int count = employeeDB.giveSalaryRaiseToHighPerformers();
        if (count > 0) {
            refreshEmployeeTable();
            showAlert("info","Salary Raise", count + " employee(s) with 3.5 and above high performance received a salary raise of 2%.");
        }else {
            showAlert("info","Salary Raise", "No employee with high performance Found!");
        }

    }

    private void showTopPaidEmployees() {
        try {
            int topN = Integer.parseInt(mainView.getSalaryManagementView().getTopNField().getText());
            if (topN <= 0) {
                showAlert("Error", "Invalid Input","Please enter a positive number for top employees.");
                return;
            }

            List<Employee<Integer>> topPaid = employeeDB.getTopPaidEmployees(topN);

            // Display the top paid employees in the table
            mainView.getTableView().updateEmployeeTable(topPaid);

            if (topPaid.isEmpty()) {
                showAlert("info","Top Paid Employees", "No employees found.");
            } else {
                showAlert("info","Top Paid Employees", "Displaying top " + topPaid.size() + " highest paid employees.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid Input","Please enter a valid number.");
        }
    }

    private void calculateAverageSalary() {
        String department = mainView.getSalaryManagementView().getDepartmentComboBox().getValue();
        if (department == null) {
            showAlert("Error", "Invalid Input","Please select a department.");
            return;
        }

        try {
            double avgSalary = employeeDB.calculateAverageSalaryByDepartment(department);

            if (avgSalary >= 0) {
                showAlert("info","Average Salary",
                        "The average salary in the " + department + " department is: $" + String.format("%.2f", avgSalary));
            } else {
                showAlert("info","Average Salary", "No employees found in the " + department + " department.");
            }
        } catch (InvalidDepartmentException e) {
            showAlert("Error","Invalid Input", e.getMessage());
        }
    }

    // Prints employee reports to the console
    private void printEmployeeReportsToConsole() {
        // Check if there are employees to display
        if (employeeDB.getTotalEmployeeCount() == 0) {
            showAlert("info","No Employees", "There are no employees to display in the console.");
            return;
        }

        // Print reports to console
        System.out.println("\n========== EMPLOYEE MANAGEMENT SYSTEM REPORTS ==========");
        System.out.println("Report generated at: " + java.time.LocalDateTime.now());
        System.out.println("\n--- Report using For-Each Loop ---");
        employeeDB.displayEmployeesWithForEach();

        System.out.println("\n--- Report using Stream API ---");
        employeeDB.displayEmployeesWithStreams();

        // Show confirmation dialog
        showAlert("info","Console Report", "Employee reports have been printed to the console.");
    }

}
