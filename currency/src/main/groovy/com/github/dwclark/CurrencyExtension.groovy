package com.github.dwclark;

import static com.github.dwclark.CurrencyId.*;
import groovy.transform.CompileStatic;

@CompileStatic
class CurrencyExtension {

    static Currency getDollars(final Number n) {
        return new Currency(DOLLAR, n as BigDecimal);
    }

    static Currency getPounds(final Number n) {
        return new Currency(POUND, n as BigDecimal);
    }

    static Currency getEuros(final Number n) {
        return new Currency(EURO, n as BigDecimal);
    }

    static Currency getRupees(final Number n) {
        return new Currency(RUPEE, n as BigDecimal);
    }

    static Currency multiply(final Number n, final Currency c) {
        return new Currency(c.id, c.amount * n);
    }
}
