package com.example.merchstore.components.models;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.09.2024
 */

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The ExchangeRate class represents an exchange rate in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has four fields:
 * <ul>
 *     <li>id: The ID of the exchange rate.</li>
 *     <li>currencyId: The ID of the currency associated with this exchange rate.</li>
 *     <li>exchangeRate: The value of the exchange rate.</li>
 *     <li>lastUpdated: The date and time when the exchange rate was last updated.</li>
 * </ul>
 *
 * It also includes methods to display data and limited display data.
 *
 * This class is also a JPA entity, meaning it is mapped to a corresponding table in the database.
 *
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.09.2024
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchange_rates")
public class ExchangeRate implements DataDisplay {

    /**
     * The ID of the exchange rate.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The the currency associated with this exchange rate.
     * @see Currency
     */
    @JoinColumn(name = "currency_id")
    @ManyToOne
    private Currency currencyId;

    /**
     * The value of the exchange rate.
     */
    @Column(name = "exchange_rate")
    private Double exchangeRate;

    /**
     * The date and time when the exchange rate was updated.
     */
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    /**
     * The copy constructor for the ExchangeRate class.
     * @param exchangeRate The ExchangeRate object to copy.
     */
    public ExchangeRate(ExchangeRate exchangeRate) {
        this.id = exchangeRate.getId();
        this.currencyId = exchangeRate.getCurrencyId();
        this.exchangeRate = exchangeRate.getExchangeRate();
        this.lastUpdated = exchangeRate.getLastUpdated();
    }

    /**
     * Display the data of the exchange rate.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the exchange rate data.
     */
    @Override
    public DataDisplay displayData() {
        return new ExchangeRate(this);
    }

    /**
     * Display the limited data of the exchange rate.
     *
     * @see DataDisplay
     *
     * @return a DataDisplay object representing the limited exchange rate data.
     */
    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}

