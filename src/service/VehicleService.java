
package service;

import config.DatabaseConnection;
import exception.VehicleNotFoundException;
import factory.VehicleFactory;
import model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleService {

    // 1. Menampilkan seluruh kendaraan
    public void displayAllVehicles() {
        String sql = "SELECT id, type, brand, model, plate_number, "
                   + "is_available FROM vehicles ORDER BY id";

        displayVehicles(sql);
    }

    // 2. Menampilkan kendaraan yang tersedia
    public void displayAvailableVehicles() {
        String sql = "SELECT id, type, brand, model, plate_number, "
                   + "is_available FROM vehicles "
                   + "WHERE is_available = 1 ORDER BY id";

        displayVehicles(sql);
    }

    // Membaca dan menampilkan kendaraan dari database
    private void displayVehicles(String sql) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            boolean found = false;

            while (rs.next()) {
                Vehicle vehicle = mapVehicle(rs);

                if (!found) {
                    found = true;
                }

                System.out.println(vehicle);
            }

            if (!found) {
                System.out.println("Tidak ada kendaraan yang ditemukan.");
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Gagal mengambil data kendaraan dari database.", e
            );
        }
    }

    // 3. Mengambil kendaraan berdasarkan ID
    public Vehicle getVehicleById(String id)
            throws VehicleNotFoundException {

        String sql = "SELECT id, type, brand, model, plate_number, "
                   + "is_available FROM vehicles WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapVehicle(rs);
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Gagal mencari kendaraan di database.", e
            );
        }

        throw new VehicleNotFoundException(
            "Kendaraan dengan ID " + id + " tidak ditemukan."
        );
    }

    // 4. Menambahkan kendaraan melalui VehicleFactory dan menyimpannya
    public void addVehicle(
            String type,
            String id,
            String brand,
            String model,
            String plateNumber) {

        // Factory memvalidasi jenis kendaraan dan membuat objeknya.
        Vehicle vehicle = VehicleFactory.createVehicle(
            type,
            id,
            brand,
            model,
            plateNumber,
            true,
            null
        );

        String sql = "INSERT INTO vehicles "
                   + "(id, type, brand, model, plate_number, "
                   + "base_rate, is_available) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vehicle.getId());
            pstmt.setString(2, vehicle.getType());
            pstmt.setString(3, vehicle.getBrand());
            pstmt.setString(4, vehicle.getModel());
            pstmt.setString(5, vehicle.getPlateNumber());
            pstmt.setDouble(6, vehicle.getBaseDailyRate());
            pstmt.setInt(7, vehicle.isAvailable() ? 1 : 0);

            pstmt.executeUpdate();

            System.out.println(
                "Kendaraan berhasil ditambahkan: " + vehicle.getId()
            );

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Gagal menyimpan kendaraan. Pastikan ID dan nomor "
                + "plat belum digunakan.", e
            );
        }
    }

    // Mengubah satu baris database menjadi objek Vehicle
    private Vehicle mapVehicle(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String id = rs.getString("id");
        String brand = rs.getString("brand");
        String model = rs.getString("model");
        String plateNumber = rs.getString("plate_number");
        boolean available = rs.getInt("is_available") == 1;

        return VehicleFactory.createVehicle(
            type,
            id,
            brand,
            model,
            plateNumber,
            available,
            null
        );
    }
}
