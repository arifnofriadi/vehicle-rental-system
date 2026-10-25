package strategy;

public class DiscountPricingStrategy implements PricingStrategy {

    @Override
    public double calculate(double baseRate, int days) {
        double total = baseRate * days;
        return (days >= 3) ? total * 0.90 : total;
    }
}
