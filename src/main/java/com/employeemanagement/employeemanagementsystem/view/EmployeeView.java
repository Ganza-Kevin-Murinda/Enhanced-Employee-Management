package com.employeemanagement.employeemanagementsystem.view;

import com.employeemanagement.employeemanagementsystem.model.Employee;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * JavaFX view component for displaying employees in a table
 */

public class EmployeeView extends BorderPane {

    private TableView<Employee<Integer>> employeeTable;
    private ObservableList<Employee<Integer>> employeeData;
    private final Label titleLabel;
    private final Button consoleReportButton;

    // Constructor
    public EmployeeView() {
        setPadding(new Insets(20));

        // Create a title with improved styling
        titleLabel = new Label("Employee Directory");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c4550; -fx-padding: 0 0 10 0;");

        consoleReportButton = new Button("Print Employee Reports to Console");
        consoleReportButton.setStyle("-fx-background-color: #4CAF50; -fx-font-weight: bold ; -fx-text-fill: white;");

        // Create a header with title on left and button on right
        BorderPane header = new BorderPane();
        header.setLeft(titleLabel);
        header.setRight(consoleReportButton);

        VBox contentBox = new VBox(10);
        contentBox.getChildren().add(header);

        setupEmployeeTable();
        contentBox.getChildren().add(employeeTable);

        setCenter(contentBox);
    }

    private static class CenteredTableCell<T> extends TableCell<Employee<Integer>, T> {
        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
            } else {
                setText(item.toString());
                setAlignment(Pos.CENTER);
            }
        }
    }

    private void setupEmployeeTable() {
        employeeTable = new TableView<>();
        employeeTable.setStyle("-fx-font-size: 13px; -fx-border-color: #E0E0E0; -fx-border-width: 1px;");
        employeeData = FXCollections.observableArrayList();
        employeeTable.setItems(employeeData);

        TableColumn<Employee<Integer>, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEmployeeId()));
        idColumn.setPrefWidth(70);
        idColumn.setMinWidth(50);
        idColumn.setCellFactory(column -> new CenteredTableCell<>());

        TableColumn<Employee<Integer>, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmployeeName()));
        nameColumn.setPrefWidth(180);
        nameColumn.setMinWidth(120);
        nameColumn.setCellFactory(column -> new CenteredTableCell<>());

        TableColumn<Employee<Integer>, String> deptColumn = new TableColumn<>("Department");
        deptColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmployeeDepartment().name()));
        deptColumn.setPrefWidth(140);
        deptColumn.setMinWidth(100);
        deptColumn.setCellFactory(column -> new CenteredTableCell<>());

        TableColumn<Employee<Integer>, String> salaryColumn = new TableColumn<>("Salary");
        salaryColumn.setCellValueFactory(data -> new SimpleStringProperty(String.format("$%.2f", data.getValue().getEmployeeSalary())));
        salaryColumn.setPrefWidth(100);
        salaryColumn.setMinWidth(90);
        salaryColumn.setCellFactory(column -> new CenteredTableCell<>());

        TableColumn<Employee<Integer>, Double> ratingColumn = new TableColumn<>("Rating");
        ratingColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPerformanceRating()).asObject());
        ratingColumn.setPrefWidth(80);
        ratingColumn.setMinWidth(60);
        ratingColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f", rating));
                    setTextFill(rating >= 4.0 ? Color.rgb(0, 150, 0) :
                            rating >= 3.0 ? Color.rgb(0, 100, 150) :
                                    rating >= 2.0 ? Color.rgb(220, 120, 0) :
                                            Color.rgb(200, 0, 0));
                    setStyle("-fx-font-weight: normal;");
                    setAlignment(Pos.CENTER);
                }
            }
        });

        TableColumn<Employee<Integer>, Integer> expColumn = new TableColumn<>("Experience");
        expColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getYearsOfExperience()));
        expColumn.setPrefWidth(100);
        expColumn.setMinWidth(80);
        expColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer years, boolean empty) {
                super.updateItem(years, empty);
                if (empty || years == null) {
                    setText(null);
                } else {
                    setText(years + (years == 1 ? " year" : " years"));
                    setAlignment(Pos.CENTER);
                }
            }
        });

        TableColumn<Employee<Integer>, Boolean> activeColumn = new TableColumn<>("Active");
        activeColumn.setCellValueFactory(data -> new SimpleBooleanProperty(data.getValue().isActive()));
        activeColumn.setPrefWidth(80);
        activeColumn.setMinWidth(70);
        activeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean active, boolean empty) {
                super.updateItem(active, empty);
                if (empty || active == null) {
                    setText(null);
                } else {
                    setText(active ? "Active" : "Inactive");
                    setTextFill(active ? Color.rgb(0, 150, 0) :  Color.rgb(200, 0, 0));
                    setStyle(active ? "-fx-font-weight: bold;" : "-fx-font-style: bold;");
                    setAlignment(Pos.CENTER);
                }
            }
        });

        // Header Styling
        String columnHeaderStyle = "-fx-text-fill: #333333; -fx-font-weight: normal; -fx-padding: 8px; -fx-border-color: #dddddd; -fx-border-width: 0 0 1 0; -fx-alignment: CENTER;";
        idColumn.setStyle(columnHeaderStyle);
        nameColumn.setStyle(columnHeaderStyle);
        deptColumn.setStyle(columnHeaderStyle);
        salaryColumn.setStyle(columnHeaderStyle);
        ratingColumn.setStyle(columnHeaderStyle);
        expColumn.setStyle(columnHeaderStyle);
        activeColumn.setStyle(columnHeaderStyle);

        // Clear any old columns to prevent extra columns
        employeeTable.getColumns().clear();
        employeeTable.getColumns().add(idColumn);
        employeeTable.getColumns().add(nameColumn);
        employeeTable.getColumns().add(deptColumn);
        employeeTable.getColumns().add(salaryColumn);
        employeeTable.getColumns().add(ratingColumn);
        employeeTable.getColumns().add(expColumn);
        employeeTable.getColumns().add(activeColumn);


        // Optional: prevent stretching and extra columns
        employeeTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Row height for clarity
        employeeTable.setFixedCellSize(40);

        // Placeholder for empty table
        Label placeholder = new Label("No employees found");
        placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: #757575; -fx-font-style: italic;");
        employeeTable.setPlaceholder(placeholder);

        // Prevent column reordering
        employeeTable.getColumns().forEach(column -> column.setReorderable(false));

        // Single selection mode
        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    // Updates the table with new employee data
    public void updateEmployeeTable(List<Employee<Integer>> employees) {
        employeeData.clear();
        employeeData.addAll(employees);

        // Update the title to show count
        titleLabel.setText("Employee Directory (" + employees.size() + " employees)");
    }

    // Get the selected employee
    public Employee<Integer> getSelectedEmployee() {
        return employeeTable.getSelectionModel().getSelectedItem();
    }

    // Get the employee table for the controller to set up listeners
    public TableView<Employee<Integer>> getEmployeeTable() {
        return employeeTable;
    }

    // Make sure the button is accessible to the controller
    public Button getConsoleReportButton() {
        return consoleReportButton;
    }
}