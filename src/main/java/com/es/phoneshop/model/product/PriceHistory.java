package com.es.phoneshop.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PriceHistory implements Comparable<PriceHistory>, Serializable {
    private LocalDate dateTime;
    private BigDecimal price;

    @Override
    public int compareTo(PriceHistory o) {
        return getDateTime().compareTo(o.getDateTime());
    }
}
