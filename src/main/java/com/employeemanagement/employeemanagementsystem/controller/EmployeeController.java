package com.employeemanagement.employeemanagementsystem.controller;

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
            double salary = Double.parseDouble(mainView.getFormView().getSalaryField().getText());
            double rating = Double.parseDouble(mainView.getFormView().getRatingField().getText());
            int experience = Integer.parseInt(mainView.getFormView().getExperienceField().getText());
            boolean active = mainView.getFormView().getActiveCheckBox().isSelected();

            // Validate input
            if (name.isEmpty()) {
                showAlert("Error", "Please fill all fields!");
                return;
            }
            if (departmentStr == null) {
                showAlert("Error", "Please select a department!");
                return;
            }
            EEmployeeDepartment department = EEmployeeDepartment.valueOf(departmentStr);

            if (rating < 0 || rating > 5) {
                showAlert("Error", "Rating must be between 0 and 5!");
                return;
            }

            // Create and add employee
            Employee<Integer> employee = new Employee<>(id, name, department, salary, rating, experience, active);
            boolean success = employeeDB.addEmployee(employee);

            if (success) {
                mainView.getFormView().clearForm();
                refreshEmployeeTable();
                showAlert("Success", "Employee added successfully!");
            } else {
                showAlert("Error", "Employee with this ID already exists!");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Please Fields like Salary, Years Of Experience and Rating only support numeric values!");
        }
    }

    //Updates an existing employee with form data
    private void updateEmployee() {
        try {
            // Get selected employee
            Employee<Integer> selectedEmployee = mainView.getTableView().getEmployeeTable().getSelectionModel().getSelectedItem();
            if (selectedEmployee == null) {
                showAlert("Error", "Please select an employee to update!");
                return;
            }

            // Parse form data
            Integer id = Integer.parseInt(mainView.getFormView().getIdField().getText());

            // Ensure we're updating the same employee
            if (!id.equals(selectedEmployee.getEmployeeId())) {
                showAlert("Error", "Cannot change employee ID!");
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
                showAlert("Error", "Please fill all fields!");
                return;
            }

            if (departmentStr.equals("Select Department")) {
                showAlert("Error", "Please select a department!");
                return;
            }
            EEmployeeDepartment department = EEmployeeDepartment.valueOf(departmentStr);

            if (rating < 0 || rating > 5) {
                showAlert("Error", "Rating must be between 0 and 5!");
                return;
            }


            // Update employee using updateEmployeeDetails method for each field
            employeeDB.updateEmployeeDetails(id, "employeeName", name);
            employeeDB.updateEmployeeDetails(id, "employeeDepartment", department);
            employeeDB.updateEmployeeDetails(id, "employeeSalary", salary);
            employeeDB.updateEmployeeDetails(id, "performanceRating", rating);
            employeeDB.updateEmployeeDetails(id, "yearOfExperience", experience);
            employeeDB.updateEmployeeDetails(id, "active", active);

            mainView.getFormView().clearForm();
            refreshEmployeeTable();
            showAlert("Success", "Employee updated successfully!");

        } catch (NumberFormatException e) {
            showAlert("Error", "Please Fields like Salary, Years Of Experience and Rating only support numeric values!");
        }
    }

    //Deletes the selected employee
    private void deleteEmployee() {
        Employee<Integer> selectedEmployee = mainView.getTableView().getEmployeeTable().getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Error", "Please select an employee to delete!");
            return;
        }

        boolean success = employeeDB.deleteEmployee(selectedEmployee.getEmployeeId());
        if (success) {
            mainView.getFormView().clearForm();
            refreshEmployeeTable();
            showAlert("Success", "Employee deleted successfully!");
        } else {
            showAlert("Error", "Could not delete employee!");
        }
    }

    //Searches for employees based on the criteria
    private void searchEmployees() {
        String searchType = mainView.getSearchView().getSearchTypeComboBox().getValue();
        String searchTerm = mainView.getSearchView().getSearchField().getText();

        if (searchTerm.isEmpty()) {
            showAlert("Error", "Please enter a search term!");
            return;
        }

        List<Employee<Integer>> results;

        switch (searchType) {
            case "Name":
                results = employeeDB.findEmployeesByName(searchTerm);
                break;
            case "Department":
                try {
                    results = employeeDB.findEmployeesByDepartment(searchTerm);
                } catch (IllegalArgumentException e) {
                    showAlert("Error", "Invalid department! Valid departments are: " +
                            String.join(", ", getDepartmentNames()));
                    return;
                }
                break;
            case "Minimum Rating":
                try {
                    double minRating = Double.parseDouble(searchTerm);
                    results = employeeDB.findEmployeesByMinRating(minRating);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid rating number!");
                    return;
                }
                break;
            case "Salary Range":
                try {
                    String[] range = searchTerm.split("-");
                    if (range.length != 2) {
                        showAlert("Error", "Salary range should be in format: min-max");
                        return;
                    }
                    double minSalary = Double.parseDouble(range[0].trim());
                    double maxSalary = Double.parseDouble(range[1].trim());
                    results = employeeDB.findEmployeesBySalaryRange(minSalary, maxSalary);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter valid salary numbers!");
                    return;
                }
                break;
            default:
                showAlert("Error", "Invalid search type!");
                return;
        }
        // Update table with search results
        ObservableList<Employee<Integer>> employeeList = FXCollections.observableArrayList(results);
        mainView.getSearchView().clearForm();
        mainView.getTableView().updateEmployeeTable(results);
    }

    //Sorts employees based on the selected criteria
    private void sortEmployees() {
        String sortType = mainView.getSearchView().getSortComboBox().getValue();
        List<Employee<Integer>> sortedList;

        switch (sortType) {
            case "Experience (Default)":
                sortedList = employeeDB.getEmployeesSortedByExperience();
                break;
            case "Salary":
                sortedList = employeeDB.getEmployeesSortedBySalary();
                break;
            case "Performance":
                sortedList = employeeDB.getEmployeesSortedByPerformance();
                break;
            default:
                showAlert("Error", "Invalid sort type!");
                return;
        }

        // Update table with sorted results
        ObservableList<Employee<Integer>> employeeList = FXCollections.observableArrayList(sortedList);
        mainView.getTableView().updateEmployeeTable(sortedList);
    }

    //Shows an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
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
            showAlert("Salary Raise", count + " employee(s) with 3.5 and above high performance received a salary raise of 2%.");
        }else {
            showAlert("Salary Raise", "No employee with high performance Found!");
        }

    }

    private void showTopPaidEmployees() {
        try {
            int topN = Integer.parseInt(mainView.getSalaryManagementView().getTopNField().getText());
            if (topN <= 0) {
                showAlert("Error", "Please enter a positive number for top employees.");
                return;
            }

            List<Employee<Integer>> topPaid = employeeDB.getTopPaidEmployees(topN);

            // Display the top paid employees in the table
            mainView.getTableView().updateEmployeeTable(topPaid);

            if (topPaid.isEmpty()) {
                showAlert("Top Paid Employees", "No employees found.");
            } else {
                showAlert("Top Paid Employees", "Displaying top " + topPaid.size() + " highest paid employees.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number.");
        }
    }

    private void calculateAverageSalary() {
        String department = mainView.getSalaryManagementView().getDepartmentComboBox().getValue();
        if (department == null) {
            showAlert("Error", "Please select a department.");
            return;
        }

        double avgSalary = employeeDB.calculateAverageSalaryByDepartment(department);

        if (avgSalary > 0) {
            showAlert("Average Salary",
                    "The average salary in the " + department + " department is: $" + String.format("%.2f", avgSalary));
        } else {
            showAlert("Average Salary", "No employees found in the " + department + " department.");
        }
    }

    // Prints employee reports to the console
    private void printEmployeeReportsToConsole() {
        // Check if there are employees to display
        if (employeeDB.getTotalEmployeeCount() == 0) {
            showAlert("No Employees", "There are no employees to display in the console.");
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
        showAlert("Console Report", "Employee reports have been printed to the console.");
    }

}
