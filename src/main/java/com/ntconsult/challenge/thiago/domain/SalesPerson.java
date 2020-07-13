package com.ntconsult.challenge.thiago.domain;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class SalesPerson {
    private String id;
    private String name;
    private BigDecimal salary;
}
