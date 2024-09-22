package com.qrgenerator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qrgenerator.service.QRCodeGeneratorService;

@RestController
public class QRCodeGeneratorController {

	 @Autowired
	    private QRCodeGeneratorService qrCodeGeneratorService;

	 @GetMapping("/generate-upi-qr")
	    public ResponseEntity<byte[]> generateUPIQRCode(
	            @RequestParam("pa") String payeeAddress,
	            @RequestParam("pn") String payeeName,
	            @RequestParam("am") String amount,
	            @RequestParam(value = "tn", required = false) String transactionNote) {
	        try {
	            // Build UPI payment URL
	            String upiUrl = "upi://pay?pa=" + payeeAddress
	                    + "&pn=" + payeeName
	                    + "&am=" + amount
	                    + "&cu=INR"
	                    + (transactionNote != null ? "&tn=" + transactionNote : "");
	            
	            // Generate QR Code
	            byte[] qrCodeImage = qrCodeGeneratorService.generateQRCode(upiUrl, 250, 250);

	            // Set response headers for PNG
	            HttpHeaders headers = new HttpHeaders();
	            headers.set(HttpHeaders.CONTENT_TYPE, "image/png");

	            // Return QR code image
	            return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
	        } catch (Exception e) {
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }
}
