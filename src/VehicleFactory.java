
public class VehicleFactory {

    public static Vehicle createVehicle(
            String type,
            String id,
            String brand,
            String model,
            String plateNumber,
            boolean available,
            PricingStrategy pricingStrategy) {

        if (type == null) {
            throw new IllegalArgumentException(
                "Jenis kendaraan tidak boleh kosong."
            );
        }

        switch (type.trim().toLowerCase()) {
            case "motor":
            case "motorcycle":
                return new Motorcycle(
                    id, brand, model, plateNumber,
                    available, pricingStrategy
                );

            case "mobil":
            case "car":
                return new Car(
                    id, brand, model, plateNumber,
                    available, pricingStrategy
                );

            case "sepeda listrik":
            case "electricbike":
            case "electric bike":
                return new ElectricBike(
                    id, brand, model, plateNumber,
                    available, pricingStrategy
                );

            default:
                throw new IllegalArgumentException(
                    "Jenis kendaraan tidak dikenal: " + type
                );
        }
    }
}
