import static CurrencyId.*;

class CurrencyExtensions {

    static Currency getDollars(final Number n) {
        return new Currency(n as BigDecimal, DOLLARS);
    }

    static Currency getPounds(final Number n) {
        return new Currency(n as BigDecimal, POUNDS);
    }

    static Currency getEuros(final Number n) {
        return new Currency(n as BigDecimal, EUROS);
    }

    static Currency getRupees(final Number n) {
        return new Currency(n as BigDecimal, RUPEES);
    }

    static Currency multiply(final Number n, final Currency c) {
        return new Currency(c.id, c.amount * n);
    }
}
