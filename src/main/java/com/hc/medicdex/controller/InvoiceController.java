package com.hc.medicdex.controller;

import com.hc.medicdex.dto.InvoiceDto;
import com.hc.medicdex.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;
    @PostMapping
    public ResponseEntity<?> saveInvoice(@RequestBody InvoiceDto invoiceDto) throws ParseException {
        invoiceService.saveInvoice(invoiceDto);
        return new ResponseEntity<>("created", HttpStatus.OK);
    }
    @GetMapping
    public Integer getUpdatedInvoiceNumber(){
        return invoiceService.getLatestInvoiceNumber();
    }
}
