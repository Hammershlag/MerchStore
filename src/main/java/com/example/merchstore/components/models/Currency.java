package com.example.merchstore.components.models;

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The Currency class represents a currency in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has four fields:
 * <ul>
 *     <li>id: The ID of the currency.</li>
 *     <li>shortName: The short name of the currency.</li>
 *     <li>symbol: The symbol of the currency.</li>
 *     <li>name: The name of the currency.</li>
 * </ul>
 *
 * It also includes methods to display data and limited display data.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
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


    public Currency(Currency currency) {
        this.id = currency.getId();
        this.shortName = currency.getShortName();
        this.symbol = currency.getSymbol();
        this.name = currency.getName();
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
