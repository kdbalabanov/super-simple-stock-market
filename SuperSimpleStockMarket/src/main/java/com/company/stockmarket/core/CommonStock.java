package main.java.com.company.stockmarket.core;

import main.java.com.company.stockmarket.utils.StockType;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Child class of Stock meant to represent a CommonStock
 */
public class CommonStock extends Stock {

    public CommonStock(String stockSymbol, StockType stockType, BigDecimal lastDividend, BigDecimal parValue) {
        super(stockSymbol, stockType, lastDividend, parValue);
    }

    /**
     * CommonStock specific implementation for calculating the dividend yield
     * @param tradePrice the trade price
     * @return the dividend yield
     */
    @Override
    public BigDecimal calculateDividendYield(BigDecimal tradePrice){
        BigDecimal dividendYield = BigDecimal.ZERO;

        if (tradePrice != null && tradePrice.compareTo(BigDecimal.ZERO) > 0 && getStockType() != null) {
            BigDecimal lastDividend = getLastDividend();
            if (lastDividend != null && lastDividend.compareTo(BigDecimal.ZERO) > 0) {
                dividendYield = lastDividend.divide(tradePrice, 6, RoundingMode.HALF_UP);
            }

            return dividendYield;
        }

        return dividendYield;
    }
}
