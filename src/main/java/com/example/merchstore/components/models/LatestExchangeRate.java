package com.example.merchstore.components.models;

/**
 * @author Tomasz Zbroszczyk
 * @version 1.0
 * @since 04.09.2024
 */

import com.example.merchstore.components.interfaces.DataDisplay;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * The LatestExchangeRate class represents the latest exchange rate in the system.
 * It implements the DataDisplay interface to control the display of data.
 *
 * It has four fields:
 * <ul>
 *     <li>id: The ID of the latest exchange rate.</li>
 *     <li>currencyId: The ID of the currency associated with this latest exchange rate.</li>
 *     <li>exchangeRate: The value of the latest exchange rate.</li>
 *     <li>lastUpdated: The date and time when the latest exchange rate was last updated.</li>
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
@Table(name = "latest_exchange_rates")
public class LatestExchangeRate implements DataDisplay {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "currency_id")
    private Long currencyId;

    @Column(name = "exchange_rate")
    private Double exchangeRate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;


    @Override
    public DataDisplay displayData() {
        return null;
    }

    @Override
    public DataDisplay limitedDisplayData() {
        return null;
    }
}
