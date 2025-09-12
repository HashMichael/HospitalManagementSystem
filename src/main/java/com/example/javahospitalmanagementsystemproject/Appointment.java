package com.example.javahospitalmanagementsystemproject;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private LocalDate date;
    private LocalTime time;
    private String status;

    public Appointment() {
    }

    public Appointment(int id, int patientId, int doctorId, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Appointment(int id, int patientId, int doctorId, String appointmentDate, String appointmentTime) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        try {
            this.date = appointmentDate != null ? LocalDate.parse(appointmentDate, DateTimeFormatter.ISO_LOCAL_DATE) : null;
            this.time = appointmentTime != null ? LocalTime.parse(appointmentTime, DateTimeFormatter.ofPattern("HH:mm")) : null;
        } catch (DateTimeParseException e) {
            System.err.println("Error parsing date/time: " + e.getMessage());
            this.date = null;
            this.time = null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", date=" + (date != null ? date : "N/A") +
                ", time=" + (time != null ? time : "N/A") +
                ", status='" + (status != null ? status : "N/A") + '\'' +
                '}';
    }
}