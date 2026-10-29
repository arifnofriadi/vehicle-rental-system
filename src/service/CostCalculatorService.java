package service;

import model.RentalTransaction;
import model.Vehicle;
import strategy.DiscountPricingStrategy;
import strategy.PricingStrategy;
import strategy.RegularPricingStrategy;

public class CostCalculatorService {

    private final PricingStrategy regular = new RegularPricingStrategy();
    private final PricingStrategy discount = new DiscountPricingStrategy();

    // Diskon 10% otomatis berlaku untuk sewa 3 hari atau lebih
    public double calculateCost(Vehicle vehicle, int days) {
        PricingStrategy strategy = (days >= 3) ? discount : regular;
        vehicle.setPricingStrategy(strategy);
        return vehicle.calculateRentalCost(days);
    }

    public void printReceipt(RentalTransaction trx, Vehicle vehicle) {
        System.out.println("\n==================================================");
        System.out.println("               STRUK SEWA KENDARAAN");
        System.out.println("==================================================");
        System.out.println("ID Transaksi  : " + trx.getRentalId());
        System.out.println("Nama Penyewa  : " + trx.getCustomerName());
        System.out.println("No. Telepon   : " + trx.getCustomerPhone());
        System.out.println("Tanggal Sewa  : " + trx.getRentalDate());
        System.out.println("--------------------------------------------------");
        System.out.println("Kendaraan     : " + vehicle.getType() + " "
                + vehicle.getBrand() + " " + vehicle.getModel());
        System.out.println("Plat Nomor    : " + vehicle.getPlateNumber());
        System.out.printf("Tarif/Hari    : Rp%,.0f%n", vehicle.getBaseDailyRate());
        System.out.println("Durasi Sewa   : " + trx.getRentalDays() + " hari");

        if (trx.getRentalDays() >= 3) {
            System.out.println("Diskon        : 10% (sewa 3 hari atau lebih)");
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("TOTAL BIAYA   : Rp%,.0f%n", trx.getTotalCost());
        System.out.println("Status Sewa   : " + trx.getStatus());
        System.out.println("==================================================\n");
    }
}
