package model;

import strategy.PricingStrategy;

public abstract class Vehicle {

    // Atribut sesuai prinsip Enkapsulasi
    protected String id;
    protected String brand;
    protected String model;
    protected String plateNumber;
    protected boolean available;
    protected PricingStrategy pricingStrategy;

    // Constructor
    public Vehicle(String id, String brand, String model,
                   String plateNumber, boolean available,
                   PricingStrategy pricingStrategy) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.plateNumber = plateNumber;
        this.available = available;
        this.pricingStrategy = pricingStrategy;
    }

    // Method abstrak (Abstraksi & Polimorfisme)
    public abstract String getType();

    public abstract double getBaseDailyRate();

    // Menghitung biaya sewa dengan Strategy Pattern
    public double calculateRentalCost(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException(
                "Durasi sewa harus lebih dari 0 hari."
            );
        }

        if (pricingStrategy == null) {
            return getBaseDailyRate() * days;
        }

        return pricingStrategy.calculate(
            getBaseDailyRate(), days
        );
    }

    // Mengatur ketersediaan kendaraan
    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Mengatur strategi harga (Dukungan perubahan strategi secara dinamis)
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    // Getter
    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public boolean getAvailable() {
        return available;
    }

    public boolean isAvailable() {
        return available;
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    @Override
    public String toString() {
        return getType()
            + " | ID: " + id
            + " | Brand: " + brand
            + " | Model: " + model
            + " | Plat: " + plateNumber
            + " | Tarif/hari: Rp"
            + String.format("%,.0f", getBaseDailyRate())
            + " | Status: "
            + (available ? "Tersedia" : "Disewa");
    }
}

