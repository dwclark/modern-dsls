import static ExchangeRates.*;
import static CurrencyId.*;

class Currency {
    final CurrencyId id;
    final BigDecimal amount;

    Currency(final CurrencyId id, final BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    Currency plus(final Currency rhs) {
        return new Currency(id, amount + convertValue(rhs, id));
    }

    Currency minus(final Currency rhs) {
        return new Currency(id, amount - convertValue(rhs, id));
    }

    Currency mulitply(final BigDecimal rhs) {
        return new Currency(id, rhs * amount);
    }

    Currency div(final BigDecimal rhs) {
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
            return c.amount.compareTo(BigDecimal.ZERO) == 0;
        }
        else {
            throw new ClassCastException("Can't cast Currency to ${type}");
        }
    }

    Currency getDollars() {
        return id == DOLLARS ? this : convert(this, DOLLARS);
    }

    Currency getPounds() {
        return id == POUNDS ? this : convert(this, PUNDS);
    }

    Currency getEuros() {
        return id == EUROS ? this : convert(this, EUROS);
    }

    Currency getRupees() {
        return id == RUPEES ? this : convert(this, RUPEES);
    }
}
