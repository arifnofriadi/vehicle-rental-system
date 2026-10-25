package model;

import java.util.Locale;

public class RentalTransaction {

    public static final String STATUS_DISEWA = "DISEWA";
    public static final String STATUS_SELESAI = "SELESAI";

    private String rentalId;
    private String customerName;
    private String customerPhone;
    private String vehicleId;
    private int rentalDays;
    private double totalCost;
    private String status;
    private String rentalDate;

    public RentalTransaction(String rentalId, String customerName,
                             String customerPhone, String vehicleId,
                             int rentalDays, double totalCost,
                             String status, String rentalDate) {
        setRentalId(rentalId);
        setCustomerName(customerName);
        setCustomerPhone(customerPhone);
        setVehicleId(vehicleId);
        setRentalDays(rentalDays);
        setTotalCost(totalCost);
        setStatus(status);
        setRentalDate(rentalDate);
    }

    public RentalTransaction(String rentalId, String customerName,
                             String customerPhone, String vehicleId,
                             int rentalDays, double totalCost,
                             String rentalDate) {
        this(rentalId, customerName, customerPhone, vehicleId,
             rentalDays, totalCost, STATUS_DISEWA, rentalDate);
    }

    public String getRentalId() {
        return rentalId;
    }

    public void setRentalId(String rentalId) {
        this.rentalId = requireText(rentalId, "ID transaksi");
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = requireText(customerName, "Nama penyewa");
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = requireText(customerPhone, "Nomor telepon");
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = requireText(vehicleId, "ID kendaraan");
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public void setRentalDays(int rentalDays) {
        if (rentalDays <= 0) {
            throw new IllegalArgumentException(
                    "Durasi sewa harus lebih dari 0 hari."
            );
        }
        this.rentalDays = rentalDays;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        if (totalCost < 0) {
            throw new IllegalArgumentException(
                    "Total biaya tidak boleh negatif."
            );
        }
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Status sewa tidak boleh kosong."
            );
        }

        String normalized = status.trim().toUpperCase(Locale.ROOT);
        if (!STATUS_DISEWA.equals(normalized)
                && !STATUS_SELESAI.equals(normalized)) {
            throw new IllegalArgumentException(
                    "Status sewa hanya boleh DISEWA atau SELESAI."
            );
        }
        this.status = normalized;
    }

    public String getRentalDate() {
        return rentalDate;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = requireText(rentalDate, "Tanggal sewa");
    }

    private String requireText(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    label + " tidak boleh kosong."
            );
        }
        return value.trim();
    }
}
