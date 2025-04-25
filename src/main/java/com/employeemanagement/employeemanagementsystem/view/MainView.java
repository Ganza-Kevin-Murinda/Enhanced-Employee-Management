package com.employeemanagement.employeemanagementsystem.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

// Main view that integrates all UI components
public class MainView extends BorderPane {
    private EmployeeView tableView;
    private EmployeeFormView formView;
    private EmployeeSearchView searchView;
    private SalaryManagementView salaryManagementView;

    // Constructor
    public MainView() {
        // Apply overall app style
        setStyle("-fx-background-color: #f5f5f7;");
        setPadding(new Insets(15));

        // Initialize components
        tableView = new EmployeeView();
        formView = new EmployeeFormView();
        searchView = new EmployeeSearchView();
        salaryManagementView = new SalaryManagementView();

        // Create a ScrollPane for the center content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f5f5f7; -fx-background-color: transparent;");

        // Create a container for the scrollable content
        VBox contentContainer = new VBox(15);
        contentContainer.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(tableView, Priority.ALWAYS);
        contentContainer.getChildren().addAll(tableView, salaryManagementView);

        // Apply drop shadow to major components
        tableView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5); -fx-background-color: white; -fx-background-radius: 8;");
        formView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5); -fx-background-color: white; -fx-background-radius: 8;");
        searchView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5); -fx-background-color: white; -fx-background-radius: 8; -fx-padding: 15;");
        salaryManagementView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5); -fx-background-color: white; -fx-background-radius: 8;");

        // Set the content container to the scroll pane
        scrollPane.setContent(contentContainer);

        // Add components to layout with some spacing
        setCenter(scrollPane);
        setRight(formView);
        setTop(searchView);

        // Add spacing between regions
        setMargin(searchView, new Insets(0, 0, 15, 0));
        setMargin(formView, new Insets(0, 0, 0, 15));
    }

    // Getters for child views
    public EmployeeView getTableView() {
        return tableView;
    }

    public EmployeeFormView getFormView() {
        return formView;
    }

    public EmployeeSearchView getSearchView() {
        return searchView;
    }

    public SalaryManagementView getSalaryManagementView() {
        return salaryManagementView;
    }
}