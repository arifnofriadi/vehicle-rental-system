package config;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_DIR = "data";
    private static final String DB_URL = "jdbc:sqlite:" + DB_DIR + "/rental.db";

    /**
     * Membuka koneksi ke database SQLite lokal data/rental.db
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver SQLite JDBC tidak ditemukan di classpath (pastikan lib/sqlite-jdbc*.jar terpasang).");
        }

        // Pastikan folder penampung database (data/) otomatis dibuat jika belum ada
        File folder = new File(DB_DIR);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Menginisialisasi tabel-tabel dan data awal (seeding) secara otomatis
     */
    public static void initializeDatabase() {
        String sqlCreateVehicles = "CREATE TABLE IF NOT EXISTS vehicles ("
                + "id TEXT PRIMARY KEY, "
                + "type TEXT NOT NULL, "
                + "brand TEXT NOT NULL, "
                + "model TEXT NOT NULL, "
                + "plate_number TEXT NOT NULL UNIQUE, "
                + "base_rate REAL NOT NULL, "
                + "is_available INTEGER NOT NULL DEFAULT 1"
                + ");";

        String sqlCreateRentals = "CREATE TABLE IF NOT EXISTS rentals ("
                + "rental_id TEXT PRIMARY KEY, "
                + "customer_name TEXT NOT NULL, "
                + "customer_phone TEXT NOT NULL, "
                + "vehicle_id TEXT NOT NULL, "
                + "rental_days INTEGER NOT NULL, "
                + "total_cost REAL NOT NULL, "
                + "status TEXT NOT NULL DEFAULT 'DISEWA', "
                + "created_at TEXT NOT NULL, "
                + "FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Buat tabel jika belum ada
            stmt.execute(sqlCreateVehicles);
            stmt.execute(sqlCreateRentals);

            // Inisialisasi data awal (seeding) jika tabel kendaraan masih kosong
            seedInitialVehicles(conn);

        } catch (SQLException e) {
            System.err.println("Gagal menginisialisasi database: " + e.getMessage());
        }
    }

    /**
     * Mengisi beberapa data awal armada jika database baru dibuat
     */
    private static void seedInitialVehicles(Connection conn) {
        String checkCountSql = "SELECT COUNT(*) FROM vehicles";
        String insertSql = "INSERT INTO vehicles (id, type, brand, model, plate_number, base_rate, is_available) VALUES (?, ?, ?, ?, ?, ?, 1)";

        try (Statement countStmt = conn.createStatement();
             ResultSet rs = countStmt.executeQuery(checkCountSql)) {

            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                    // 1. Mobil
                    insertVehicle(pstmt, "CAR-001", "Mobil", "Toyota", "Avanza", "B 1234 ABC", 250000.0);
                    insertVehicle(pstmt, "CAR-002", "Mobil", "Honda", "Brio", "D 5678 XYZ", 250000.0);

                    // 2. Motor
                    insertVehicle(pstmt, "MTR-001", "Motor", "Honda", "Vario 160", "B 4321 DEF", 75000.0);
                    insertVehicle(pstmt, "MTR-002", "Motor", "Yamaha", "NMAX 155", "B 8765 GHI", 75000.0);

                    // 3. Sepeda Listrik
                    insertVehicle(pstmt, "EBK-001", "Sepeda Listrik", "Polygon", "G-Bike E2", "L 1122 JKL", 50000.0);
                    insertVehicle(pstmt, "EBK-002", "Sepeda Listrik", "Xiaomi", "Himo Z20", "L 3344 MNO", 50000.0);

                    System.out.println("🌱 Data awal kendaraan berhasil ditambahkan ke database!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Peringatan: Gagal menambahkan data awal: " + e.getMessage());
        }
    }

    private static void insertVehicle(PreparedStatement pstmt, String id, String type,
                                      String brand, String model, String plateNumber, double rate) throws SQLException {
        pstmt.setString(1, id);
        pstmt.setString(2, type);
        pstmt.setString(3, brand);
        pstmt.setString(4, model);
        pstmt.setString(5, plateNumber);
        pstmt.setDouble(6, rate);
        pstmt.executeUpdate();
    }
}

