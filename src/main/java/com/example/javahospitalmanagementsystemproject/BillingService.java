package com.example.javahospitalmanagementsystemproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillingService {
    private DatabaseManager dbManager;

    public BillingService(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addBill(Billing bill) {
        String sql = "INSERT INTO billing (patient_id, amount, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, bill.getPatientId());
            stmt.setDouble(2, bill.getAmount());
            stmt.setString(3, bill.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Billing getBillById(int id) {
        String sql = "SELECT * FROM billing WHERE id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Billing(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBill(Billing bill) {
        String sql = "UPDATE billing SET patient_id = ?, amount = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, bill.getPatientId());
            stmt.setDouble(2, bill.getAmount());
            stmt.setString(3, bill.getStatus());
            stmt.setInt(4, bill.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBill(int id) {
        String sql = "DELETE FROM billing WHERE id = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Billing> getAllBills() {
        List<Billing> bills = new ArrayList<>();
        String sql = "SELECT * FROM billing";
        try (Statement stmt = dbManager.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bills.add(new Billing(
                        rs.getInt("id"),
                        rs.getInt("patient_id"),
                        rs.getDouble("amount"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }
}
