package com.example.customerskyc.repository;

import com.example.customerskyc.model.CustomerKyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CustomerKycRepository extends JpaRepository<CustomerKyc,Long> {

}
