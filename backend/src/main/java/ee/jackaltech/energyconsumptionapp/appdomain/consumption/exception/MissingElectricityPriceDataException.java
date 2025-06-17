package ee.jackaltech.energyconsumptionapp.appdomain.consumption.exception;

public class MissingElectricityPriceDataException extends RuntimeException {

    public MissingElectricityPriceDataException() {
        super("Electricity price point not found for consumption");
    }
}
