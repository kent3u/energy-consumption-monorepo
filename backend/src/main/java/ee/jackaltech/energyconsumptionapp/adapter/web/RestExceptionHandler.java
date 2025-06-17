package ee.jackaltech.energyconsumptionapp.adapter.web;

import ee.jackaltech.energyconsumptionapp.appdomain.consumption.exception.MissingElectricityPriceDataException;
import ee.jackaltech.energyconsumptionapp.appdomain.meteringpoint.exception.UnauthorizedMeteringPointAccessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(Exception e) {
        log.error("Unhandled exception", e);
    }

    @ExceptionHandler(UnauthorizedMeteringPointAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleUnauthorizedMeteringPointAccessException(UnauthorizedMeteringPointAccessException e) {
        log.info("Customer tried to access metering point not authorized for them", e);
    }

    @ExceptionHandler(MissingElectricityPriceDataException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public void handleMissingElectricityPriceDataException(MissingElectricityPriceDataException e) {
        log.warn("No price was found for given consumption data point", e);
    }
}
