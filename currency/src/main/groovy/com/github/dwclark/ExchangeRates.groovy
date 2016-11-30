package com.github.dwclark;

import static com.github.dwclark.CurrencyId.*;
import groovy.transform.CompileStatic;

@CompileStatic
class ExchangeRates {

    static final Map<CurrencyId,Map<CurrencyId,BigDecimal>> rates =
        [ (DOLLAR): [ (POUND): 0.8, (EURO): 0.94, (RUPEE): 68.53 ],
          (POUND): [ (DOLLAR): 1.25, (EURO): 1.17, (RUPEE): 85.51 ],
          (EURO): [ (DOLLAR): 1.06, (POUND): 0.85, (RUPEE): 72.86 ],
          (RUPEE): [ (DOLLAR): 0.015, (POUND): 0.012, (EURO): 0.014 ] ] as Map<CurrencyId,Map<CurrencyId,BigDecimal>>;

    static BigDecimal convertValue(final Currency val, final CurrencyId target) {
        rates[val.id][target] * val.amount;
    }

    static Currency convert(final Currency val, final CurrencyId target) {
        return new Currency(target, convertValue(val, target));
    }
}
