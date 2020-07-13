package com.ntconsult.challenge.thiago.domain;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Sale {
    private String id;
    private List<Item> items;
    private String name;

    public BigDecimal totalAmount() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items.stream()
            .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal::add)
            .get();
    }
}
