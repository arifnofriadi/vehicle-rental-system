package service;

import config.DatabaseConnection;
import exception.RentalValidationException;
import exception.VehicleNotFoundException;
import factory.VehicleFactory;
import model.RentalTransaction;
import model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RentalService {

    private static final DateTimeFormatter DATE_TIME =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Menyewa kendaraan yang masih tersedia dan menyimpan transaksinya.
     */
    public RentalTransaction rentVehicle(
            String vehicleId,
            String customerName,
            String phone,
            int days,
            CostCalculatorService costService)
            throws VehicleNotFoundException, RentalValidationException {

        if (days <= 0) {
            throw new RentalValidationException(
                    "Durasi sewa harus lebih dari 0 hari."
            );
        }
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new RentalValidationException(
                    "Nama penyewa tidak boleh kosong."
            );
        }
        if (phone == null || phone.trim().isEmpty()) {
            throw new RentalValidationException(
                    "Nomor telepon tidak boleh kosong."
            );
        }
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new VehicleNotFoundException(
                    "Kendaraan dengan ID tersebut tidak ditemukan!"
            );
        }
        if (costService == null) {
            throw new RentalValidationException(
                    "Layanan perhitungan biaya tidak tersedia."
            );
        }

        String id = vehicleId.trim();
        String name = customerName.trim();
        String customerPhone = phone.trim();

        String findVehicleSql =
                "SELECT id, type, brand, model, plate_number, is_available "
                + "FROM vehicles WHERE id = ?";

        String updateVehicleSql =
                "UPDATE vehicles SET is_available = 0 "
                + "WHERE id = ? AND is_available = 1";

        String insertRentalSql =
                "INSERT INTO rentals ("
                + "rental_id, customer_name, customer_phone, vehicle_id, "
                + "rental_days, total_cost, status, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                String type;
                String brand;
                String modelName;
                String plateNumber;
                boolean available;

                try (PreparedStatement ps =
                             conn.prepareStatement(findVehicleSql)) {
                    ps.setString(1, id);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new VehicleNotFoundException(
                                    "Kendaraan dengan ID " + id
                                    + " tidak ditemukan!"
                            );
                        }
                        type = rs.getString("type");
                        brand = rs.getString("brand");
                        modelName = rs.getString("model");
                        plateNumber = rs.getString("plate_number");
                        available = rs.getInt("is_available") == 1;
                    }
                }

                if (!available) {
                    throw new RentalValidationException(
                            "Kendaraan sedang disewa!"
                    );
                }

                Vehicle vehicle = VehicleFactory.createVehicle(
                        type, id, brand, modelName,
                        plateNumber, available, null
                );

                double totalCost = costService.calculateCost(vehicle, days);
                vehicle.setAvailable(false);

                String rentalId = nextRentalId(conn);
                String rentalDate = LocalDateTime.now().format(DATE_TIME);

                try (PreparedStatement ps =
                             conn.prepareStatement(updateVehicleSql)) {
                    ps.setString(1, id);
                    if (ps.executeUpdate() != 1) {
                        throw new RentalValidationException(
                                "Kendaraan sedang disewa!"
                        );
                    }
                }

                try (PreparedStatement ps =
                             conn.prepareStatement(insertRentalSql)) {
                    ps.setString(1, rentalId);
                    ps.setString(2, name);
                    ps.setString(3, customerPhone);
                    ps.setString(4, id);
                    ps.setInt(5, days);
                    ps.setDouble(6, totalCost);
                    ps.setString(7, RentalTransaction.STATUS_DISEWA);
                    ps.setString(8, rentalDate);
                    ps.executeUpdate();
                }

                conn.commit();

                return new RentalTransaction(
                        rentalId, name, customerPhone, id,
                        days, totalCost, rentalDate
                );

            } catch (VehicleNotFoundException | RentalValidationException e) {
                rollback(conn);
                throw e;
            } catch (IllegalArgumentException e) {
                rollback(conn);
                throw new RentalValidationException(e.getMessage());
            } catch (SQLException e) {
                rollback(conn);
                throw new RentalValidationException(
                        "Gagal menyimpan transaksi sewa: " + e.getMessage()
                );
            }
        } catch (SQLException e) {
            throw new RentalValidationException(
                    "Gagal mengakses database: " + e.getMessage()
            );
        }
    }

    private String nextRentalId(Connection conn) throws SQLException {
        String sql = "SELECT rental_id FROM rentals";
        int max = 0;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String rentalId = rs.getString("rental_id");
                if (rentalId != null && rentalId.startsWith("RNT-")) {
                    try {
                        int number = Integer.parseInt(rentalId.substring(4));
                        if (number > max) {
                            max = number;
                        }
                    } catch (NumberFormatException ignored) {
                        // Abaikan ID yang tidak berformat RNT-angka.
                    }
                }
            }
        }

        return String.format("RNT-%03d", max + 1);
    }

    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            System.err.println(
                    "Peringatan: Rollback database gagal: " + e.getMessage()
            );
        }
    }
}
