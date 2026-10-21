
public class Motorcycle extends Vehicle {

    public Motorcycle(String id, String brand, String model,
                      String plateNumber, boolean available,
                      PricingStrategy pricingStrategy) {

        super(id, brand, model, plateNumber,
              available, pricingStrategy);
    }

    @Override
    public String getType() {
        return "Motor";
    }

    @Override
    public double getBaseDailyRate() {
        return 75000;
    }
}
