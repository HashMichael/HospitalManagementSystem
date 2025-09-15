package com.example.javahospitalmanagementsystemproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordService {
    private DatabaseManager dbManager;

    public MedicalRecordService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    // Add new medical record
    public void addMedicalRecord(MedicalRecord record) {
        String sql = "INSERT INTO medical_records (patient_id, doctor_id, diagnosis, treatment, prescription, record_date) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, record.getPatientId());
            stmt.setInt(2, record.getDoctorId());
            stmt.setString(3, record.getDiagnosis());
            stmt.setString(4, record.getTreatment());
            stmt.setString(5, record.getPrescription());
            stmt.setString(6, record.getRecordDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get medical record by ID
    public MedicalRecord getMedicalRecordById(int id) {
        String sql = "SELECT * FROM medical_records WHERE id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new MedicalRecord(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("diagnosis"),
                        rs.getString("treatment"),
                        rs.getString("prescription"),
                        rs.getString("record_date")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update medical record
    public void updateMedicalRecord(MedicalRecord record) {
        String sql = "UPDATE medical_records SET patient_id = ?, doctor_id = ?, diagnosis = ?, " +
                "treatment = ?, prescription = ?, record_date = ? WHERE id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, record.getPatientId());
            stmt.setInt(2, record.getDoctorId());
            stmt.setString(3, record.getDiagnosis());
            stmt.setString(4, record.getTreatment());
            stmt.setString(5, record.getPrescription());
            stmt.setString(6, record.getRecordDate());
            stmt.setInt(7, record.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete medical record
    public void deleteMedicalRecord(int id) {
        String sql = "DELETE FROM medical_records WHERE id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all medical records
    public List<MedicalRecord> getAllMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM medical_records";
        try (Statement stmt = dbManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                records.add(new MedicalRecord(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getString("diagnosis"),
                        rs.getString("treatment"),
                        rs.getString("prescription"),
                        rs.getString("record_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
}
