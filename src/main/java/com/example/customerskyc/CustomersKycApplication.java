package com.example.customerskyc;

import com.example.customerskyc.model.CustomerKyc;
import com.example.customerskyc.repository.CustomerKycRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CustomersKycApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomersKycApplication.class, args);
    }
    @Bean
    public CommandLineRunner loadData(CustomerKycRepository customerKycRepository){
        return  args -> {
            CustomerKyc customerKyc = new CustomerKyc();
            customerKyc.setId(1L);
            customerKyc.setEmail("jamesjohn@gmail.com");
            customerKyc.setContactAddress("3, simons street");
            customerKyc.setFullName("John James");
            customerKyc.setPhoneNumber("+234804536272");

            CustomerKyc customer2 = new CustomerKyc();
            customer2.setId(2L);
            customer2.setPhoneNumber("+23490564378");
            customer2.setEmail("ritaSmith@gmail.com");
            customer2.setContactAddress("6,gorgery lane");
            customer2.setFullName("rita smith");

            customerKycRepository.save(customerKyc);
            customerKycRepository.save(customer2);

        };

    }

}
