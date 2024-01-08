package com.example.customerskyc.service;

import com.example.customerskyc.dto.CustomerKycRequest;
import com.example.customerskyc.dto.CustomerKycResponse;
import com.example.customerskyc.model.CustomerKyc;
import com.example.customerskyc.repository.CustomerKycRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CustomerKycServiceImplTest {
    @Autowired
    private CustomerKycService customerKycService;
    @Autowired
    private CustomerKycRepository customerKycRepository;

    @BeforeEach
//    void setUp() {
//        customerKycService= new CustomerKycServiceImpl();
//    }
    @Test
    void testThatCustomerKycCanBeCreated(){
        CustomerKycRequest customerKycRequest = new CustomerKycRequest();
        customerKycRequest.setFullName("John James");
        customerKycRequest.setContactAddress("2, makoko street");
        customerKycRequest.setPhoneNumber("+2345674356456");
        customerKycRequest.setEmail("johnjames@gmail.com");

        CustomerKycResponse customerResponse =customerKycService.createCustomersKyc(customerKycRequest);

        assertEquals(customerResponse.getEmail(),"johnjames@gmail.com");
    }
    @Test
    void testThatAllCustomerKycCanBereturned(){
        CustomerKycRequest customerKycRequest = new CustomerKycRequest();
        customerKycRequest.setFullName("John James");
        customerKycRequest.setContactAddress("2, makoko street");
        customerKycRequest.setPhoneNumber("+2345674356456");
        customerKycRequest.setEmail("johnjames@gmail.com");

        customerKycService.createCustomersKyc(customerKycRequest);
        List<CustomerKyc> customers = customerKycRepository.findAll();
        System.out.println(customers);

     assertEquals(customers.size(), 1);
    }

}