package ee.jackaltech.energyconsumptionapp;

import org.springframework.boot.SpringApplication;

public class TestEnergyConsumptionAppApplication {

    public static void main(String[] args) {
        SpringApplication.from(EnergyConsumptionAppApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
