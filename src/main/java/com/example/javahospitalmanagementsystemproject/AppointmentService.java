package com.example.javahospitalmanagementsystemproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private DatabaseManager dbManager;

    public AppointmentService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }
    public void addAppointment(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, appointment_time, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, appointment.getPatientId());
            pstmt.setInt(2, appointment.getDoctorId());
            pstmt.setDate(3, appointment.getDate() != null ? java.sql.Date.valueOf(appointment.getDate()) : null);
            pstmt.setTime(4, appointment.getTime() != null ? java.sql.Time.valueOf(appointment.getTime()) : null);
            pstmt.setString(5, appointment.getStatus() != null ? appointment.getStatus() : "SCHEDULED");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding appointment: " + e.getMessage());
            throw e;
        }
    }
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, p.name AS patient_name, d.name AS doctor_name " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.id " +
                "JOIN doctors d ON a.doctor_id = d.id " +
                "ORDER BY a.appointment_date, a.appointment_time";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("appointment_date") != null ? rs.getDate("appointment_date").toString() : null,
                        rs.getTime("appointment_time") != null ? rs.getTime("appointment_time").toString().substring(0, 5) : null
                );
                appointment.setStatus(rs.getString("status") != null ? rs.getString("status") : "SCHEDULED");
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving appointments: " + e.getMessage());
            e.printStackTrace();
        }
        return appointments;
    }
    public Appointment getAppointmentById(int id) throws SQLException {
        String sql = "SELECT * FROM appointments WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Appointment appointment = new Appointment(rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("appointment_date").toString(),
                        rs.getTime("appointment_time").toString().substring(0, 5)
                );
                appointment.setStatus(rs.getString("status"));
                return appointment;
            }
        }
        return null;
    }
    public void updateAppointmentStatus(int id, String status) throws SQLException {
        String sql = "UPDATE appointments SET status = ? WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        }
    }
    public void deleteAppointment(int id) throws SQLException {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE patient_id = ? ORDER BY appointment_date DESC";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("appointment_date").toString(),
                        rs.getTime("appointment_time").toString().substring(0, 5)
                );
                appointment.setStatus(rs.getString("status"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient appointments: " + e.getMessage());
        }
        return appointments;
    }
    public List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE doctor_id = ? ORDER BY appointment_date DESC";
        try (PreparedStatement pstmt = dbManager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Appointment appointment = new Appointment(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("appointment_date").toString(),
                        rs.getTime("appointment_time").toString().substring(0, 5)
                );
                appointment.setStatus(rs.getString("status"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving doctor appointments: " + e.getMessage());
        }
        return appointments;
    }
}

