package com.example.customerskyc.service;

import com.example.customerskyc.dto.CustomerKycRequest;
import com.example.customerskyc.dto.CustomerKycResponse;
import com.example.customerskyc.model.CustomerKyc;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface CustomerKycService {

    CustomerKycResponse createCustomersKyc(CustomerKycRequest customerKycRequest);


    ByteArrayInputStream downloadCustomerKycFromExcelSheetForm();
    ByteArrayInputStream downloadCustomerKycAsPdf();

     void sendCustomerKycByEmail(String recipientEmail);

}
