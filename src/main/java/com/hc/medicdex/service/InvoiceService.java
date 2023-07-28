package com.hc.medicdex.service;

import com.hc.medicdex.dto.InvoiceDto;
import com.hc.medicdex.entity.InvoiceEntity;
import com.hc.medicdex.repository.InvoiceEntityRepository;
import com.hc.medicdex.util.DateFormattingUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.text.ParseException;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class InvoiceService {
    @Autowired
    private InvoiceEntityRepository invoiceEntityRepository;
    public Integer getLatestInvoiceNumber(){
        Integer invNumber = 0;
        InvoiceEntity invoice = invoiceEntityRepository.findTopByOrderByIdDesc();
        if(invoice != null){
            invNumber = invoiceEntityRepository.findTopByOrderByIdDesc().getId() + 1;
        }
        return invNumber;
    }
    public void saveInvoice(InvoiceDto invoiceDto) throws ParseException {
        DateFormattingUtil dateFormattingUtil= new DateFormattingUtil();
        InvoiceEntity invoice = InvoiceEntity.builder()
                .amount(invoiceDto.getAmount())
                .createdOn(dateFormattingUtil.getCurrentDateTime())
                .build();
        invoiceEntityRepository.save(invoice);
    }
}
