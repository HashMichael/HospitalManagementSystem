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
    private MedicalRecordService medicalRecordService;
    private BillingService billingService;

    @Override
    public void start(Stage primaryStage) {
        dbManager = new DatabaseManager();
        patientService = new PatientService(dbManager);
        doctorService = new DoctorService(dbManager);
        appointmentService = new AppointmentService(dbManager);
        medicalRecordService = new MedicalRecordService(dbManager);
        billingService = new BillingService(dbManager);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        TabPane tabPane = new TabPane();

        Tab patientTab = new Tab("Patients", createPatientTab());
        patientTab.setClosable(false);

        Tab doctorTab = new Tab("Doctors", createDoctorTab());
        doctorTab.setClosable(false);

        Tab appointmentTab = new Tab("Appointments", createAppointmentTab());
        appointmentTab.setClosable(false);

        Tab recordTab = new Tab("Medical Records", createMedicalRecordTab());
        recordTab.setClosable(false);

        Tab billingTab = new Tab("Billing", createBillingTab());
        billingTab.setClosable(false);

        tabPane.getTabs().addAll(patientTab, doctorTab, appointmentTab, recordTab, billingTab);
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1200, 750);
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

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAlert("About", "Hospital Management System v2.0"));
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    private VBox createPatientTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane form = createFormGrid();

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField ageField = new TextField();
        TextField phoneField = new TextField();
        TextField addressField = new TextField();

        form.add(new Label("ID:"), 0, 0); form.add(idField, 1, 0);
        form.add(new Label("Name:"), 0, 1); form.add(nameField, 1, 1);
        form.add(new Label("Age:"), 0, 2); form.add(ageField, 1, 2);
        form.add(new Label("Phone:"), 0, 3); form.add(phoneField, 1, 3);
        form.add(new Label("Address:"), 0, 4); form.add(addressField, 1, 4);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String ageText = ageField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();

                if (name.isEmpty() || ageText.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    showAlert("Error", "All fields must be filled!");
                    return;
                }

                int age = Integer.parseInt(ageText);

                Patient p = new Patient(0, nameField.getText(), age, phoneField.getText(), addressField.getText());
                patientService.addPatient(p);
                showAlert("Success", "Patient added!");
                clearFields(idField, nameField, ageField, phoneField, addressField);
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button getButton = new Button("Get By ID");
        getButton.setOnAction(e -> {
            try {
                Patient p = patientService.getPatientById(Integer.parseInt(idField.getText()));
                if (p != null) showAlert("Patient Found", p.toString());
                else showAlert("Not Found", "No patient with ID");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            try {
                Patient p = new Patient(Integer.parseInt(idField.getText()), nameField.getText(),
                        Integer.parseInt(ageField.getText()), phoneField.getText(), addressField.getText());
                patientService.updatePatient(p);
                showAlert("Success", "Patient updated!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            try {
                patientService.deletePatient(Integer.parseInt(idField.getText()));
                showAlert("Deleted", "Patient removed!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        form.addRow(5, addButton, getButton, updateButton, deleteButton);

        ListView<String> patientList = new ListView<>();
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            patientList.getItems().clear();
            patientService.getAllPatients().forEach(p -> patientList.getItems().add(p.toString()));
        });

        vbox.getChildren().addAll(new Label("Patient Management"), form, refreshButton, patientList);
        return vbox;
    }

    private VBox createDoctorTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane form = createFormGrid();

        TextField idField = new TextField();
        TextField nameField = new TextField();
        TextField specializationField = new TextField();
        TextField phoneField = new TextField();

        form.add(new Label("ID:"), 0, 0); form.add(idField, 1, 0);
        form.add(new Label("Name:"), 0, 1); form.add(nameField, 1, 1);
        form.add(new Label("Specialization:"), 0, 2); form.add(specializationField, 1, 2);
        form.add(new Label("Phone:"), 0, 3); form.add(phoneField, 1, 3);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String specialization = specializationField.getText().trim();
                String phone = phoneField.getText().trim();

                if (name.isEmpty() || specialization.isEmpty() || phone.isEmpty()) {
                    showAlert("Error", "All fields must be filled!");
                    return;
                }

                Doctor d = new Doctor(0, name, specialization, phone);
                doctorService.addDoctor(d);
                showAlert("Success", "Doctor added!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button getButton = new Button("Get By ID");
        getButton.setOnAction(e -> {
            try {
                Doctor d = doctorService.getDoctorById(Integer.parseInt(idField.getText()));
                if (d != null) showAlert("Doctor Found", d.toString());
                else showAlert("Not Found", "No doctor with ID");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            try {
                Doctor d = new Doctor(Integer.parseInt(idField.getText()), nameField.getText(),
                        specializationField.getText(), phoneField.getText());
                doctorService.updateDoctor(d);
                showAlert("Success", "Doctor updated!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            try {
                doctorService.deleteDoctor(Integer.parseInt(idField.getText()));
                showAlert("Deleted", "Doctor removed!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        form.addRow(4, addButton, getButton, updateButton, deleteButton);

        ListView<String> doctorList = new ListView<>();
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            doctorList.getItems().clear();
            doctorService.getAllDoctors().forEach(d -> doctorList.getItems().add(d.toString()));
        });

        vbox.getChildren().addAll(new Label("Doctor Management"), form, refreshButton, doctorList);
        return vbox;
    }

    private VBox createAppointmentTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane form = createFormGrid();

        TextField idField = new TextField();
        TextField patientIdField = new TextField();
        TextField doctorIdField = new TextField();
        DatePicker datePicker = new DatePicker();
        TextField timeField = new TextField(); // e.g. "14:30"
        TextField statusField = new TextField();

        form.add(new Label("ID:"), 0, 0); form.add(idField, 1, 0);
        form.add(new Label("Patient ID:"), 0, 1); form.add(patientIdField, 1, 1);
        form.add(new Label("Doctor ID:"), 0, 2); form.add(doctorIdField, 1, 2);
        form.add(new Label("Date:"), 0, 3); form.add(datePicker, 1, 3);
        form.add(new Label("Time:"), 0, 4); form.add(timeField, 1, 4);
        form.add(new Label("Status:"), 0, 5); form.add(statusField, 1, 5);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {

                LocalDate selectedDate = datePicker.getValue();
                LocalDate today = LocalDate.now();

                if (selectedDate == null) {
                    showAlert("Error", "Please select a date!");
                    return;
                }

                if (selectedDate.isBefore(today)) {
                    showAlert("Error", "Appointment date cannot be in the past!");
                    return;
                }

                LocalTime selectedTime = LocalTime.parse(timeField.getText());
                LocalTime now = LocalTime.now();

                if (selectedDate.equals(today) && selectedTime.isBefore(now)){
                    showAlert("Error", "Appointment time cannot be in the past!");
                    return;
                }

                Appointment a = new Appointment(
                        0,
                        Integer.parseInt(patientIdField.getText()),
                        Integer.parseInt(doctorIdField.getText()),
                        selectedDate,
                        selectedTime,
                        statusField.getText()
                );

                appointmentService.addAppointment(a);
                showAlert("Success", "Appointment added!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button getButton = new Button("Get By ID");
        getButton.setOnAction(e -> {
            try {
                Appointment a = appointmentService.getAppointmentById(Integer.parseInt(idField.getText()));
                if (a != null) showAlert("Appointment Found", a.toString());
                else showAlert("Not Found", "No appointment with ID");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            try {
                Appointment a = new Appointment(Integer.parseInt(idField.getText()),
                        Integer.parseInt(patientIdField.getText()),
                        Integer.parseInt(doctorIdField.getText()),
                        datePicker.getValue(),
                        LocalTime.parse(timeField.getText()),
                        statusField.getText());
                appointmentService.updateAppointmentStatus(a);
                showAlert("Success", "Appointment updated!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            try {
                appointmentService.deleteAppointment(Integer.parseInt(idField.getText()));
                showAlert("Deleted", "Appointment removed!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        form.addRow(6, addButton, getButton, updateButton, deleteButton);

        ListView<String> appointmentList = new ListView<>();

        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            appointmentList.getItems().clear();
            appointmentService.getAllAppointments().forEach(a -> appointmentList.getItems().add(a.toString()));
        });

        // ✅ New: Filter by patient
        Button patientAppointmentsButton = new Button("Show by Patient");
        patientAppointmentsButton.setOnAction(e -> {
            try {
                appointmentList.getItems().clear();
                appointmentService.getAppointmentsByPatientId(Integer.parseInt(patientIdField.getText()))
                        .forEach(a -> appointmentList.getItems().add(a.toString()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        // ✅ New: Filter by doctor
        Button doctorAppointmentsButton = new Button("Show by Doctor");
        doctorAppointmentsButton.setOnAction(e -> {
            try {
                appointmentList.getItems().clear();
                appointmentService.getAppointmentsByDoctorId(Integer.parseInt(doctorIdField.getText()))
                        .forEach(a -> appointmentList.getItems().add(a.toString()));
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        vbox.getChildren().addAll(
                new Label("Appointment Management"),
                form,
                refreshButton,
                patientAppointmentsButton,
                doctorAppointmentsButton,
                appointmentList
        );
        return vbox;
    }

    private VBox createMedicalRecordTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane form = createFormGrid();

        TextField idField = new TextField();
        TextField patientIdField = new TextField();
        TextArea diagnosisArea = new TextArea();
        TextArea treatmentArea = new TextArea();
        TextArea prescriptionArea = new TextArea();
        DatePicker datePicker = new DatePicker();


        form.add(new Label("ID:"), 0, 0); form.add(idField, 1, 0);
        form.add(new Label("Patient ID:"), 0, 1); form.add(patientIdField, 1, 1);
        form.add(new Label("Diagnosis:"), 0, 2); form.add(diagnosisArea, 1, 2);
        form.add(new Label("Treatment:"), 0, 3); form.add(treatmentArea, 1, 3);
        form.add(new Label("Prescription"), 0, 4); form.add(prescriptionArea, 1, 4);
        form.add(new Label("RecordDate:"), 0, 5); form.add(datePicker, 1, 5);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {

                LocalDate selectedDate = datePicker.getValue();
                LocalDate today = LocalDate.now();

                if (selectedDate == null) {
                    showAlert("Error", "Please select a date!");
                    return;
                }

                if (selectedDate.isBefore(today)) {
                    showAlert("Error", "Appointment date cannot be in the past!");
                    return;
                }

                MedicalRecord r = new MedicalRecord(0, Integer.parseInt(patientIdField.getText()),
                        diagnosisArea.getText(), treatmentArea.getText(), prescriptionArea.getText(), selectedDate);
                medicalRecordService.addMedicalRecord(r);
                showAlert("Success", "Record added!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button getButton = new Button("Get By ID");
        getButton.setOnAction(e -> {
            try {
                MedicalRecord r = medicalRecordService.getMedicalRecordById(Integer.parseInt(idField.getText()));
                if (r != null) showAlert("Record Found", r.toString());
                else showAlert("Not Found", "No record with ID");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            try {
                LocalDate selectedDate = datePicker.getValue();
                LocalDate today = LocalDate.now();

                if (selectedDate == null) {
                    showAlert("Error", "Please select a date!");
                    return;
                }

                if (selectedDate.isBefore(today)) {
                    showAlert("Error", "Record date cannot be in the past!");
                    return;
                }

                MedicalRecord r = new MedicalRecord(
                        Integer.parseInt(idField.getText()),
                        Integer.parseInt(patientIdField.getText()),
                        diagnosisArea.getText(),
                        treatmentArea.getText(),
                        prescriptionArea.getText(),
                        selectedDate
                );

                medicalRecordService.updateMedicalRecord(r);
                showAlert("Success", "Record updated!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });


        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            try {
                medicalRecordService.deleteMedicalRecord(Integer.parseInt(idField.getText()));
                showAlert("Deleted", "Record removed!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        form.addRow(4, addButton, getButton, updateButton, deleteButton);

        ListView<String> recordList = new ListView<>();
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            recordList.getItems().clear();
            medicalRecordService.getAllMedicalRecords().forEach(r -> recordList.getItems().add(r.toString()));
        });

        vbox.getChildren().addAll(new Label("Medical Records Management"), form, refreshButton, recordList);
        return vbox;
    }

    // ---------------- Billing ----------------
    private VBox createBillingTab() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        GridPane form = createFormGrid();

        TextField idField = new TextField();
        TextField patientIdField = new TextField();
        TextField amountField = new TextField();
        TextField statusField = new TextField();

        form.add(new Label("ID:"), 0, 0); form.add(idField, 1, 0);
        form.add(new Label("Patient ID:"), 0, 1); form.add(patientIdField, 1, 1);
        form.add(new Label("Amount:"), 0, 2); form.add(amountField, 1, 2);
        form.add(new Label("Status:"), 0, 3); form.add(statusField, 1, 3);

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try {
                Billing b = new Billing(0, Integer.parseInt(patientIdField.getText()),
                        Double.parseDouble(amountField.getText()), statusField.getText());
                billingService.addBill(b);
                showAlert("Success", "Bill added!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button getButton = new Button("Get By ID");
        getButton.setOnAction(e -> {
            try {
                Billing b = billingService.getBillById(Integer.parseInt(idField.getText()));
                if (b != null) showAlert("Bill Found", b.toString());
                else showAlert("Not Found", "No bill with ID");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            try {
                Billing b = new Billing(Integer.parseInt(idField.getText()),
                        Integer.parseInt(patientIdField.getText()),
                        Double.parseDouble(amountField.getText()), statusField.getText());
                billingService.updateBill(b);
                showAlert("Success", "Bill updated!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            try {
                billingService.deleteBill(Integer.parseInt(idField.getText()));
                showAlert("Deleted", "Bill removed!");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        form.addRow(4, addButton, getButton, updateButton, deleteButton);

        ListView<String> billList = new ListView<>();
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> {
            billList.getItems().clear();
            billingService.getAllBills().forEach(b -> billList.getItems().add(b.toString()));
        });

        vbox.getChildren().addAll(new Label("Billing Management"), form, refreshButton, billList);
        return vbox;
    }

    private GridPane createFormGrid() {
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        return form;
    }

    private void clearFields(TextField... fields) {
        for (TextField f : fields) f.clear();
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
