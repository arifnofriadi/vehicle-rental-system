package model;

import strategy.PricingStrategy;

public class ElectricBike extends Vehicle {

    public ElectricBike(String id, String brand, String model,
                        String plateNumber, boolean available,
                        PricingStrategy pricingStrategy) {

        super(id, brand, model, plateNumber,
              available, pricingStrategy);
    }

    @Override
    public String getType() {
        return "Sepeda Listrik";
    }

    @Override
    public double getBaseDailyRate() {
        return 50000;
    }
}

