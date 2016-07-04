package com.github.dwclark;

import static com.github.dwclark.ExchangeRates.*;
import static com.github.dwclark.CurrencyId.*;
import groovy.transform.CompileStatic;

@CompileStatic
class Currency {
    final CurrencyId id;
    final BigDecimal amount;

    Currency(final CurrencyId id, final BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "${id.symbol}${amount}";
    }

    Currency plus(final Currency rhs) {
        return new Currency(id, amount + convertValue(rhs, id));
    }

    Currency minus(final Currency rhs) {
        return new Currency(id, amount - convertValue(rhs, id));
    }

    Currency multiply(final Number rhs) {
        return new Currency(id, rhs * amount);
    }

    Currency div(final Number rhs) {
        return new Currency(id, amount / rhs);
    }

    Currency positive() {
        return (amount >= 0) ? this : new Currency(id, amount.abs())
    }

    Currency negative() {
        return new Currency(id, -amount);
    }

    Object asType(final Class type) {
        if(type == Boolean || type == boolean) {
            return amount.compareTo(BigDecimal.ZERO) == 0;
        }
        else {
            throw new ClassCastException("Can't cast Currency to ${type}");
        }
    }

    Currency getDollars() {
        return id == DOLLAR ? this : convert(this, DOLLAR);
    }

    Currency getPounds() {
        return id == POUND ? this : convert(this, POUND);
    }

    Currency getEuros() {
        return id == EURO ? this : convert(this, EURO);
    }

    Currency getRupees() {
        return id == RUPEE ? this : convert(this, RUPEE);
    }
}
