package main.java.com.company.stockmarket.core;

import main.java.com.company.stockmarket.utils.StockType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Abstract class which contains implementation of methods and fields that are common to all types of stocks
 */
public abstract class Stock {
    private String stockSymbol;
    private StockType stockType;
    private BigDecimal lastDividend;
    private BigDecimal parValue;

    public Stock(String stockSymbol, StockType stockType, BigDecimal lastDividend, BigDecimal parValue) {
        this.stockSymbol = stockSymbol;
        this.stockType = stockType;
        this.lastDividend = lastDividend;
        this.parValue = parValue;
    }

    public abstract BigDecimal calculateDividendYield(BigDecimal tradePrice);

    /**
     * Provided implementation for calculating P/E ratio since it is the same for types of stocks
     * @param tradePrice the trade price
     * @return the P/E ratio
     */
    public BigDecimal calculatePriceEarningsRatio(BigDecimal tradePrice) {
        BigDecimal priceEarningsRatio = BigDecimal.ZERO;

        if (tradePrice != null && tradePrice.compareTo(BigDecimal.ZERO) > 0 && lastDividend != null && lastDividend.compareTo(BigDecimal.ZERO) > 0) {
            priceEarningsRatio = tradePrice.divide(lastDividend, 6, RoundingMode.HALF_UP);
        } else {

        }

        return priceEarningsRatio;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public StockType getStockType() {
        return stockType;
    }

    public void setStockType(StockType stockType) {
        this.stockType = stockType;
    }

    public BigDecimal getLastDividend() {
        return lastDividend;
    }

    public void setLastDividend(BigDecimal lastDividend) {
        this.lastDividend = lastDividend;
    }

    public BigDecimal getParValue() {
        return parValue;
    }

    public void setParValue(BigDecimal parValue) {
        this.parValue = parValue;
    }

}
