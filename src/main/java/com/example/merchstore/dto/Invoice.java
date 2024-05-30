package com.example.merchstore.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 28.05.2024
 */

@Data @AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice implements DataDisplay{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Column(name = "invoice_date", nullable = false)
    private LocalDateTime invoiceDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "status", nullable = false)
    private String status;

    public Invoice(Invoice other) {
        this.invoiceId = other.invoiceId;
        this.order = other.order;
        this.invoiceDate = other.invoiceDate;
        this.amount = other.amount;
        this.status = other.status;
    }

    @Override
    public DataDisplay displayData() {
        return new Invoice(this);
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}

