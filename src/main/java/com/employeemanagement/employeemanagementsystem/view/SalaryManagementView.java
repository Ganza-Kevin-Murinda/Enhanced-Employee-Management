package com.employeemanagement.employeemanagementsystem.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * JavaFX view component for salary management operations
 */
public class SalaryManagementView extends VBox {

    private Button salaryRaiseButton;
    private Button topPaidButton;
    private TextField topNField;
    private Button avgSalaryButton;
    private ComboBox<String> departmentComboBox;

    // Constructor
    public SalaryManagementView() {
        setPadding(new Insets(20));
        setSpacing(15);

        setupSalaryControls();
    }

    private void setupSalaryControls() {
        // Title with style
        Label titleLabel = new Label("Salary Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        // Section labels with style
        Label raiseLabel = new Label("Give raises to employees with high performance ratings (≥ 3.5)");
        raiseLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #34495e;");

        Label topPaidLabel = new Label("View top paid employees:");
        topPaidLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #34495e;");

        Label avgSalaryLabel = new Label("Calculate average salary by department:");
        avgSalaryLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #34495e;");

        // High performer salary raise section
        salaryRaiseButton = new Button("Give Raise");
        salaryRaiseButton.setStyle("-fx-background-color: #8E44AD; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        salaryRaiseButton.setPrefWidth(150);

        // Button hover effects
        salaryRaiseButton.setOnMouseEntered(e -> salaryRaiseButton.setStyle("-fx-background-color: #7D3C98; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
        salaryRaiseButton.setOnMouseExited(e -> salaryRaiseButton.setStyle("-fx-background-color: #8E44AD; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));

        // Top paid employees section
        HBox topPaidBox = new HBox(10);
        topPaidBox.setStyle("-fx-alignment: center-left;");

        Label topLabel = new Label("Top");
        topLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        topNField = new TextField("5");
        topNField.setPrefWidth(50);
        topNField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");

        topPaidButton = new Button("Show Top Paid");
        topPaidButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        topPaidButton.setPrefWidth(150);

        topPaidButton.setOnMouseEntered(e -> topPaidButton.setStyle("-fx-background-color: #F57C00; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
        topPaidButton.setOnMouseExited(e -> topPaidButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));

        topPaidBox.getChildren().addAll(topLabel, topNField, topPaidButton);

        // Average salary section
        HBox avgSalaryBox = new HBox(10);
        avgSalaryBox.setStyle("-fx-alignment: center-left;");

        Label deptLabel = new Label("Department:");
        deptLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        departmentComboBox = new ComboBox<>();
        departmentComboBox.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
        departmentComboBox.setPrefWidth(150);

        avgSalaryButton = new Button("Calculate Average");
        avgSalaryButton.setStyle("-fx-background-color: #00BCD4; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        avgSalaryButton.setPrefWidth(150);

        avgSalaryButton.setOnMouseEntered(e -> avgSalaryButton.setStyle("-fx-background-color: #00ACC1; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));
        avgSalaryButton.setOnMouseExited(e -> avgSalaryButton.setStyle("-fx-background-color: #00BCD4; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;"));

        avgSalaryBox.getChildren().addAll(deptLabel, departmentComboBox, avgSalaryButton);

        // Add all to the view with better spacing
        getChildren().addAll(
                titleLabel,
                raiseLabel,
                salaryRaiseButton,
                new Separator(15),
                topPaidLabel,
                topPaidBox,
                new Separator(15),
                avgSalaryLabel,
                avgSalaryBox
        );
    }

    // Custom separator for visual spacing
    private static class Separator extends HBox {
        public Separator(double height) {
            setMinHeight(height);
        }
    }

    // Populate department combo box
    public void populateDepartments(String[] departments) {
        departmentComboBox.getItems().clear();
        departmentComboBox.getItems().addAll(departments);
        if (departments.length > 0) {
            departmentComboBox.setValue(departments[0]);
        }
    }

    // Getters remain the same
    public Button getSalaryRaiseButton() {
        return salaryRaiseButton;
    }

    public Button getTopPaidButton() {
        return topPaidButton;
    }

    public TextField getTopNField() {
        return topNField;
    }

    public Button getAvgSalaryButton() {
        return avgSalaryButton;
    }

    public ComboBox<String> getDepartmentComboBox() {
        return departmentComboBox;
    }
}
