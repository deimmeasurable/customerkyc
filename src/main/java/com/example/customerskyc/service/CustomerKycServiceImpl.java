package com.example.customerskyc.service;

import com.example.customerskyc.dto.CustomerKycRequest;
import com.example.customerskyc.dto.CustomerKycResponse;
import com.example.customerskyc.model.CustomerKyc;
import com.example.customerskyc.repository.CustomerKycRepository;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;

import com.itextpdf.text.pdf.PdfWriter;


import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

import org.springframework.stereotype.Service;


import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;


import static org.apache.poi.ss.usermodel.Color.*;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;


@AllArgsConstructor
@Service
public class CustomerKycServiceImpl implements CustomerKycService {


    private final CustomerKycRepository customerKycRepository;

    private final String senderEmail = "sseun976@gmail.com";

    @Override
    public CustomerKycResponse createCustomersKyc(CustomerKycRequest customerKycRequest) {
        CustomerKyc customer = new CustomerKyc();
        customer.setFullName(customerKycRequest.getFullName());
        customer.setPhoneNumber(customerKycRequest.getPhoneNumber());
        customer.setContactAddress(customerKycRequest.getContactAddress());
        customer.setEmail(customerKycRequest.getEmail());

        CustomerKycResponse customerResponse = new CustomerKycResponse();
        customerResponse.setEmail(customer.getEmail());
        customerResponse.setContactAddress(customer.getContactAddress());
        customerResponse.setPhoneNumber(customer.getPhoneNumber());
        customerResponse.setFullName(customer.getFullName());

        customerKycRepository.save(customer);
        System.out.println(" customer created;" + customer);
        return customerResponse;
    }


    @Override
    public ByteArrayInputStream downloadCustomerKycFromExcelSheetForm() {
        try (Workbook workbook = new HSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet();
            var customerKyc = customerKycRepository.findAll();
            String[] headers = {"id", "full name", "phoneNumber", "contactAddress", "email"};
            createHeader(headers, sheet, workbook);
            writeValuesToExcelForCustomerKyc(sheet, customerKyc);
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createHeader(String[] headers, Sheet sheet, Workbook workbook) {
        CellStyle headerStyle = createHeaderStyle(sheet, workbook);
        for (int index = 0; index < headers.length; index++) {
            sheet.setColumnWidth(index, 5000);
            Cell cell = sheet.getRow(0).createCell(index);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(headers[index]);

        }
    }

    private CellStyle createHeaderStyle(Sheet sheet, Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);
        font.setFontHeight((short) 200);
        headerStyle.setAlignment(CENTER);
        sheet.createRow(0);
        return headerStyle;
    }

    private void writeValuesToExcelForCustomerKyc(Sheet sheet, List<CustomerKyc> customerKyc) {
        int ONE = 1;
        int ZERO = 0;
        for (int index = 0; index < customerKyc.size(); index++) {
            var customer = customerKyc.get(index);
            sheet.createRow(index + ONE).createCell(ZERO).setCellValue(customer.getId().toString());
            sheet.getRow(index + ONE).getCell(1, CREATE_NULL_AS_BLANK).setCellValue(customer.getFullName());
            sheet.getRow(index + ONE).getCell(2, CREATE_NULL_AS_BLANK).setCellValue(customer.getPhoneNumber());
            sheet.getRow(index + ONE).getCell(3, CREATE_NULL_AS_BLANK).setCellValue(customer.getContactAddress());
            sheet.getRow(index + ONE).getCell(4, CREATE_NULL_AS_BLANK).setCellValue(customer.getEmail());

        }
    }

    @Override
    public ByteArrayInputStream downloadCustomerKycAsPdf() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();

            List<CustomerKyc> customerKycList = customerKycRepository.findAll();

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10);


            addTableHeader(table);



            for (CustomerKyc customer : customerKycList) {
                String [] customerKycDetail={String.valueOf(customer.getId()), customer.getFullName(), customer.getEmail(), customer.getPhoneNumber(), customer.getContactAddress()};
                addTableRow(table,customerKycDetail );
            }

            document.add(table);

            document.close();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addTableHeader(PdfPTable table) {
        String[] header = {"ID", "Full Name", "Email", "Phone Number", "Contact Address"};
        for (String collect : header) {
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(java.awt.Color.GRAY);
            headerCell.setPhrase(new Phrase(collect));

            table.addCell(collect);
            
        }

    }




    private void addTableRow(PdfPTable table, String[] cellValues) {
        for (String customerKyc : cellValues) {
            table.addCell(customerKyc);
        }
    }
    private Session initializeSession() {
        String port = "465";

        String senderPassword= "vebl qlhg apix fmbd";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.user", senderEmail);
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.socketFactory.port", port);
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

     Session   session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });
        session.setDebug(true);
        return session;
    }
    @Override
    public void sendCustomerKycByEmail(String recipientEmail) {
        ByteArrayInputStream excelStream = downloadCustomerKycFromExcelSheetForm();
        ByteArrayInputStream pdfStream = downloadCustomerKycAsPdf();

        try {
            MimeMessage message = createMessage(recipientEmail);
            addAttachmentsToMessage(message, excelStream, pdfStream);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private MimeMessage createMessage(String recipientEmail) throws MessagingException {
        Session session=initializeSession();
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Customer KYC Data");
        message.setText("Please find attached the Customer KYC data in Excel and PDF formats.");
        return message;
    }
    private void addAttachmentsToMessage(MimeMessage message, ByteArrayInputStream excelStream, ByteArrayInputStream pdfStream) throws MessagingException, IOException {
        Multipart multipart = new MimeMultipart();

        MimeBodyPart excelAttachment = new MimeBodyPart();
        excelAttachment.setDataHandler(new DataHandler(new ByteArrayDataSource(excelStream, "application/vnd.ms-excel")));
        excelAttachment.setFileName("customer_kyc.xls");
        multipart.addBodyPart(excelAttachment);

        MimeBodyPart pdfAttachment = new MimeBodyPart();
        pdfAttachment.setDataHandler(new DataHandler(new ByteArrayDataSource(pdfStream, "application/pdf")));
        pdfAttachment.setFileName("customer_kyc.pdf");
        multipart.addBodyPart(pdfAttachment);

        message.setContent(multipart);
    }
}







