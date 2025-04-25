package com.employeemanagement.employeemanagementsystem.view;

import com.employeemanagement.employeemanagementsystem.model.EEmployeeDepartment;
import com.employeemanagement.employeemanagementsystem.model.Employee;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;

/**
 * JavaFX view component for adding/editing employee details
 */

public class EmployeeFormView extends VBox {

    private TextField idField;
    private TextField nameField;
    private ComboBox<String> departmentComboBox;
    private TextField salaryField;
    private TextField ratingField;
    private TextField experienceField;
    private CheckBox activeCheckBox;

    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button clearButton;

    // Constructor
    public EmployeeFormView() {
        setPadding(new Insets(20));
        setSpacing(15);
        setPrefWidth(400);
        setMinWidth(300);
        setupForm();
    }

    private void setupForm() {

        // Create a styled title
        Label titleLabel = new Label("Employee Details");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Create a grid with better spacing
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(10, 0, 20, 0));

        // Add these column constraints
        ColumnConstraints labelColumn = new ColumnConstraints();
        labelColumn.setPercentWidth(30);

        ColumnConstraints fieldColumn = new ColumnConstraints();
        fieldColumn.setPercentWidth(70);
        fieldColumn.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(labelColumn, fieldColumn);

        // Create styled form fields
        String fieldStyle = "-fx-background-radius: 5; -fx-border-radius: 5;";

        idField = new TextField();
        idField.setPromptText("Employee ID");
        idField.setEditable(false);
        idField.setStyle(fieldStyle + "-fx-background-color: #f0f0f0;");

        nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setMaxWidth(Double.MAX_VALUE);
        nameField.setStyle(fieldStyle);

        departmentComboBox = new ComboBox<>();
        for (EEmployeeDepartment dept : EEmployeeDepartment.values()) {
            departmentComboBox.getItems().add(dept.name());
        }
        departmentComboBox.setPromptText("Select Department");
        departmentComboBox.setPrefWidth(Double.MAX_VALUE);
        departmentComboBox.setStyle(fieldStyle);

        salaryField = new TextField();
        salaryField.setPromptText("Salary");
        salaryField.setStyle(fieldStyle);

        ratingField = new TextField();
        ratingField.setPromptText("Performance Rating (0-5)");
        ratingField.setStyle(fieldStyle);

        experienceField = new TextField();
        experienceField.setPromptText("Years of Experience");
        experienceField.setStyle(fieldStyle);

        activeCheckBox = new CheckBox("Active Employee");
        activeCheckBox.setSelected(true);
        activeCheckBox.setStyle("-fx-text-fill: #2c3e50;");

        // Style labels
        String labelStyle = "-fx-font-weight: bold; -fx-text-fill: #2c3e50;";

        Label idLabel = new Label("Employee ID:");
        idLabel.setStyle(labelStyle);

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle(labelStyle);

        Label deptLabel = new Label("Department:");
        deptLabel.setStyle(labelStyle);

        Label salaryLabel = new Label("Salary:");
        salaryLabel.setStyle(labelStyle);

        Label ratingLabel = new Label("Rating:");
        ratingLabel.setStyle(labelStyle);

        Label expLabel = new Label("Experience:");
        expLabel.setStyle(labelStyle);

        // Add fields to grid
        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(deptLabel, 0, 2);
        grid.add(departmentComboBox, 1, 2);
        grid.add(salaryLabel, 0, 3);
        grid.add(salaryField, 1, 3);
        grid.add(ratingLabel, 0, 4);
        grid.add(ratingField, 1, 4);
        grid.add(expLabel, 0, 5);
        grid.add(experienceField, 1, 5);
        grid.add(activeCheckBox, 0, 6, 2, 1);

        // Create styled buttons
        addButton = new Button("Add Employee");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        addButton.setPrefWidth(140);

        updateButton = new Button("Update Employee");
        updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        updateButton.setPrefWidth(140);

        deleteButton = new Button("Delete Employee");
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        deleteButton.setPrefWidth(140);

        clearButton = new Button("Clear Form");
        clearButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        clearButton.setPrefWidth(140);

        // Create hover effects for buttons
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #43A047; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));

        updateButton.setOnMouseEntered(e -> updateButton.setStyle("-fx-background-color: #1E88E5; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
        updateButton.setOnMouseExited(e -> updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));

        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));

        clearButton.setOnMouseEntered(e -> clearButton.setStyle("-fx-background-color: #757575; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
        clearButton.setOnMouseExited(e -> clearButton.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));

        // Add buttons to VBox with spacing
        VBox buttonBox = new VBox(10);
        HBox buttonBox1 = new HBox(10);
        buttonBox1.getChildren().addAll(addButton, updateButton);

        HBox buttonBox2 = new HBox(10);
        buttonBox2.getChildren().addAll(deleteButton, clearButton);

        buttonBox.getChildren().addAll(buttonBox1, buttonBox2);

        // Add all components to the form
        getChildren().addAll(titleLabel, grid, buttonBox);
    }

    public void displayEmployee(Employee<Integer> employee) {
        if (employee != null) {
            idField.setText(employee.getEmployeeId().toString());
            nameField.setText(employee.getEmployeeName());
            departmentComboBox.setValue(employee.getEmployeeDepartment().name());
            salaryField.setText(String.valueOf(employee.getEmployeeSalary()));
            ratingField.setText(String.valueOf(employee.getPerformanceRating()));
            experienceField.setText(String.valueOf(employee.getYearsOfExperience()));
            activeCheckBox.setSelected(employee.isActive());
        }
    }

    public void clearForm() {
        idField.clear();
        nameField.clear();
        departmentComboBox.setValue(null);
        salaryField.clear();
        ratingField.clear();
        experienceField.clear();
        activeCheckBox.setSelected(true);
    }

    // Getters remain the same
    public TextField getIdField() {
        return idField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public ComboBox<String> getDepartmentComboBox() {
        return departmentComboBox;
    }

    public TextField getSalaryField() {
        return salaryField;
    }

    public TextField getRatingField() {
        return ratingField;
    }

    public TextField getExperienceField() {
        return experienceField;
    }

    public CheckBox getActiveCheckBox() {
        return activeCheckBox;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getUpdateButton() {
        return updateButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    public Button getClearButton() {
        return clearButton;
    }
}