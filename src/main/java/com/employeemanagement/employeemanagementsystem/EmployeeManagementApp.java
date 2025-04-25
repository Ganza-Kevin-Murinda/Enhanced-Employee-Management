package com.employeemanagement.employeemanagementsystem;

import com.employeemanagement.employeemanagementsystem.controller.EmployeeController;
import com.employeemanagement.employeemanagementsystem.model.EmployeeDatabase;
import com.employeemanagement.employeemanagementsystem.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Employee Management System
 */
public class EmployeeManagementApp extends Application {

    private static EmployeeDatabase<Integer> employeeDatabase;

    @Override
    public void start(Stage primaryStage) {
        // Initialize the database
        employeeDatabase = new EmployeeDatabase<>();

        // Create the main view
        MainView mainView = new MainView();

        // Create and set up the controller
        EmployeeController controller = new EmployeeController(employeeDatabase, mainView);

        // Create scene and set stage
        Scene scene = new Scene(mainView, 1366, 660);
        primaryStage.setTitle("Employee Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Gets the employee database instance
     */
    public static EmployeeDatabase<Integer> getEmployeeDatabase() {
        if (employeeDatabase == null) {
            employeeDatabase = new EmployeeDatabase<>();
        }
        return employeeDatabase;
    }

    /**
     * Main method
     */
    public static void main(String[] args) {
        launch(args);
    }
}