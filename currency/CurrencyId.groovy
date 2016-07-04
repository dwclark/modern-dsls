enum CurrencyId {

    DOLLAR('$'), POUND('£'), EURO('€'), RUPEE('₹');

    private CurrencyId(final String symbol) {
        this.symbol = symbol;
    }

    final String symbol;
}
