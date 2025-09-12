package com.example.javahospitalmanagementsystemproject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PatientService {
    private DatabaseManager dbManager;
    public PatientService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }    public void addPatient(Patient patient) throws SQLException {
        String sql = "INSERT INTO patients (name, age, phone, address) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getPhone());
            pstmt.setString(4, patient.getAddress());
            pstmt.executeUpdate();
        }
    }
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY id";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patients: " + e.getMessage());
        }
        return patients;
    }
    public Patient getPatientById(int id) throws SQLException {
        String sql = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("phone"),
                        rs.getString("address")
                );
            }
        }
        return null;
    }
    public void updatePatient(Patient patient) throws SQLException {
        String sql = "UPDATE patients SET name = ?, age = ?, phone = ?, address = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getPhone());
            pstmt.setString(4, patient.getAddress());
            pstmt.setInt(5, patient.getId());
            pstmt.executeUpdate();
        }
    }
    public void deletePatient(int id) throws SQLException {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
