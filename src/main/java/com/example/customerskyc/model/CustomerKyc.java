package com.example.customerskyc;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerKyc {
    @Id
    private Long id;

   private String fullName;
   private String phoneNumber;
   private String 

}
