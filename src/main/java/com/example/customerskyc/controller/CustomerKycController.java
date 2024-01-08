package com.example.customerskyc.controller;


import com.example.customerskyc.service.CustomerKycService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.ByteArrayInputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;



import static org.apache.tomcat.util.http.fileupload.FileUploadBase.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.parseMediaType;

@RestController
@RequestMapping("api/v1/customerKyc")
public class CustomerKycController {
    @Autowired
    private CustomerKycService customerKycService;

    @GetMapping("/excel/uploadCustomerKyc")
    public ResponseEntity<?> downloadAllWebTransactionExcelFile() {
//        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "web_customerKyc".concat(".xls");
        var response = customerKycService.downloadCustomerKycFromExcelSheetForm();
        InputStreamResource resource = new InputStreamResource(response);
        return ResponseEntity.ok().header(CONTENT_DISPOSITION, "attachment;filename=" + filename)
                .contentType(parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }

    @PostMapping("/pdf/downloadCustomerKyc")
    public ResponseEntity<?> generatePdf() {
        ByteArrayInputStream pdfStream = customerKycService.downloadCustomerKycAsPdf();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "customer_kyc.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(new InputStreamResource(pdfStream), headers, HttpStatus.OK);
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendPdfAndExcelFileByEmail(@RequestParam("recipientEmail") String recipientEmail) {
        customerKycService.sendCustomerKycByEmail(recipientEmail);
        return ResponseEntity.ok("Email sent successfully");
    }
}






