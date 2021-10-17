package com.exxair.model;

import com.exxair.enums.Currency;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String pesel;

    @ElementCollection
    @CollectionTable(name = "subaccounts")
    @MapKeyColumn(name = "currency")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "value", scale = 2)
    private final Map<Currency, BigDecimal> subaccounts = new HashMap<>();

    public Account(String name, String surname, String pesel, BigDecimal initialValue) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.subaccounts.put(Currency.PLN, initialValue);
        this.subaccounts.put(Currency.USD, BigDecimal.valueOf(0, 2));
    }
}
