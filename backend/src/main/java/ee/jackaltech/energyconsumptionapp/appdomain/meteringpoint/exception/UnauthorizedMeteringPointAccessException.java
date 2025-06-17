package ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.exception;

public class UnauthorizedMeteringPointAccessException extends RuntimeException {

    public UnauthorizedMeteringPointAccessException() {
        super("Customer is not allowed to access given metering point");
    }
}
