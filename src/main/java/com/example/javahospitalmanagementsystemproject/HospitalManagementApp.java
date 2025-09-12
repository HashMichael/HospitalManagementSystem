package com.example.javahospitalmanagementsystemproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class HospitalManagementApp extends Application {
    private DatabaseManager dbManager;
    private PatientService patientService;
    private DoctorService doctorService;
    private AppointmentService appointmentService;
    @Override
    public void start(Stage primaryStage) {

         dbManager = new DatabaseManager();
         patientService = new PatientService(dbManager);
         doctorService = new DoctorService(dbManager);
         appointmentService = new AppointmentService(dbManager);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

         MenuBar menuBar = createMenuBar();
         root.setTop(menuBar);

        TabPane tabPane = new TabPane();

        Tab patientTab = new Tab("Patients");
        patientTab.setClosable(false);
        patientTab.setContent(createPatientTab());

        Tab doctorTab = new Tab("Doctors");
        doctorTab.setClosable(false);
        doctorTab.setContent(createDoctorTab());

        Tab appointmentTab = new Tab("Appointments");
        appointmentTab.setClosable(false);
        appointmentTab.setContent(createAppointmentTab());
        tabPane.getTabs().addAll(patientTab, doctorTab, appointmentTab);
        root.setCenter(tabPane);

         Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Hospital Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);
        // Help menu
        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAlert("About", "Hospital Management System v1.0"));
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }
    private VBox createPatientTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

         GridPane form = new GridPane();
         form.setHgap(10);
         form.setVgap(10);
         TextField nameField = new TextField();
         TextField ageField = new TextField();
         TextField phoneField = new TextField();
         TextField addressField = new TextField();
         form.add(new Label("Name:"), 0, 0);
         form.add(nameField, 1, 0);
         form.add(new Label("Age:"), 0, 1);
         form.add(ageField, 1, 1);
         form.add(new Label("Phone:"), 0, 2);
         form.add(phoneField, 1, 2);
         form.add(new Label("Address:"), 0, 3);
         form.add(addressField, 1, 3);
         Button addButton = new Button("Add Patient");
         addButton.setOnAction(e -> {
             try {
                 String name = nameField.getText();
                 int age = Integer.parseInt(ageField.getText());
                 String phone = phoneField.getText();
                 String address = addressField.getText();
                 Patient patient = new Patient(0, name, age, phone, address);
                 patientService.addPatient(patient);
                 showAlert("Success", "Patient added successfully!");
                 clearFields(nameField, ageField, phoneField, addressField);
             } catch (Exception ex) {
                 showAlert("Error", "Error adding patient: " + ex.getMessage());
             }
         });
         form.add(addButton, 1, 4);

        ListView<String> patientList = new ListView<>();
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            patientList.getItems().clear();
            patientService.getAllPatients().forEach(patient ->
                    patientList.getItems().add(patient.toString()));
        });
        vbox.getChildren().addAll(
                new Label("Add New Patient"),
                new Separator(),
                form,
                new Separator(),
                new Label("Patient List"),
                refreshButton,
                patientList
        );
        return vbox;
    }
    private VBox createDoctorTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        TextField nameField = new TextField();
        TextField specializationField = new TextField();
        TextField phoneField = new TextField();
        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Specialization:"), 0, 1);
        form.add(specializationField, 1, 1);
        form.add(new Label("Phone:"), 0, 2);
        form.add(phoneField, 1, 2);
        Button addButton = new Button("Add Doctor");
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String specialization = specializationField.getText();
                String phone = phoneField.getText();
                Doctor doctor = new Doctor(0, name, specialization, phone);
                doctorService.addDoctor(doctor);
                showAlert("Success", "Doctor added successfully!");
                clearFields(nameField, specializationField, phoneField);
            } catch (Exception ex) {
                showAlert("Error", "Error adding doctor: " + ex.getMessage());
            }
        });
        form.add(addButton, 1, 3);

        ListView<String> doctorList = new ListView<>();
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            doctorList.getItems().clear();
            doctorService.getAllDoctors().forEach(doctor ->
                    doctorList.getItems().add(doctor.toString()));
        });
        vbox.getChildren().addAll(                new Label("Add New Doctor"),
                new Separator(),
                form,
                new Separator(),
                new Label("Doctor List"),
                refreshButton,
                doctorList
        );
        return vbox;
    }
    private VBox createAppointmentTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        TextField patientIdField = new TextField();
        TextField doctorIdField = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField();
        form.add(new Label("Patient ID:"), 0, 0);
        form.add(patientIdField, 1, 0);
        form.add(new Label("Doctor ID:"), 0, 1);
        form.add(doctorIdField, 1, 1);
        form.add(new Label("Date:"), 0, 2);
        form.add(datePicker, 1, 2);
        form.add(new Label("Time (HH:mm):"), 0, 3);
        form.add(timeField, 1, 3);
        Button addButton = new Button("Schedule Appointment");
        addButton.setOnAction(e -> {
            try {
                int patientId = Integer.parseInt(patientIdField.getText());
                int doctorId = Integer.parseInt(doctorIdField.getText());
                if (datePicker.getValue() == null) {
                    showAlert("Error", "Please select a date.");
                    return;
                }
                String date = datePicker.getValue().toString();
                String time = timeField.getText();
                if (!time.matches("\\d{2}:\\d{2}")) {
                    showAlert("Error", "Please enter time in HH:mm format (e.g., 09:00).");
                    return;
                }
                Appointment appointment = new Appointment(0, patientId, doctorId, date, time);
                if (appointment.getDate() == null || appointment.getTime() == null) {
                    showAlert("Error", "Invalid date or time format.");
                    return;
                }
                appointmentService.addAppointment(appointment);
                showAlert("Success", "Appointment scheduled successfully!");
                clearFields(patientIdField, doctorIdField, timeField);
                datePicker.setValue(null);
            } catch (Exception ex) {
                showAlert("Error", "Error scheduling appointment: " + ex.getMessage());
            }
        });
        form.add(addButton, 1, 4);

        ListView<String> appointmentList = new ListView<>();
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            appointmentList.getItems().clear();
            try {
                List<Appointment> appointments = appointmentService.getAllAppointments();
                if (appointments.isEmpty()) {
                    appointmentList.getItems().add("No appointments found.");
                } else {
                    appointments.forEach(appointment ->
                            appointmentList.getItems().add(appointment.toString()));
                }
            } catch (Exception ex) {
                showAlert("Error", "Failed to refresh appointments: " + ex.getMessage());
            }
        });
        vbox.getChildren().addAll(
                new Label("Schedule New Appointment"),
                new Separator(),
                form,
                new Separator(),
                new Label("Appointment List"),
                refreshButton,
                appointmentList
        );
        return vbox;
    }
    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

