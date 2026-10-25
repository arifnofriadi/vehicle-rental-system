/**
 * Paket ini berisi layanan untuk memproses pengembalian kendaraan
 * dan menampilkan daftar transaksi sewa yang masih aktif.
 */
package service;

import config.DatabaseConnection;
import exception.RentalValidationException;
import exception.VehicleNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class ReturnService {

    /**
     * Memproses pengembalian berdasarkan ID transaksi sewa
     * atau ID kendaraan.
     *
     * @param rentalIdOrVehicleId ID transaksi atau ID kendaraan
     * @throws RentalValidationException jika transaksi tidak valid
     * @throws VehicleNotFoundException jika kendaraan tidak ditemukan
     */
    public void processReturn(String rentalIdOrVehicleId)
            throws RentalValidationException, VehicleNotFoundException {

        if (rentalIdOrVehicleId == null
                || rentalIdOrVehicleId.trim().isEmpty()) {
            throw new RentalValidationException(
                    "ID transaksi atau ID kendaraan tidak boleh kosong!"
            );
        }

        String id = rentalIdOrVehicleId.trim();

        String findRentalSql =
                "SELECT rental_id, vehicle_id, status "
                + "FROM rentals "
                + "WHERE rental_id = ?";

        String findActiveRentalByVehicleSql =
                "SELECT rental_id, vehicle_id, status "
                + "FROM rentals "
                + "WHERE vehicle_id = ? AND status = 'DISEWA' "
                + "LIMIT 1";

        String checkVehicleSql =
                "SELECT id FROM vehicles WHERE id = ?";

        String updateRentalSql =
                "UPDATE rentals SET status = 'SELESAI' "
                + "WHERE rental_id = ? AND status = 'DISEWA'";

        String updateVehicleSql =
                "UPDATE vehicles SET is_available = 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Mulai transaksi database.
            conn.setAutoCommit(false);

            try {
                String rentalId = null;
                String vehicleId = null;
                String status = null;

                // 1. Cari berdasarkan ID transaksi terlebih dahulu.
                try (PreparedStatement ps =
                             conn.prepareStatement(findRentalSql)) {

                    ps.setString(1, id);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            rentalId = rs.getString("rental_id");
                            vehicleId = rs.getString("vehicle_id");
                            status = rs.getString("status");
                        }
                    }
                }

                // 2. Jika bukan ID transaksi, cari kendaraan
                // yang sedang disewa menggunakan ID kendaraan.
                if (rentalId == null) {
                    try (PreparedStatement ps =
                                 conn.prepareStatement(
                                         findActiveRentalByVehicleSql)) {

                        ps.setString(1, id);

                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                rentalId = rs.getString("rental_id");
                                vehicleId = rs.getString("vehicle_id");
                                status = rs.getString("status");
                            }
                        }
                    }

                    // 3. Jika tidak ada sewa aktif, cek apakah
                    // kendaraan tersebut terdaftar.
                    if (rentalId == null) {
                        try (PreparedStatement ps =
                                     conn.prepareStatement(checkVehicleSql)) {

                            ps.setString(1, id);

                            try (ResultSet rs = ps.executeQuery()) {
                                if (!rs.next()) {
                                    throw new VehicleNotFoundException(
                                            "Kendaraan dengan ID " + id
                                            + " tidak ditemukan!"
                                    );
                                }
                            }
                        }

                        throw new RentalValidationException(
                                "Tidak ada transaksi sewa aktif "
                                + "untuk kendaraan " + id + "!"
                        );
                    }
                }

                // 4. Validasi status transaksi.
                if (!"DISEWA".equalsIgnoreCase(status)) {
                    throw new RentalValidationException(
                            "Transaksi " + rentalId
                            + " tidak dapat diproses karena statusnya "
                            + status + ", bukan DISEWA!"
                    );
                }

                // 5. Ubah status transaksi menjadi SELESAI.
                int updatedRental;

                try (PreparedStatement ps =
                             conn.prepareStatement(updateRentalSql)) {

                    ps.setString(1, rentalId);
                    updatedRental = ps.executeUpdate();
                }

                if (updatedRental != 1) {
                    throw new RentalValidationException(
                            "Transaksi gagal diperbarui. "
                            + "Pastikan transaksi masih aktif."
                    );
                }

                // 6. Kembalikan status kendaraan menjadi tersedia.
                try (PreparedStatement ps =
                             conn.prepareStatement(updateVehicleSql)) {

                    ps.setString(1, vehicleId);

                    if (ps.executeUpdate() != 1) {
                        throw new VehicleNotFoundException(
                                "Data kendaraan dengan ID " + vehicleId
                                + " tidak ditemukan!"
                        );
                    }
                }

                // 7. Simpan kedua perubahan sekaligus.
                conn.commit();

                System.out.println("\n==================================");
                System.out.println(" PENGEMBALIAN BERHASIL");
                System.out.println("==================================");
                System.out.println("ID Transaksi : " + rentalId);
                System.out.println("ID Kendaraan : " + vehicleId);
                System.out.println("Status Sewa  : SELESAI");
                System.out.println("Status Unit  : Tersedia");
                System.out.println("Kendaraan berhasil dikembalikan.");
                System.out.println("==================================\n");

            } catch (RentalValidationException | VehicleNotFoundException e) {

                rollback(conn);
                throw e;

            } catch (SQLException e) {

                rollback(conn);

                throw new RentalValidationException(
                        "Gagal memproses pengembalian: " + e.getMessage()
                );
            }

        } catch (SQLException e) {
            throw new RentalValidationException(
                    "Gagal mengakses database: " + e.getMessage()
            );
        }
    }

    /**
     * Menampilkan semua transaksi sewa yang masih aktif.
     */
    public void displayActiveRentals()
            throws RentalValidationException {

        String sql =
                "SELECT rental_id, customer_name, customer_phone, "
                + "vehicle_id, rental_days, total_cost, created_at "
                + "FROM rentals "
                + "WHERE status = 'DISEWA' "
                + "ORDER BY created_at DESC";

        String rowFormat = "%-12s %-16s %-14s %-12s %8s %14s %-20s%n";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n          DAFTAR SEWA AKTIF");

            boolean found = false;

            while (rs.next()) {
                if (!found) {
                    System.out.println("-".repeat(100));
                    System.out.printf(rowFormat,
                            "ID Transaksi", "Nama Penyewa", "No. Telepon",
                            "ID Kendaraan", "Durasi", "Total Biaya",
                            "Tanggal Sewa");
                    System.out.println("-".repeat(100));
                    found = true;
                }

                System.out.printf(rowFormat,
                        rs.getString("rental_id"),
                        rs.getString("customer_name"),
                        rs.getString("customer_phone"),
                        rs.getString("vehicle_id"),
                        rs.getInt("rental_days") + " hari",
                        String.format("Rp%,.0f", rs.getDouble("total_cost")),
                        rs.getString("created_at"));
            }

            if (!found) {
                System.out.println(
                        "Tidak ada transaksi sewa yang masih aktif."
                );
            } else {
                System.out.println("-".repeat(100));
            }

        } catch (SQLException e) {
            throw new RentalValidationException(
                    "Gagal mengambil riwayat sewa aktif: "
                    + e.getMessage()
            );
        }
    }

    /**
     * Membatalkan perubahan database jika proses gagal.
     */
    private void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException e) {
            System.err.println(
                    "Peringatan: Rollback database gagal: "
                    + e.getMessage()
            );
        }
    }
}