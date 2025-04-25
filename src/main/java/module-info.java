module com.employeemanagement.employeemanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.employeemanagement.employeemanagementsystem to javafx.fxml;
    exports com.employeemanagement.employeemanagementsystem;
}