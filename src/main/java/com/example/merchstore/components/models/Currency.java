package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 03.09.2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency implements DataDisplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "short", unique = true, nullable = false)
    private String shortName;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "name")
    private String name;

    @Column(name = "exchange_rate")
    private double exchangeRate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public Currency(Currency currency) {
        this.id = currency.getId();
        this.shortName = currency.getShortName();
        this.symbol = currency.getSymbol();
        this.name = currency.getName();
        this.exchangeRate = currency.getExchangeRate();
        this.lastUpdated = currency.getLastUpdated();
    }

    @Override
    public DataDisplay displayData() {
        return null;
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
