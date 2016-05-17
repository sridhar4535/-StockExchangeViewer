package com.usc.meg.stockexchangeviewer;

/**
 * Created by sbmeg on 24/04/16.
 */
public class StockSummary {
    private String name;
    private String symbol;
    private String exchange;

    public StockSummary(String name, String symbol, String exchange) {
        this.name = name;
        this.symbol = symbol;
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return symbol.toString();
    }
}
