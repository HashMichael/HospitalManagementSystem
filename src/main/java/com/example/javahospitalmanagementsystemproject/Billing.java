package com.example.javahospitalmanagementsystemproject;

public class Billing {
    private int id;
    private int patientId;
    private double amount;
    private String status;

    public Billing() {}

    public Billing(int id, int patientId, double amount, String status) {
        this.id = id;
        this.patientId = patientId;
        this.amount = amount;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Billing{" +
                "id=" + id +
                ", patientId=" + patientId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
