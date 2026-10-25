package strategy;

public class RegularPricingStrategy implements PricingStrategy {

    @Override
    public double calculate(double baseRate, int days) {
        return baseRate * days;
    }
}
