import config.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("     SISTEM RENTAL KENDARAAN (SI204)     ");
        System.out.println("=========================================");

        // Inisialisasi database SQLite otomatis saat aplikasi start
        DatabaseConnection.initializeDatabase();

        System.out.println("Status: Database & Baseline Project Siap!");
        System.out.println("Silakan lanjutkan implementasi fitur sesuai task.md");
        System.out.println("=========================================");
    }
}
