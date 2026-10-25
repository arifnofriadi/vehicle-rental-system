import config.DatabaseConnection;
import exception.RentalValidationException;
import exception.VehicleNotFoundException;
import model.RentalTransaction;
import model.Vehicle;
import service.CostCalculatorService;
import service.RentalService;
import service.ReturnService;
import service.VehicleService;

import java.util.Scanner;

public class Main {

    private static final VehicleService vehicleService = new VehicleService();
    private static final RentalService rentalService = new RentalService();
    private static final ReturnService returnService = new ReturnService();
    private static final CostCalculatorService costCalculatorService = new CostCalculatorService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("     SISTEM RENTAL KENDARAAN (SI204)     ");
        System.out.println("=========================================");

        DatabaseConnection.initializeDatabase();

        boolean running = true;
        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    vehicleService.displayAllVehicles();
                    break;
                case "2":
                    vehicleService.displayAvailableVehicles();
                    break;
                case "3":
                    handleRentVehicle();
                    break;
                case "4":
                    handleReturnVehicle();
                    break;
                case "5":
                    handleDisplayActiveRentals();
                    break;
                case "6":
                    handleAddVehicle();
                    break;
                case "0":
                    running = false;
                    System.out.println("Terima kasih telah menggunakan sistem rental kendaraan.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n============ MENU UTAMA ============");
        System.out.println("1. Lihat semua kendaraan");
        System.out.println("2. Lihat kendaraan tersedia");
        System.out.println("3. Sewa kendaraan");
        System.out.println("4. Kembalikan kendaraan");
        System.out.println("5. Lihat sewa aktif");
        System.out.println("6. Tambah kendaraan baru");
        System.out.println("0. Keluar");
        System.out.print("Pilih menu: ");
    }

    private static void handleRentVehicle() {
        System.out.print("ID Kendaraan  : ");
        String vehicleId = scanner.nextLine().trim();

        System.out.print("Nama Penyewa  : ");
        String name = scanner.nextLine().trim();

        System.out.print("No. Telepon   : ");
        String phone = scanner.nextLine().trim();

        System.out.print("Durasi (hari) : ");
        int days;
        try {
            days = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Durasi harus berupa angka!");
            return;
        }

        try {
            RentalTransaction trx = rentalService.rentVehicle(
                    vehicleId, name, phone, days, costCalculatorService
            );
            Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
            costCalculatorService.printReceipt(trx, vehicle);

        } catch (VehicleNotFoundException | RentalValidationException e) {
            System.out.println("Gagal menyewa kendaraan: " + e.getMessage());
        }
    }

    private static void handleReturnVehicle() {
        System.out.print("ID Transaksi / ID Kendaraan: ");
        String id = scanner.nextLine().trim();

        try {
            returnService.processReturn(id);
        } catch (RentalValidationException | VehicleNotFoundException e) {
            System.out.println("Gagal memproses pengembalian: " + e.getMessage());
        }
    }

    private static void handleDisplayActiveRentals() {
        try {
            returnService.displayActiveRentals();
        } catch (RentalValidationException e) {
            System.out.println("Gagal mengambil data sewa aktif: " + e.getMessage());
        }
    }

    private static void handleAddVehicle() {
        System.out.print("Jenis (mobil/motor/sepeda listrik): ");
        String type = scanner.nextLine().trim();

        System.out.print("ID Kendaraan  : ");
        String id = scanner.nextLine().trim();

        System.out.print("Merk          : ");
        String brand = scanner.nextLine().trim();

        System.out.print("Model         : ");
        String model = scanner.nextLine().trim();

        System.out.print("Plat Nomor    : ");
        String plateNumber = scanner.nextLine().trim();

        try {
            vehicleService.addVehicle(type, id, brand, model, plateNumber);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Gagal menambah kendaraan: " + e.getMessage());
        }
    }
}
