package service;

import model.RentalTransaction;
import model.Vehicle;

/**
 * Jembatan kompilasi untuk Fitur 4.
 * Pemilihan strategi diskon tetap diisi oleh bagian Arif.
 */
public class CostCalculatorService {

    public double calculateCost(Vehicle vehicle, int days) {
        return vehicle.calculateRentalCost(days);
    }

    public void printReceipt(RentalTransaction trx, Vehicle vehicle) {
        // Struk formal diimplementasikan pada Fitur 4.
    }
}
