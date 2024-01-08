package com.example.customerskyc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerKycRequest {
    private String fullName;
    private String phoneNumber;
    private String contactAddress;
    private String email;
}
