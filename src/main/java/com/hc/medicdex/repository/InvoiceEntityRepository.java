package com.hc.medicdex.repository;

import com.hc.medicdex.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceEntityRepository extends JpaRepository<InvoiceEntity, Integer> {

    InvoiceEntity findTopByOrderByIdDesc();
}