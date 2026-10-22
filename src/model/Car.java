package model;

import strategy.PricingStrategy;

public class Car extends Vehicle {

    public Car(String id, String brand, String model,
               String plateNumber, boolean available,
               PricingStrategy pricingStrategy) {

        super(id, brand, model, plateNumber,
              available, pricingStrategy);
    }

    @Override
    public String getType() {
        return "Mobil";
    }

    @Override
    public double getBaseDailyRate() {
        return 250000;
    }
}

