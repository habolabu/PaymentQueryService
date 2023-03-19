package edu.ou.paymentqueryservice;

import edu.ou.coreservice.annotation.BaseQueryAnnotation;
import org.springframework.boot.SpringApplication;

@BaseQueryAnnotation
public class PaymentQueryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentQueryServiceApplication.class, args);
    }

}
