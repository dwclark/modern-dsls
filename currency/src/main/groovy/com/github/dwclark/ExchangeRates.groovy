package com.github.dwclark;

import static com.github.dwclark.CurrencyId.*;
import groovy.transform.CompileStatic;

@CompileStatic
class ExchangeRates {

    static final Map<CurrencyId,Map<CurrencyId,BigDecimal>> rates =
        [ (DOLLAR): [ (POUND): 0.75, (EURO): 0.9, (RUPEE): 67.25 ],
          (POUND): [ (DOLLAR): 1.33, (EURO): 1.19, (RUPEE): 89.44 ],
          (EURO): [ (DOLLAR): 1.12, (POUND): 0.84, (RUPEE): 75.04 ],
          (RUPEE): [ (DOLLAR): 0.015, (POUND): 0.011, (EURO): 0.013 ] ] as Map<CurrencyId,Map<CurrencyId,BigDecimal>>;

    static BigDecimal convertValue(final Currency val, final CurrencyId target) {
        rates[val.id][target] * val.amount;
    }

    static Currency convert(final Currency val, final CurrencyId target) {
        return new Currency(target, convertValue(val, target));
    }
}
