package com.ntconsult.challenge.thiago.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Customer {
    private String id;
    private String name;
    private String businessArea;
}
