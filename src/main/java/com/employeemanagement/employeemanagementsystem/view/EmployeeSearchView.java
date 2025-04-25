package com.employeemanagement.employeemanagementsystem.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * JavaFX view component for searching and sorting employees
 */

public class EmployeeSearchView extends HBox {
    private TextField searchField;
    private ComboBox<String> searchTypeComboBox;
    private ComboBox<String> sortComboBox;
    private Button searchButton;
    private Button sortButton;
    private Button resetButton;
    private Button refreshButton;

    // Constructor
    public EmployeeSearchView() {
        setPadding(new Insets(15));
        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);

        setupSearchControls();
    }

    private void setupSearchControls() {
        // Search field with style
        searchField = new TextField();
        searchField.setPromptText("Search term");
        searchField.setPrefWidth(200);
        searchField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        // Search type dropdown with style
        searchTypeComboBox = new ComboBox<>();
        searchTypeComboBox.getItems().addAll("Name", "Department", "Minimum Rating", "Salary Range");
        searchTypeComboBox.setValue("Name");
        searchTypeComboBox.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        // Sort dropdown with style
        sortComboBox = new ComboBox<>();
        sortComboBox.getItems().addAll("Experience (Default)", "Salary", "Performance");
        sortComboBox.setValue("Experience (Default)");
        sortComboBox.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        // Style labels
        Label searchLabel = new Label("Search:");
        searchLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label sortLabel = new Label("Sort by:");
        sortLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Button styles
        String buttonStyle = "-fx-font-weight: bold; -fx-background-radius: 5;";

        searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " + buttonStyle);

        sortButton = new Button("Sort");
        sortButton.setStyle("-fx-background-color: #E67E22; -fx-text-fill: white; " + buttonStyle);

        resetButton = new Button("Reset");
        resetButton.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; " + buttonStyle);

        refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; " + buttonStyle);

        // Button hover effects
        searchButton.setOnMouseEntered(e -> searchButton.setStyle("-fx-background-color: #2980B9; -fx-text-fill: white; " + buttonStyle));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("-fx-background-color: #3498DB; -fx-text-fill: white; " + buttonStyle));

        sortButton.setOnMouseEntered(e -> sortButton.setStyle("-fx-background-color: #D35400; -fx-text-fill: white; " + buttonStyle));
        sortButton.setOnMouseExited(e -> sortButton.setStyle("-fx-background-color: #E67E22; -fx-text-fill: white; " + buttonStyle));

        resetButton.setOnMouseEntered(e -> resetButton.setStyle("-fx-background-color: #7F8C8D; -fx-text-fill: white; " + buttonStyle));
        resetButton.setOnMouseExited(e -> resetButton.setStyle("-fx-background-color: #95A5A6; -fx-text-fill: white; " + buttonStyle));

        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle("-fx-background-color: #27AE60; -fx-text-fill: white; " + buttonStyle));
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle("-fx-background-color: #2ECC71; -fx-text-fill: white; " + buttonStyle));

        // Add all to search pane with better organization
        getChildren().addAll(
                searchLabel,
                searchField,
                searchTypeComboBox,
                searchButton,
                new Spacer(), // Add flexible space
                sortLabel,
                sortComboBox,
                sortButton,
                resetButton,
                refreshButton
        );
    }

    // Flexible spacer
    private class Spacer extends Region {
        public Spacer() {
            HBox.setHgrow(this, Priority.ALWAYS);
        }
    }

    // Clear search fields
    public void clearForm() {
        searchField.clear();
    }

    // Getters remain the same
    public TextField getSearchField() {
        return searchField;
    }

    public ComboBox<String> getSearchTypeComboBox() {
        return searchTypeComboBox;
    }

    public ComboBox<String> getSortComboBox() {
        return sortComboBox;
    }

    public Button getSearchButton() {
        return searchButton;
    }

    public Button getSortButton() {
        return sortButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getRefreshButton() {
        return refreshButton;
    }
}